package com.dd.classdiary.controller;

import com.dd.classdiary.model.KeyAndPassword;
import com.dd.classdiary.service.AccountService;
import com.dd.classdiary.service.dto.UserDTO;
import com.dd.classdiary.service.dto.UserExtraDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;

@Controller
public class TermsConditions {

    private final Logger log = LoggerFactory.getLogger(TermsConditions.class);



    @GetMapping("/privacy-policy")
    public String getPrivacyPolicy(Model model, HttpServletRequest request) throws URISyntaxException {
        log.info("Controller - Request to get a privacy policy");


        return "privacy-policy";
    }
    @GetMapping("/terms-and-conditions")
    public String getTC(Model model, HttpServletRequest request) throws URISyntaxException {
        log.info("Controller - Request to get a Terms and Conditions");


        return "termsandconditions";
    }

}
