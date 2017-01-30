package ru.bulatmukhutdinov.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.bulatmukhutdinov.security.ActiveAccountStore;
import ru.bulatmukhutdinov.service.AccountService;

import java.util.Locale;

@Controller
public class AccountController {

    @Autowired
    ActiveAccountStore activeAccountStore;

    @Autowired
    AccountService userService;

    @RequestMapping(value = "/loggedUsers", method = RequestMethod.GET)
    public String getLoggedUsers(final Locale locale, final Model model) {
        model.addAttribute("users", activeAccountStore.getAccounts());
        return "users";
    }

    @RequestMapping(value = "/loggedUsersFromSessionRegistry", method = RequestMethod.GET)
    public String getLoggedUsersFromSessionRegistry(final Locale locale, final Model model) {
        model.addAttribute("users", userService.getAccountsFromSessionRegistry());
        return "users";
    }
}
