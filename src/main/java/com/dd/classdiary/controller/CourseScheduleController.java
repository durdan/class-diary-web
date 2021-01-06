package com.dd.classdiary.controller;

import com.dd.classdiary.config.UserType;
import com.dd.classdiary.model.*;
import com.dd.classdiary.service.ClassScheduleService;
import com.dd.classdiary.service.CourseService;
import com.dd.classdiary.service.StudentService;
import com.dd.classdiary.service.TeacherService;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("/class-schedule")
public class CourseScheduleController {
    private final Logger log = LoggerFactory.getLogger(CourseScheduleController.class);

    @Autowired
    ClassScheduleService classScheduleService;

    @Autowired
    StudentService studentService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    CourseService courseService;

    @GetMapping("/createclass")
    public String getClassForm( Model model) throws URISyntaxException {
        model.addAttribute("classScheduleForm", new ClassScheduleForm());
        model.addAttribute("username",getUsername());
        model.addAttribute("courseList",courseService.getCourses());
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        model.addAttribute("userType",userExtraDTO.getUserType());
        log.info("getting a class form {}"+userExtraDTO.getUserType());
        if(userExtraDTO.getUserType().equals(UserType.STUDENT.name())){

            List <Teacher> teacherList = studentService.getTeachers();
            model.addAttribute("teacherList",teacherList);

        }
        if(userExtraDTO.getUserType().equals(UserType.TEACHER.name())){
            List <Student> studentList = teacherService.getStudents();
            model.addAttribute("studentList",studentList);
        }
        return "classScheduleForm";
    }



    @PostMapping("/createclass")
    public String createClass(@Valid @ModelAttribute("classScheduleForm") ClassScheduleForm classScheduleForm, BindingResult bindingResult, Model model, HttpServletRequest request) throws URISyntaxException {

        log.info("Creating ClassSchdule  {}" ,classScheduleForm.getName());
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        if (bindingResult.hasErrors()) {
            log.info("Error in Creating ClassSchdule  {}" ,bindingResult.getFieldErrors().get(0));
            model.addAttribute("classScheduleForm", classScheduleForm);
            model.addAttribute("userType",userExtraDTO.getUserType());
            model.addAttribute("username",getUsername());
            return "classScheduleForm";
        }
        classScheduleService.createClass(classScheduleForm);

        if(userExtraDTO.getUserType().equals(UserType.TEACHER.name())){
            List <Student> studentList = teacherService.getStudents();
            model.addAttribute("studentList",studentList);
        }
        model.addAttribute("userType",userExtraDTO.getUserType());
        model.addAttribute("username",getUsername());
        return "classScheduleForm";
    }

    @GetMapping("/get-class-schedules")
//    public String getClassSchedules( Model model,
//                                     @RequestParam(value="filterId",required=false) Long filterId,
//                                     @RequestParam(value="payment",required=false) Boolean payment,
//                                      @RequestParam(value="confirmed",required=false) Boolean confirmed,
//                                     @RequestParam(value="rescheduled",required=false) Boolean rescheduled,
//                                     @RequestParam(value="startDate",required=false) String startDate,
//                                     @RequestParam(value="endDate",required=false) String endDate
//                                     ) throws URISyntaxException {

    public String getClassSchedukles(Model model,  SearchFilter searchFilter) throws URISyntaxException{

        Long filterId=null;
        Boolean payment=null;
        Boolean rescheduled=null;
        Boolean confirmed=null;
        String startDate=null;
        String endDate=null;


        if(searchFilter==null){
            model.addAttribute("searchFilter",new SearchFilter());
        }else{
            log.info("Search filer is {}",searchFilter);
            endDate=searchFilter.getEndDate();
            startDate=searchFilter.getStartDate();
            payment=searchFilter.isPayment();
            confirmed=searchFilter.isConfirmed();
            rescheduled=searchFilter.isRescheduled();
            filterId=searchFilter.getFilterId();
            model.addAttribute("searchFilter",searchFilter);
        }

        List<ClassSchedule> classScheduleList=classScheduleService.getClassSchedules(filterId,payment,confirmed,rescheduled,startDate,endDate);
        model.addAttribute("classScheduleList",classScheduleList);
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        if(userExtraDTO.getUserType().equals(UserType.STUDENT.name())){

            List <Teacher> teacherList = studentService.getTeachers();
            model.addAttribute("teacherList",teacherList);

        }
        if(userExtraDTO.getUserType().equals(UserType.TEACHER.name())){
            List <Student> studentList = teacherService.getStudents();
            model.addAttribute("studentList",studentList);
        }
        model.addAttribute("userType",userExtraDTO.getUserType());
        model.addAttribute("username",getUsername());
        return "classschedulelist";
    }

    @GetMapping(path = {"/{id}"})
    public String getClassScheduleById(Model model, @PathVariable("id") Long id, HttpServletRequest request) throws URISyntaxException {
        log.info("Controller - Request to get a ClassSchedule with id{}",id);
        ClassSchedule classSchedule=classScheduleService.getClassScheduleById(id);

        model.addAttribute("classschedule",classSchedule);
        model.addAttribute("username",getUsername());
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        model.addAttribute("userType",userExtraDTO.getUserType());
        return "classschedule";
    }

    @GetMapping(path = {"/update/{id}"})
    public String getUpdateClassScheduleForm(Model model, @PathVariable("id") Long id,  HttpServletRequest request) throws URISyntaxException{
        log.info("Controller - Request to update a class Schedule with id{}",id);
        ClassSchedule classSchedule=classScheduleService.getClassScheduleById(id);
        ClassScheduleForm classScheduleForm = new ClassScheduleForm();
        classScheduleForm.setId(classSchedule.getId());
        classScheduleForm.setComment(classSchedule.getComment());
        classScheduleForm.setConfirmedByStudent(classSchedule.getConfirmedByStudent());
        classScheduleForm.setCourseId(classSchedule.getCourse().getId());
        classScheduleForm.setTeacherId(classSchedule.getTeacher().getId());
        classScheduleForm.setStudentId(classSchedule.getStudent().getId());
        classScheduleForm.setConnected(classSchedule.getConnected());
        classScheduleForm.setConfirmedByTeacher(classSchedule.getConfirmedByTeacher());
        classScheduleForm.setConfirmed(classSchedule.getConfirmed());
        classScheduleForm.setRescheduled(classSchedule.getRescheduled());

        classScheduleForm.setName(classSchedule.getName());
        classScheduleForm.setPayment(classSchedule.getPayment());
        classScheduleForm.setSchedule(classSchedule.getSchedule().toString());
//        classScheduleForm.setParentId(classSchedule.getParent().getId());
        model.addAttribute("classScheduleForm",classScheduleForm);
        model.addAttribute("courseList",courseService.getCourses());
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        if(userExtraDTO.getUserType().equals(UserType.STUDENT.name())){
            List <Teacher> teacherList = studentService.getTeachers();
            model.addAttribute("teacherList",teacherList);

        }
        if(userExtraDTO.getUserType().equals(UserType.TEACHER.name())){
            List <Student> studentList = teacherService.getStudents();
            model.addAttribute("studentList",studentList);
        }
        model.addAttribute("userType",userExtraDTO.getUserType());
        return "updateclassScheduleForm";
    }

    @PutMapping("update")
    public String updateClassSchedule(@Valid @ModelAttribute("classScheduleForm") ClassScheduleForm classScheduleForm, BindingResult bindingResult, Model model, HttpServletRequest request) throws URISyntaxException {

        log.info("Controller - Request to update a class Schedule with id{}",classScheduleForm.getId());
        ClassSchedule classSchedule=classScheduleService.updateClass(classScheduleForm);
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        if(userExtraDTO.getUserType().equalsIgnoreCase("STUDENT")){
            model.addAttribute("username",getUsername());
            return "dashboard";
        }
        else
        {
            model.addAttribute("username",getUsername());
            return "teacherDashboard";
        }

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
