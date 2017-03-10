package ru.bulatmukhutdinov.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.bulatmukhutdinov.persistance.model.Account;

import java.util.Locale;

/**
 * Created by Reverendo on 17.02.2017.
 */

@Controller
public class HomeController {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getHome(Locale locale, final Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof Account) {
            Account account = (Account) auth.getPrincipal();
            model.addAttribute("categories", account.getCategories());
        }
        return "home";
    }

}
