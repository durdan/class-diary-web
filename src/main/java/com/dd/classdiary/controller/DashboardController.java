package com.dd.classdiary.controller;


import com.dd.classdiary.model.Student;
import com.dd.classdiary.service.TeacherService;
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
public class DashboardController {

    private final Logger log = LoggerFactory.getLogger(DashboardController.class);
    @Autowired
    TeacherService teacherService;

    @GetMapping("/dashboard")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {

        log.info("dashboard Request");

        model.addAttribute("name", name);
        model.addAttribute("username",getUsername());
        return "/dashboard";
    }
    @GetMapping("/teacherDashboard")
    public String teacherDashboard(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) throws URISyntaxException {

        log.info("Teacher dashboard Request");
        List<Student> studentList=teacherService.getStudents();
        model.addAttribute("studentList",studentList);
        model.addAttribute("name", name);
        model.addAttribute("username",getUsername());
        return "/teacherDashboard";
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
