package ru.bulatmukhutdinov.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bulatmukhutdinov.dto.AccountDto;
import ru.bulatmukhutdinov.persistance.model.Account;
import ru.bulatmukhutdinov.service.AccountService;

/**
 * Created by Reverendo on 31.03.2017.
 */
@Controller
public class ContractorController {

    @Autowired
    AccountService accountService;


    @RequestMapping(value = "/contractor", method = RequestMethod.GET)
    public String getPersonal(@RequestParam("id") String id, final Model model) {
        Account account = accountService.getAccountByID(Long.parseLong(id));
        AccountDto accountDto = new AccountDto(account);
        model.addAttribute("account", accountDto);
        return "contractor";
    }

}
