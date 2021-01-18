package com.dd.classdiary.controller;

import com.dd.classdiary.service.dto.UserDTO;
import com.dd.classdiary.service.dto.UserExtraDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/math")
public class MathController {

    private final Logger log = LoggerFactory.getLogger(MathController.class);

    @RequestMapping("/fraction")
    public String defaultAfterLogin(Model model, HttpServletRequest request) {
        log.debug("Fraction {}");

        return "fraction";
    }



}
