package com.dd.classdiary.controller;

import com.dd.classdiary.service.dto.UserExtraDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DefaultController {
    @RequestMapping("/default")
    public String defaultAfterLogin(Model model, HttpServletRequest request) {
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        model.addAttribute("username",getUsername());
        if (userExtraDTO.getUserType().equalsIgnoreCase("TEACHER")) {

            return "redirect:teacherDashboard";
        }
        return "redirect:dashboard";
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
