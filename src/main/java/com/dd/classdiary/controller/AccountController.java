package com.dd.classdiary.controller;

import com.dd.classdiary.model.*;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@Controller
public class AccountController {

    private final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    AccountService accountService;

    @GetMapping("/user")
    public String getUserProfile(Model model, HttpServletRequest request) throws URISyntaxException {
        log.info("Controller - Request to get a user Profile");

        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();

        UserDTO userDTO=accountService.getAccountProfile(userExtraDTO.getToken());
        model.addAttribute("userExtraDTO",userExtraDTO);
        model.addAttribute("userDTO",userDTO);
        model.addAttribute("userType",userExtraDTO.getUserType());
        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());
        return "userprofile";
    }

    @GetMapping("/account/activate")
    public String activate(@RequestParam String key, Model model) throws URISyntaxException {
        log.info("Controller - Request to activate");

//        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();

        String result=accountService.Activate(key);

        if(result.equalsIgnoreCase("SUCCESS")){
           model.addAttribute("message","ACTIVATED");
        }
        else
        {
            model.addAttribute("message","FAILED");
        }

        return "activate";
    }

    @GetMapping("/account/resetform")
    public String getResetToEnterKeyForm( Model model) throws URISyntaxException {
        log.info("Controller - Request to rest password");
        return "password-reset";
    }

    @GetMapping("/account/reset/finish")
    public String getPasswordResetForm(@RequestParam String key, Model model) throws URISyntaxException {
        log.info("Controller Finish - Request to rest password ");
        KeyAndPassword keyAndPassword = new KeyAndPassword();
        keyAndPassword.setKey(key);

        model.addAttribute("keyAndPassword",keyAndPassword);
        return "password-reset-f";
    }

    @PostMapping("/account/passwordResetForm")
    public String resetNewPassword(@Valid @ModelAttribute("keyAndPassword") KeyAndPassword keyAndPassword, BindingResult bindingResult, Model model, HttpServletRequest request) throws URISyntaxException {
        log.info("Post  - Request to rest password ");
         String result=accountService.resetPassword(keyAndPassword);
        if(result.equalsIgnoreCase("SUCCESS")){
            model.addAttribute("message","Your password has been changed successfully.");
        }
        else
        {
            model.addAttribute("message","Error in resetting your password. Please try again");
        }
        return "activate";
    }


    @PostMapping("/account/resetform")
    public String passwordReset(@Valid @ModelAttribute("email") String email,Model model){
        log.info("Password Rest requested for email {}",email);

        log.info("Controller - Request to activate");

//        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();

        String result=accountService.resetPassword(email);

        if(result.equalsIgnoreCase("SUCCESS")){
            model.addAttribute("message","Your submissions has been received. If we have an account matching your email address, you will receive an email with a link to reset your password.");
        }
        else
        {
            model.addAttribute("message","Your submissions has been received. If we have an account matching your email address, you will receive an email with a link to reset your password.");
        }

        return "activate";

    }

    private String getUsername() {
        User user = (User) getAuthentication().getPrincipal();
        return user.getUsername();
    }

    private String getLoggedInUser() {
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        UserDTO  userDTO=userExtraDTO.getUserDTO();
        return userDTO.getFirstName() + " "+ userDTO.getLastName();
    }



    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert(authentication.isAuthenticated());
        return authentication;
    }
}
