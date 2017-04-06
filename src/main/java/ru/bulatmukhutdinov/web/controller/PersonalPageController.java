package ru.bulatmukhutdinov.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import ru.bulatmukhutdinov.dto.AccountDto;
import ru.bulatmukhutdinov.persistance.model.Account;
import ru.bulatmukhutdinov.persistance.model.Photo;
import ru.bulatmukhutdinov.service.AccountService;
import ru.bulatmukhutdinov.storage.StorageService;

import java.util.Iterator;

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
    public ResponseEntity handleFileUpload(MultipartHttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) auth.getPrincipal();

        Iterator<String> itr = request.getFileNames();

        while (itr.hasNext()) {
            Photo photo = new Photo(storageService.store(request.getFile(itr.next())), account);
            account.getPhotos().add(photo);
            accountService.saveRegisteredAccount(account);
        }

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @RequestMapping(value = "/personal/userPic", method = RequestMethod.POST)
    public ResponseEntity handleUserPicUpload(MultipartHttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) auth.getPrincipal();

        Iterator<String> itr = request.getFileNames();

        while (itr.hasNext()) {
            account.setUserPicUri(storageService.store(request.getFile(itr.next())));
            accountService.saveRegisteredAccount(account);
        }

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

}
