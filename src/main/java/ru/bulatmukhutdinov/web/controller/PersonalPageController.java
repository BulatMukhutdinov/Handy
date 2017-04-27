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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import ru.bulatmukhutdinov.dto.AccountDto;
import ru.bulatmukhutdinov.dto.CategoryDto;
import ru.bulatmukhutdinov.persistance.model.*;
import ru.bulatmukhutdinov.service.*;
import ru.bulatmukhutdinov.storage.StorageService;
import ru.bulatmukhutdinov.web.util.GenericResponse;

import java.util.*;

/**
 * Created by Reverendo on 31.03.2017.
 */
@Controller
public class PersonalPageController {
    private final StorageService storageService;

    @Autowired
    private CategoryTextService categoryTextService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LangService langService;

    @Autowired
    AccountService accountService;

    @Autowired
    ServiceService serviceService;

    @Autowired
    public PersonalPageController(StorageService storageService) {
        this.storageService = storageService;
    }


    @RequestMapping(value = "/personal", method = RequestMethod.GET)
    public String getPersonal(final Model model, Locale locale) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) auth.getPrincipal();
        if (!account.getWizardVisited()) {
            return "redirect:/wizard";
        }
        Lang language = null;
        List<Lang> langList = langService.getLangs();
        for (Lang lang : langList) {
            if (locale.getLanguage().equals(lang.getName())) {
                language = lang;
                break;
            }
        }
        List<Category> categories = categoryService.findAll();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            categoryDtos.add(new CategoryDto(categoryTextService.findByLang(category, language)));
        }
        AccountDto accountDto = new AccountDto(account);
        model.addAttribute("account", accountDto);
        model.addAttribute("categories", categoryDtos);
        return "personalPage";
    }

    @RequestMapping(value = "/updateServices", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse updateServices(@RequestParam(value = "service", required = false) String[] services,
                                          @RequestParam(value = "categorySelect", required = false) String[] categoriesSelected,
                                          @RequestParam(value = "price", required = false) String[] prices) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) auth.getPrincipal();
        Set<Service> serviceSet = account.getServices();
        for (Service service : serviceSet) {
            serviceService.delete(service);
        }
        serviceSet = new HashSet<>();
        for (int i = 0; i < services.length; i++) {
            Service service = new Service();
            service.setAccount(account);
            service.setDescription(services[i]);
            service.setCategory(categoryService.findById(Long.valueOf(categoriesSelected[i])));
            service.setPrice(Integer.valueOf(prices[i]));
            serviceSet.add(service);
        }
        for (Service service : serviceSet) {
            serviceService.save(service);
        }
        return new GenericResponse("success");
    }

    @RequestMapping(value = "/updateAbout", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse updateAbout(@RequestParam(value = "about", required = false) String about) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) auth.getPrincipal();
        account.setDescription(about);
        accountService.saveRegisteredAccount(account);
        return new GenericResponse("success");
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
