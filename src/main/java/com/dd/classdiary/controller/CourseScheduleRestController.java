package com.dd.classdiary.controller;

import com.dd.classdiary.model.ClassSchedule;
import com.dd.classdiary.model.Event;
import com.dd.classdiary.service.ClassScheduleService;
import com.dd.classdiary.service.CourseService;
import com.dd.classdiary.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/class-schedule")
public class CourseScheduleRestController {
    private final Logger log = LoggerFactory.getLogger(CourseScheduleRestController.class);

    @Autowired
    ClassScheduleService classScheduleService;

    @Autowired
    StudentService studentService;

    @Autowired
    CourseService courseService;

    @GetMapping(value = "/my-classes", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Event> getRestClassSchedules( Model model) throws URISyntaxException {

        List<ClassSchedule> classScheduleList=classScheduleService.getClassSchedules(null,null,null,null,null,null);
        log.info("List of all classes {} {}",classScheduleList.size(),classScheduleList.get(0));
      //  log.info("List of all classes",classScheduleList.size());
        List<Event>events = new ArrayList<>();
        for(int i=0;i<classScheduleList.size();i++){
            Event event = new Event();
            ClassSchedule classSchedule = classScheduleList.get(i);
            if(classSchedule.getSchedule()!=null) {
                event.setStart(classSchedule.getSchedule().toString());
                event.setEnd(classSchedule.getSchedule().toString());
                event.setTitle(classSchedule.getName());
                event.setId(classSchedule.getId().toString());
                events.add(event);
            }

        }
        model.addAttribute("classScheduleList",classScheduleList);
        return events;
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
