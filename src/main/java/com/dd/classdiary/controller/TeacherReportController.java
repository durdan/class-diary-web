package com.dd.classdiary.controller;

import com.dd.classdiary.config.ReportType;
import com.dd.classdiary.model.StudentReport;
import com.dd.classdiary.model.TeacherReport;
import com.dd.classdiary.service.StudentReportService;
import com.dd.classdiary.service.TeacherReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teacher/report")
public class TeacherReportController {
    private final Logger log = LoggerFactory.getLogger(TeacherReportController.class);

    @Autowired
    TeacherReportService teacherReportService;


    @GetMapping(value = "/total-classes-by-students/{studentId}/{reportType}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<TeacherReport> getTotalClassesGroupByStudent( Model model,@PathVariable Optional<String> studentId,@PathVariable Optional<String> reportType) throws URISyntaxException {
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
       List<TeacherReport> teacherReports=teacherReportService.getTotalClasses(studentId,report);
        model.addAttribute("username",getUsername());
        return teacherReports;
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
