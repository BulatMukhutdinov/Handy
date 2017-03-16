package ru.bulatmukhutdinov.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.bulatmukhutdinov.dto.AccountDto;
import ru.bulatmukhutdinov.dto.CategoryDto;
import ru.bulatmukhutdinov.persistance.model.Account;
import ru.bulatmukhutdinov.persistance.model.Category;
import ru.bulatmukhutdinov.persistance.model.Lang;
import ru.bulatmukhutdinov.service.CategoryService;
import ru.bulatmukhutdinov.service.CategoryTextService;
import ru.bulatmukhutdinov.service.LangService;

import java.util.*;

/**
 * Created by Reverendo on 17.02.2017.
 */

@Controller
public class HomeController {

    @Autowired
    private LangService langService;

    @Autowired
    private CategoryTextService categoryTextService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getHome(Locale locale, final Model model) {
        List<Category> categories = categoryService.findAll();
        Map<CategoryDto, List<AccountDto>> accountDtoHashMap = new HashMap<>();
        List<Lang> langList = langService.getLangs();
        Lang language = null;
        for (Lang lang : langList) {
            if (locale.getLanguage().equals(lang.getName())) {
                language = lang;
                break;
            }
        }
        Set<Account> accounts;
        AccountDto accountDto;
        List<AccountDto> accountDtos;
        for (Category category : categories) {
            accounts = category.getAccounts();
            accountDtos = new ArrayList<>();
            for (Account account : accounts) {
                accountDto = new AccountDto(account.getFirstName(), account.getLastName(), account.getEmail(),
                        account.getDescription(), account.getPrice());
                accountDtos.add(accountDto);
            }
            accountDtoHashMap.put(new CategoryDto(categoryTextService.findByLang(category, language).getText()), accountDtos);
        }
        model.addAttribute("categoryAccounts", accountDtoHashMap);
        System.out.println("!");
        return "home";
    }
}
