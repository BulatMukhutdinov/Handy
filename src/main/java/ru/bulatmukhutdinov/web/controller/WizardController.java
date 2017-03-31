package ru.bulatmukhutdinov.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.bulatmukhutdinov.dto.CategoryDto;
import ru.bulatmukhutdinov.persistance.model.Category;
import ru.bulatmukhutdinov.persistance.model.Lang;
import ru.bulatmukhutdinov.service.CategoryService;
import ru.bulatmukhutdinov.service.CategoryTextService;
import ru.bulatmukhutdinov.service.LangService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
            translatedCategories.add(new CategoryDto(categoryTextService.findByLang(category, language).getText()));
        }
        model.addAttribute("categories", translatedCategories);

        return "contractorWizard";
    }
}
