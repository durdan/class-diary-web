package com.dd.classdiary.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CalanderController {

    private final Logger log = LoggerFactory.getLogger(CalanderController.class);

    @GetMapping("/calendar")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {

        log.info("calendar Request");

        model.addAttribute("name", name);
        model.addAttribute("username",getUsername());
        return "/calendar";
    }
    private String getUsername() {
        User user = (User) getAuthentication().getPrincipal();
        return user.getUsername();
    }



    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert(authentication.isAuthenticated());
        return authentication;
    }
}
