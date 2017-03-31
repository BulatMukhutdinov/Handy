package ru.bulatmukhutdinov.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.bulatmukhutdinov.dto.AccountDto;
import ru.bulatmukhutdinov.persistance.model.Account;
import ru.bulatmukhutdinov.storage.StorageService;

/**
 * Created by Reverendo on 31.03.2017.
 */
@Controller
public class PersonalPageController {
    private final StorageService storageService;

    @Autowired
    public PersonalPageController(StorageService storageService) {
        this.storageService = storageService;
    }


    @RequestMapping(value = "/personal", method = RequestMethod.GET)
    public String getContractor(final Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) auth.getPrincipal();
        AccountDto accountDto = new AccountDto(account);
        model.addAttribute("account", accountDto);
        return "personalPage";
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }
}
