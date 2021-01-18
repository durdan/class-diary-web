package com.dd.classdiary.controller;

import com.dd.classdiary.config.UserType;
import com.dd.classdiary.model.*;
import com.dd.classdiary.service.AccountService;
import com.dd.classdiary.service.SharedInfoService;
import com.dd.classdiary.service.StudentService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class SharedInfoController {

    private final Logger log = LoggerFactory.getLogger(SharedInfoController.class);

    @Autowired
    TeacherService teacherService;

    @Autowired
    StudentService studentService;

    @Autowired
    SharedInfoService sharedInfoService;


    @PostMapping("/shared")
    public String createRequestTask(@Valid @ModelAttribute("sharedRequestForm") SharedRequestForm sharedRequestForm, BindingResult bindingResult, Model model, HttpServletRequest request) throws URISyntaxException {

        log.info("Shared User {}" ,sharedRequestForm.getSharedUserId());
        RequestTask requestTask=sharedInfoService.sharedInfo(sharedRequestForm.getSharedUserId());
        model.addAttribute("sharedRequestForm",sharedRequestForm);
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        model.addAttribute("shared","true");
        model.addAttribute("userType",userExtraDTO.getUserType());

        model.addAttribute("loggedinUserName",getLoggedInUser());

        if(userExtraDTO.getUserType().equals(UserType.STUDENT.name())){

            List<Teacher> teacherList = studentService.getTeachers();
            model.addAttribute("teacherList",teacherList);

        }
        if(userExtraDTO.getUserType().equals(UserType.TEACHER.name())){
            List <Student> studentList = teacherService.getStudents();
            model.addAttribute("studentList",studentList);
        }
        model.addAttribute("sharedKey",requestTask.getRequestCode());

        return "shared";
    }

    @GetMapping("/shared")
    public String getSharedForm( Model model) throws URISyntaxException {
        model.addAttribute("sharedRequestForm", new SharedRequestForm());
        model.addAttribute("username",getUsername());
        model.addAttribute("shared","false");

        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        model.addAttribute("userType",userExtraDTO.getUserType());

        model.addAttribute("loggedinUserName",getLoggedInUser());
        log.info("getting a class form {}"+userExtraDTO.getUserType());
        if(userExtraDTO.getUserType().equals(UserType.STUDENT.name())){

            List<Teacher> teacherList = studentService.getTeachers();
            model.addAttribute("teacherList",teacherList);

        }
        if(userExtraDTO.getUserType().equals(UserType.TEACHER.name())){
            List <Student> studentList = teacherService.getStudents();
            model.addAttribute("studentList",studentList);
        }
        return "shared";
    }

    @GetMapping(path = {"/getSharedInfo/{key}"})
    public String getClassScheduleById(Model model, @PathVariable("key") String key, HttpServletRequest request) throws URISyntaxException {
        log.info("Controller - Request to get a Shared ClassSchedule with key{}",key);
        List<ClassSchedule> classScheduleList=sharedInfoService.getSharedClassSchedule(key);

        model.addAttribute("classScheduleList",classScheduleList);
        model.addAttribute("loggedinUserName","Guest User");
        return "sharedClassschedulelist";
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
