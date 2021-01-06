package com.dd.classdiary.controller;

import com.dd.classdiary.model.Teacher;
import com.dd.classdiary.model.UserProfile;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class AccountController {

    private final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    AccountService accountService;

    @GetMapping
    public String getUserProfile(Model model, HttpServletRequest request) throws URISyntaxException {
        log.info("Controller - Request to get a user Profile");

        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();

        UserDTO userDTO=accountService.getAccountProfile(userExtraDTO.getToken());
        model.addAttribute("userExtraDTO",userExtraDTO);
        model.addAttribute("userDTO",userDTO);
        model.addAttribute("userType",userExtraDTO.getUserType());
        model.addAttribute("username",getUsername());
        return "userprofile";
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
