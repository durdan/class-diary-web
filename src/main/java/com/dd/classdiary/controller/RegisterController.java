package com.dd.classdiary.controller;

import com.dd.classdiary.config.UserType;
import com.dd.classdiary.model.UserForm;
import com.dd.classdiary.service.AccountService;
import com.dd.classdiary.service.dto.UserDTO;
import com.google.gson.JsonArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Controller
public class RegisterController {

    private final Logger log = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    AccountService accountService;

    @GetMapping("/register")
    public String getRegisterForm( Model model) {
        model.addAttribute("userForm", new UserForm());
        return "register";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult, Model model, HttpServletRequest request) {

        log.info("Creating User {}" ,userForm.getUserType());
        if (bindingResult.hasErrors()) {

            model.addAttribute("userForm", userForm);
            return "register";
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setCreatedBy(userForm.getFirstName());
        userDTO.setFirstName(userForm.getFirstName());
        userDTO.setCreatedDate(Instant.now());
        userDTO.setEmail(userForm.getEmail());
        userDTO.setLastName(userForm.getLastName());
        userDTO.setLogin(userForm.getEmail());
        userDTO.setActivated(true);
        userDTO.setPassword(userForm.getPassword());
        Set<String> authorities = new HashSet<String>();
         if(userForm.getUserType().equals(UserType.STUDENT.name())){
             log.info("I am student");
             authorities.add("ROLE_STUDENT");

              authorities.add("ROLE_USER");
         }
         if(userForm.getUserType().equals(UserType.TEACHER.name())){
             log.info("I am teacher");
            authorities.add("ROLE_TEACHER");

            authorities.add("ROLE_USER");

        }
        if(userForm.getUserType().equals(UserType.PARENT.name())){
            log.info("I am parent");
            authorities.add("ROLE_PARENT");

            authorities.add("ROLE_USER");

        }





        userDTO.setAuthorities(authorities);

        accountService.createAccount(userDTO,userForm.getUserType());

        //log.info("User {} is saved {} {}",savedUser.getId(),savedUser.getFirstName());
        UserDTO siginUser= new UserDTO();
        siginUser.setLogin(userDTO.getLogin());
        siginUser.setFirstName(userDTO.getFirstName());
        siginUser.setLastName(userDTO.getLastName());

        log.info("saved user is {}",siginUser.getLogin());

        model.addAttribute("siginUser",siginUser);


        return "signin";
    }


}
