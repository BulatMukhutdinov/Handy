package ru.bulatmukhutdinov.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Locale;

/**
 * Created by Reverendo on 17.02.2017.
 */

@Controller
public class HomepageController {

    @RequestMapping(value = "/homepage", method = RequestMethod.GET)
    public String getLoggedUsers(final Locale locale, final Model model) {
        return "homepage";
    }

}
