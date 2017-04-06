package ru.bulatmukhutdinov.web.controller;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.bulatmukhutdinov.dto.CategoryDto;
import ru.bulatmukhutdinov.persistance.model.Account;
import ru.bulatmukhutdinov.persistance.model.Category;
import ru.bulatmukhutdinov.persistance.model.Lang;
import ru.bulatmukhutdinov.persistance.model.Service;
import ru.bulatmukhutdinov.service.*;
import ru.bulatmukhutdinov.web.util.GenericResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by neewy on 31/03/17.
 */
@Controller
public class WizardController {

    @Autowired
    private LangService langService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryTextService categoryTextService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private AccountService accountService;


    @RequestMapping(value = "/wizard", method = RequestMethod.GET)
    public String getDefault(Locale locale, final Model model) {

        List<Category> categories = categoryService.findAll();
        List<Lang> langList = langService.getLangs();
        Lang language = null;
        for (Lang lang : langList) {
            if (locale.getLanguage().equals(lang.getName())) {
                language = lang;
                break;
            }
        }
        List<CategoryDto> translatedCategories = new ArrayList<>();
        for (Category category : categories) {
            CategoryDto categoryDto = new CategoryDto(categoryTextService.findByLang(category, language).getText());
            categoryDto.setId(category.getId());
            translatedCategories.add(categoryDto);
        }
        model.addAttribute("categories", translatedCategories);

        return "contractorWizard";
    }

    @RequestMapping(value = "/wizard", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse signup(@RequestParam(value = "date", required = false) String birthDate, @RequestParam(value = "experience", required = false) String experienceYears,
                                  @RequestParam(value = "address", required = false) String location, @RequestParam(value = "about", required = false) String about,
                                  @RequestParam(value = "service", required = false) String[] services,
                                  @RequestParam(value = "categorySelect", required = false) String[] categoriesSelected,
                                  @RequestParam(value = "price", required = false) String[] prices) {

        Set<String> errors = new HashSet<>();

        LocalDate birthDateFormatted = null;
        Double experienceYearsFormatted = null;

        if (birthDate != null) {
            try {
                DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
                birthDateFormatted = df.parse(birthDate.trim()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } catch (ParseException e) {
                errors.add("Пожалуйста, укажите дату рождения в формате 30.01.1990");
            }
        }

        if (experienceYears == null) {
            errors.add("Укажите опыт вашей работы в сфере предстоставляемых услуг");
        } else {
            try {
                experienceYearsFormatted = Double.parseDouble(experienceYears.trim());
                if (experienceYearsFormatted < 0) {
                    errors.add("Пожалуйста, используйте только положительное число в описании стажа работы");
                }
            } catch (NumberFormatException e) {
                errors.add("Пожалуйста, используйте только количество лет (число, например 0.5) в описании стажа работы");
            }
        }

        if (location == null) {
            errors.add("Укажите ваше примерное местоположение (к примеру Казань или Иннополис)");
        }

        if (about == null) {
            errors.add("Расскажите о себе и своих услугах в описании на первом шаге");
        }

        if (services == null) {
            errors.add("Укажите хотя бы одну предоставялемую услугу");
        }

        if (categoriesSelected == null) {
            errors.add("Укажите категорию услуги");
        }

        if (prices == null) {
            errors.add("Укажите стоимость услуги");
        } else {
            for (String price :
                    prices) {
                try {
                    Integer priceInt = Integer.parseInt(price.trim());
                    if (priceInt < 0) {
                        errors.add("Пожалуйста, используйте только положительное число в описании cтоимости услуги");
                    }
                } catch (NumberFormatException e) {
                    errors.add("Пожалуйста, используйте только число в описании стоимости услуги (в рублях)");
                }
            }
        }

        if (errors.size() == 0) {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account account = (Account) auth.getPrincipal();


            if (experienceYearsFormatted != null) {
                account.setLengthOfService(experienceYearsFormatted);
            }

            if (birthDateFormatted != null) {
                account.setAge(Period.between(birthDateFormatted, LocalDate.now()).getYears());
            }

            account.setCity(location);
            account.setDescription(about);

            for (int i = 0; i < services.length; i++) {
                Service service = new Service();
                service.setDescription(services[i]);
                service.setCategory(categoryService.findById(Long.parseLong(categoriesSelected[i].trim())));
                service.setPrice(Integer.parseInt(prices[i].trim()));
                service.setAccount(account);
                serviceService.save(service);
            }

            accountService.saveRegisteredAccount(account);

            return new GenericResponse("success");

        } else {

            JSONArray array = new JSONArray(errors);
            return new GenericResponse(array.toString());
        }


    }
}
