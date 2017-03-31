package ru.bulatmukhutdinov.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Reverendo on 31.03.2017.
 */
@Controller
public class ContractorController {
    @RequestMapping(value = "/contractor", method = RequestMethod.GET)
    public String getDefault() {
        return "contractor";
    }
    @RequestMapping(value = "/contractor-a", method = RequestMethod.GET)
    public String get() {
        return "contractor_edit";
    }
}
