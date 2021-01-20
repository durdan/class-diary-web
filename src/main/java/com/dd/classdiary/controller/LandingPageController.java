package com.dd.classdiary.controller;


import com.dd.classdiary.model.Student;
import com.dd.classdiary.service.TeacherService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URISyntaxException;
import java.util.List;

@Controller
public class LandingPageController {

    private final Logger log = LoggerFactory.getLogger(LandingPageController.class);
    @Autowired
    TeacherService teacherService;

    @GetMapping("/student-landing")
    public String studentLandingPage(Model model) {

        log.info("student-landing-page");

        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());
        return "/student-landing";
    }
    @GetMapping("/teacher-landing")
    public String teacherDashboard( Model model) throws URISyntaxException {

        log.info("Teacher-landing-page");

        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());
        return "/teacher-landing";
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
