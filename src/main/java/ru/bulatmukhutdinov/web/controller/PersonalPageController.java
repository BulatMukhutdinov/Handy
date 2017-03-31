package ru.bulatmukhutdinov.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.bulatmukhutdinov.dto.AccountDto;
import ru.bulatmukhutdinov.persistance.model.Account;
import ru.bulatmukhutdinov.persistance.model.Photo;
import ru.bulatmukhutdinov.service.AccountService;
import ru.bulatmukhutdinov.service.PhotoService;
import ru.bulatmukhutdinov.storage.StorageService;

import java.util.Set;

/**
 * Created by Reverendo on 31.03.2017.
 */
@Controller
public class PersonalPageController {
    private final StorageService storageService;

    @Autowired
    AccountService accountService;

    @Autowired
    public PersonalPageController(StorageService storageService) {
        this.storageService = storageService;
    }


    @RequestMapping(value = "/personal", method = RequestMethod.GET)
    public String getPersonal(final Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) auth.getPrincipal();
        AccountDto accountDto = new AccountDto(account);
        model.addAttribute("account", accountDto);
        return "personalPage";
    }

    @RequestMapping(value = "/personal", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   final Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) auth.getPrincipal();
        Photo photo = new Photo(storageService.store(file), account);
        account.getPhotos().add(photo);
        accountService.saveRegisteredAccount(account);
        return getPersonal(model);
    }

}
