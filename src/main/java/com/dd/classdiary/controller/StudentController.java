package com.dd.classdiary.controller;

import com.dd.classdiary.model.*;
import com.dd.classdiary.service.StudentService;
import com.dd.classdiary.service.dto.TeacherDTO;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentController {
    private final Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    StudentService studentService;

    @GetMapping("/createclass")
    public String getClassForm( Model model) {
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());
        return "addclass";
    }

    @GetMapping("/createteacher")
    public String getTeacherForm( Model model) {
        if(model.getAttribute("teacherForm")!=null){
            TeacherForm teacherForm = (TeacherForm) model.getAttribute("teacherForm");
            log.info("Update Request {}",teacherForm.getEmail());
            model.addAttribute("teacherForm", teacherForm);
        }else{
            model.addAttribute("teacherForm", new TeacherForm());
        }

        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());
        return "teacherForm";
    }

    @PostMapping("/createTeacher")
    public String createTeacher(@Valid @ModelAttribute("teacherForm") TeacherForm teacherForm, BindingResult bindingResult, Model model, HttpServletRequest request) throws URISyntaxException {

        log.info("Creating Teacher {}" ,teacherForm.getEmail());
        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());

        if (bindingResult.hasErrors()) {

            model.addAttribute("teacherForm", teacherForm);
            return "teacherForm";
        }

        Teacher teacher = new Teacher();
        teacher.setCreatedBy(getUsername());
        teacher.setCreated(Instant.now());
        teacher.setEmail(teacherForm.getEmail());
        teacher.setFirstName(teacherForm.getFirstName());
        teacher.setLastName(teacherForm.getLastName());
        teacher.setPhone(teacherForm.getPhone());
        teacher.setProfileContent(teacherForm.getProfileContent());
        Student student= new Student();
        student.setEmail(getUsername());
        teacher.setStudent(student);
        Teacher teacher1=studentService.createTeacher(teacher);

        model.addAttribute("teacher",teacher1);
        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());

        return "teacher";
    }

    @PutMapping ("/updateTeacher")
    public String updateTeacher(@Valid @ModelAttribute("teacherForm") TeacherForm teacherForm, BindingResult bindingResult, Model model, HttpServletRequest request) throws URISyntaxException {

        log.info("Updating Teacher {}" ,teacherForm.getEmail());
        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());

        if (bindingResult.hasErrors()) {

            model.addAttribute("teacherForm", teacherForm);
            return "teacherForm";
        }

        Teacher teacher = new Teacher();
        teacher.setId(teacherForm.getId());
        teacher.setCreatedBy(getUsername());
        teacher.setCreated(Instant.now());
        teacher.setEmail(teacherForm.getEmail());
        teacher.setFirstName(teacherForm.getFirstName());
        teacher.setLastName(teacherForm.getLastName());
        teacher.setPhone(teacherForm.getPhone());
        teacher.setProfileContent(teacherForm.getProfileContent());
        Student student= new Student();
        student.setId(teacherForm.getStudentId());
        student.setEmail(getUsername());

        teacher.setStudent(student);
        Teacher teacher1= studentService.updateTeacher(teacher);
        model.addAttribute("teacher",teacher1);

        return "teacher";
    }


    @GetMapping("/teachers")
    public String getTeachers( Model model) throws URISyntaxException {
        List<Teacher> teacherList=studentService.getTeachers();
        model.addAttribute("teacherList",teacherList);
        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());
        return "teacherlist";
    }

    @GetMapping(path = {"/teacher/{id}"})
    public String getTeacher(Model model, @PathVariable("id") Long id, HttpServletRequest request) throws URISyntaxException {
        log.info("Controller - Request to get a Teacher with id{}",id);
        List<Teacher> teacherList=studentService.getTeacherById(id);

        model.addAttribute("teacher",teacherList.get(0));
        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());
        return "teacher";
    }

    @GetMapping(path = {"/teacher-update/{id}"})
    public String updateTeacher(Model model, @PathVariable("id") Long id, RedirectAttributes redirectAttrs, HttpServletRequest request) throws URISyntaxException {
        log.info("Controller - Request to update a Teacher with id{}",id);
        List<Teacher> teacherList=studentService.getTeacherById(id);
        model.addAttribute("teacherList",teacherList);
        if(!teacherList.isEmpty()){
            Teacher teacher=teacherList.get(0);
            TeacherForm teacherForm = new TeacherForm();
            teacherForm.setId(teacherList.get(0).getId());
            teacherForm.setCreated(teacherList.get(0).getCreated());
            teacherForm.setCreatedBy(teacherList.get(0).getCreatedBy());
            teacherForm.setEmail(teacherList.get(0).getEmail());
            teacherForm.setFirstName(teacherList.get(0).getFirstName());
            teacherForm.setLastName(teacherList.get(0).getLastName());
            teacherForm.setPhone(teacher.getPhone());
            teacherForm.setProfileContent(teacher.getProfileContent());
            teacherForm.setStudentId(teacher.getStudent().getId());
            redirectAttrs.addFlashAttribute("teacherForm",teacherForm);
            model.addAttribute("teacherForm",teacherForm);

        }
        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());

       // return "redirect:/student/createteacher";
        return "updateTeacher";
    }



    @PostMapping("/createclass")
    public String createClass(@Valid @ModelAttribute("classScheduleForm") ClassScheduleForm classScheduleForm, BindingResult bindingResult, Model model, HttpServletRequest request) throws URISyntaxException {

        log.info("Creating ClassSchdule for {}" );
        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());
        if (bindingResult.hasErrors()) {

            model.addAttribute("classScheduleForm", classScheduleForm);
            return "addclass";
        }




        return "/addclass";
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
