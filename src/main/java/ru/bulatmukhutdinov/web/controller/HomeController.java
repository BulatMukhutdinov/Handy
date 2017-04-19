package ru.bulatmukhutdinov.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.bulatmukhutdinov.dto.AccountDto;
import ru.bulatmukhutdinov.dto.CategoryDto;
import ru.bulatmukhutdinov.persistance.model.*;
import ru.bulatmukhutdinov.registration.OnRegistrationCompleteEvent;
import ru.bulatmukhutdinov.service.*;
import ru.bulatmukhutdinov.web.error.AccountAlreadyExistException;
import ru.bulatmukhutdinov.web.util.GenericResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by Reverendo on 17.02.2017.
 */

@Controller
public class HomeController {

    @Autowired
    private Environment env;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private LangService langService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CategoryTextService categoryTextService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ServiceService serviceService;

    private List<Service> foundServices;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getDefault() {
        return "redirect:/home";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getHome(Locale locale, final Model model) {
        List<Category> categories = new ArrayList<>();
        if (foundServices == null) {
            categories = categoryService.findAll();
        } else {
            for (Service service : foundServices) {
                if (!categories.contains(service.getCategory())) {
                    categories.add(service.getCategory());
                }
            }
            foundServices = null;
        }
        Map<CategoryDto, Set<Service>> categoryDtoListMap = new HashMap<>();
        Lang language = null;
        List<Lang> langList = langService.getLangs();
        for (Lang lang : langList) {
            if (locale.getLanguage().equals(lang.getName())) {
                language = lang;
                break;
            }
        }
        for (Category category : categories) {
            if (!category.getServices().isEmpty()) {
                categoryDtoListMap.put(new CategoryDto(categoryTextService.findByLang(category, language).getText()), category.getServices());
            }
        }
        model.addAttribute("categoryServices", categoryDtoListMap);
        return "home";
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(final Locale locale, final Model model, @RequestParam("token") final String token) throws UnsupportedEncodingException {
        final String result = accountService.validateVerificationToken(token);
        if (result.equals("valid")) {
            model.addAttribute("registrationConfirm", "valid");
            return getHome(locale, model);
        }

        model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
        model.addAttribute("expired", "expired".equals(result));
        model.addAttribute("token", token);
        return "redirect:/badUser.html?lang=" + locale.getLanguage();
    }

    @RequestMapping(value = "/fastOrder", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse fastOrder(@RequestParam("name") String name, @RequestParam("date") String date,
                                     @RequestParam("time") String time, @RequestParam("phone") String phone,
                                     @RequestParam("address") String address,
                                     @RequestParam(value = "email", required = false) String email,
                                     @RequestParam(value = "wishes", required = false) String wishes) {

        final String subject = "Заявка с севриса Handy";
        String message = "Доброго времени суток! Вам была оставлена заявка на сервисе Handy." +
                "\nИмя: " + name +
                "\nДата: " + date +
                "\nВремя: " + time +
                "\nТелефон: " + phone +
                "\nАдрес: " + address;
        if (wishes != null) {
            message += "\nПожелания: " + wishes;
        }
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom(env.getProperty("support.email"));
        mailSender.send(simpleMailMessage);
        return new GenericResponse("success");
    }


    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse search(@RequestParam(value = "search") String search, Locale locale, Model model) {
        if (search.isEmpty()) {
            foundServices = null;
        } else {
            List<Service> services = serviceService.findAll();
            foundServices = new ArrayList<>();
            for (Service service : services) {
                if ((service.getAccount().getFirstName() != null && service.getAccount().getFirstName().contains(search))
                        || (service.getAccount().getLastName() != null && service.getAccount().getLastName().contains(search))
                        || service.getDescription().contains(search)) {
                    foundServices.add(service);
                }
            }
        }
        return new GenericResponse("success");
    }


    @RequestMapping(value = "/home", method = RequestMethod.POST)
    public String signUp(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                         @RequestParam("email") String email, @RequestParam("phone") String phone,
                         @RequestParam("password") String password, final HttpServletRequest request,
                         Locale locale, final Model model) {
        AccountDto accountDto = new AccountDto(firstName, lastName, password, email, phone);
        try {
            final Account registered = accountService.registerNewAccount(accountDto);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));

        } catch (DataIntegrityViolationException e) {
            System.out.println(e);
        } catch (AccountAlreadyExistException e) {
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("registrationError", messages.getMessage("message.regError", new Object[0], locale));
        }
        model.addAttribute("registrationSuccess", messages.getMessage("message.regSucc", new Object[0], locale));
        return getHome(locale, model);

    }
    // ============== NON-API ============

    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final Account account) {
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, account);
    }


    private SimpleMailMessage constructEmail(String subject, String body, Account account) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(account.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
