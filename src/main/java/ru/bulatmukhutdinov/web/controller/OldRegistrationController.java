package ru.bulatmukhutdinov.web.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.bulatmukhutdinov.dto.AccountDto;
import ru.bulatmukhutdinov.persistance.model.Account;
import ru.bulatmukhutdinov.persistance.model.PasswordResetToken;
import ru.bulatmukhutdinov.persistance.model.VerificationToken;
import ru.bulatmukhutdinov.registration.OnRegistrationCompleteEvent;
import ru.bulatmukhutdinov.service.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

@Controller
@RequestMapping(value = "/old")
public class OldRegistrationController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private Environment env;

    public OldRegistrationController() {
        super();
    }

    // API

    @RequestMapping(value = "/user/registration", method = RequestMethod.GET)
    public String showRegistrationForm(final HttpServletRequest request, final Model model) {
        LOGGER.debug("Rendering registration page.");
        final AccountDto accountDto = new AccountDto();
        model.addAttribute("user", accountDto);
        return "registration";
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(final HttpServletRequest request, final Model model, @RequestParam("token") final String token) {
        final Locale locale = request.getLocale();

        final VerificationToken verificationToken = accountService.getVerificationToken(token);
        if (verificationToken == null) {
            final String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }

        final Account account = verificationToken.getAccount();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("message", messages.getMessage("auth.message.expired", null, locale));
            model.addAttribute("expired", true);
            model.addAttribute("token", token);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }

        account.setEnabled(true);
        accountService.saveRegisteredAccount(account);
        model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
        return "redirect:/login.html?lang=" + locale.getLanguage();
    }

    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid final AccountDto accountDto, final HttpServletRequest request, final Errors errors) {
        LOGGER.debug("Registering user account with information: {}", accountDto);

        final Account registered = accountService.registerNewAccount(accountDto);
        if (registered == null) {
            // result.rejectValue("email", "message.regError");
            return new ModelAndView("registration", "user", accountDto);
        }
        try {
            final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
        } catch (final Exception ex) {
            LOGGER.warn("Unable to register user", ex);
            return new ModelAndView("emailError", "user", accountDto);
        }
        return new ModelAndView("successRegister", "user", accountDto);
    }

    @RequestMapping(value = "/user/resendRegistrationToken", method = RequestMethod.GET)
    public String resendRegistrationToken(final HttpServletRequest request, final Model model, @RequestParam("token") final String existingToken) {
        final Locale locale = request.getLocale();
        final VerificationToken newToken = accountService.generateNewVerificationToken(existingToken);
        final Account account = accountService.getAccount(newToken.getToken());
        try {
            final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            final SimpleMailMessage email = constructResetVerificationTokenEmail(appUrl, request.getLocale(), newToken, account);
            mailSender.send(email);
        } catch (final MailAuthenticationException e) {
            LOGGER.debug("MailAuthenticationException", e);
            return "redirect:/emailError.html?lang=" + locale.getLanguage();
        } catch (final Exception e) {
            LOGGER.debug(e.getLocalizedMessage(), e);
            model.addAttribute("message", e.getLocalizedMessage());
            return "redirect:/login.html?lang=" + locale.getLanguage();
        }
        model.addAttribute("message", messages.getMessage("message.resendToken", null, locale));
        return "redirect:/login.html?lang=" + locale.getLanguage();
    }

    @RequestMapping(value = "/user/resetPassword", method = RequestMethod.POST)
    public String resetPassword(final HttpServletRequest request, final Model model, @RequestParam("email") final String userEmail) {
        final Account account = accountService.findAccountByEmail(userEmail);
        if (account == null) {
            model.addAttribute("message", messages.getMessage("message.userNotFound", null, request.getLocale()));
            return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
        }

        final String token = UUID.randomUUID().toString();
        accountService.createPasswordResetTokenForAccount(account, token);
        try {
            final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            final SimpleMailMessage email = constructResetTokenEmail(appUrl, request.getLocale(), token, account);
            mailSender.send(email);
        } catch (final MailAuthenticationException e) {
            LOGGER.debug("MailAuthenticationException", e);
            return "redirect:/emailError.html?lang=" + request.getLocale().getLanguage();
        } catch (final Exception e) {
            LOGGER.debug(e.getLocalizedMessage(), e);
            model.addAttribute("message", e.getLocalizedMessage());
            return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
        }
        model.addAttribute("message", messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
        return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
    }

    @RequestMapping(value = "/user/changePassword", method = RequestMethod.GET)
    public String changePassword(final HttpServletRequest request, final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
        final Locale locale = request.getLocale();

        final PasswordResetToken passToken = accountService.getPasswordResetToken(token);
        final Account account = passToken.getAccount();
        if ((passToken == null) || (account.getId() != id)) {
            final String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "redirect:/login.html?lang=" + locale.getLanguage();
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("message", messages.getMessage("auth.message.expired", null, locale));
            return "redirect:/login.html?lang=" + locale.getLanguage();
        }

        final Authentication auth = new UsernamePasswordAuthenticationToken(account, null, userDetailsService.loadUserByUsername(account.getEmail()).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
    }

    @RequestMapping(value = "/user/savePassword", method = RequestMethod.POST)
    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    public String savePassword(final HttpServletRequest request, final Model model, @RequestParam("password") final String password) {
        final Locale locale = request.getLocale();

        final Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountService.changeAccountPassword(account, password);
        model.addAttribute("message", messages.getMessage("message.resetPasswordSuc", null, locale));
        return "redirect:/login.html?lang=" + locale;
    }

    // NON-API

    private final SimpleMailMessage constructResetVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final Account account) {
        final String confirmationUrl = contextPath + "/old/registrationConfirm.html?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Resend Registration Token");
        email.setText(message + " \r\n" + confirmationUrl);
        email.setTo(account.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private final SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final Account account) {
        final String url = contextPath + "/old/user/changePassword?id=" + account.getId() + "&token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(account.getEmail());
        email.setSubject("Reset Password");
        email.setText(message + " \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
