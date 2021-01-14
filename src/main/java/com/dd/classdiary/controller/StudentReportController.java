package com.dd.classdiary.controller;

import com.dd.classdiary.config.ReportType;
import com.dd.classdiary.model.*;
import com.dd.classdiary.service.StudentReportService;
import com.dd.classdiary.service.StudentService;
import com.dd.classdiary.service.dto.UserDTO;
import com.dd.classdiary.service.dto.UserExtraDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student/report")
public class StudentReportController {
    private final Logger log = LoggerFactory.getLogger(StudentReportController.class);

    @Autowired
    StudentReportService studentReportService;


    @GetMapping(value = "/total-classes-by-teachers/{teacherId}/{reportType}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<StudentReport> getTotalClassesGroupByTeacher( Model model,@PathVariable Optional<String> teacherId,@PathVariable Optional<String> reportType) throws URISyntaxException {
        ReportType report=null;

        if(reportType.isPresent()){
            if(reportType.get().equalsIgnoreCase(ReportType.CONFIRMED.toString())){
                report=ReportType.CONFIRMED;
            }
            if(reportType.get().equalsIgnoreCase(ReportType.CONFIRMED_NOT_PAID.toString())){
                report=ReportType.CONFIRMED_NOT_PAID;
            }
            if(reportType.get().equalsIgnoreCase(ReportType.CONFIRMED_PAID.toString())){
                report=ReportType.CONFIRMED_PAID;
            }
            if(reportType.get().equalsIgnoreCase(ReportType.UNCONFIRMED.toString())){
                report=ReportType.UNCONFIRMED;
            }
            if(reportType.get().equalsIgnoreCase(ReportType.TOTAL.toString())){
                report=ReportType.TOTAL;
            }
        }else{
            report=ReportType.TOTAL;
        }
        log.info("Report Type {}",report);
       List<StudentReport> studentReports=studentReportService.getTotalClasses(teacherId,report);
        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());
        return studentReports;
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
