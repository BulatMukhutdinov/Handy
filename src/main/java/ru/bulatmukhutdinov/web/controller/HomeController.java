package ru.bulatmukhutdinov.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.bulatmukhutdinov.persistance.model.Account;
import ru.bulatmukhutdinov.persistance.model.CategoryText;
import ru.bulatmukhutdinov.persistance.model.Lang;
import ru.bulatmukhutdinov.service.CategoryTextService;
import ru.bulatmukhutdinov.service.LangService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Reverendo on 17.02.2017.
 */

@Controller
public class HomeController {

    @Autowired
    private LangService langService;

    @Autowired
    private CategoryTextService categoryTextService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getHome(Locale locale, final Model model) {
        List<Lang> langList = langService.getLangs();
        List<CategoryText> texts;
        for (Lang lang : langList) {
            if (locale.getLanguage().equals(lang.getName())) {
                texts = categoryTextService.findCategoryTextByLang(lang);
                model.addAttribute("categories", texts);
                break;
            }
        }
        return "home";
    }

}
