package com.dd.classdiary.controller;

import com.dd.classdiary.model.*;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
    private final Logger log = LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    TeacherService teacherService;

    @GetMapping("/createclass")
    public String getClassForm( Model model) {
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("username",getUsername());
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        model.addAttribute("loggedinUserName",getLoggedInUser());
        model.addAttribute("userType",userExtraDTO.getUserType());
        return "addclass";
    }

    @GetMapping("/createstudent")
    public String getStudentForm( Model model) {
        if(model.getAttribute("studentForm")!=null){
            StudentForm studentForm = (StudentForm) model.getAttribute("studentForm");
            log.info("Create Student Request {}",studentForm.getEmail());
            model.addAttribute("studentForm", studentForm);
        }else{
            model.addAttribute("studentForm", new StudentForm());
        }
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        model.addAttribute("userType",userExtraDTO.getUserType());
        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());
        return "studentform";
    }

    @PostMapping("/create-student")
    public String createStudent(@Valid @ModelAttribute("studentForm") StudentForm studentForm, BindingResult bindingResult, Model model, HttpServletRequest request) throws URISyntaxException {

        log.info("Creating Student {}" ,studentForm.getEmail());

        if (bindingResult.hasErrors()) {

            model.addAttribute("studentForm", studentForm);
            return "studentform";
        }

        Student student = new Student();
        student.setCreatedBy(getUsername());
        student.setCreated(Instant.now());
        student.setEmail(studentForm.getEmail());
        student.setFirstName(studentForm.getFirstName());
        student.setLastName(studentForm.getLastName());
        student.setPhone(studentForm.getPhone());
        student.setSchoolYear(studentForm.getSchoolYear());

        Teacher teacher= new Teacher();
        teacher.setEmail(getUsername());
        student.setTeacher(teacher);
        Student student1=teacherService.createStudent(student);
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        model.addAttribute("userType",userExtraDTO.getUserType());
        model.addAttribute("student",student1);
        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());

        return "student";
    }

    @GetMapping("/students")
    public String getStudents( Model model) throws URISyntaxException {
        List<Student> studentList=teacherService.getStudents();
        model.addAttribute("studentList",studentList);
        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        model.addAttribute("userType",userExtraDTO.getUserType());
        return "studentlist";
    }

    @GetMapping(path = {"/student/{id}"})
    public String getTeacher(Model model, @PathVariable("id") Long id, HttpServletRequest request) throws URISyntaxException {
        log.info("Controller - Request to get a Student with id{}",id);
        List<Student> studentList=teacherService.getStudentById(id);

        model.addAttribute("student",studentList.get(0));
        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        model.addAttribute("userType",userExtraDTO.getUserType());
        return "student";
    }

    @GetMapping(path = {"/student-update/{id}"})
    public String updateStudent(Model model, @PathVariable("id") Long id, RedirectAttributes redirectAttrs, HttpServletRequest request) throws URISyntaxException {
        log.info("Controller - Request to update a Student with id{}",id);
        List<Student> studentList=teacherService.getStudentById(id);
        model.addAttribute("studentList",studentList);
        if(!studentList.isEmpty()){
            Student student=studentList.get(0);
            StudentForm studentForm = new StudentForm();
            studentForm.setId(studentList.get(0).getId());
            studentForm.setCreated(studentList.get(0).getCreated());
            studentForm.setCreatedBy(studentList.get(0).getCreatedBy());
            studentForm.setEmail(studentList.get(0).getEmail());
            studentForm.setFirstName(studentList.get(0).getFirstName());
            studentForm.setLastName(studentList.get(0).getLastName());
            studentForm.setPhone(studentList.get(0).getPhone());
            studentForm.setSchoolYear(studentList.get(0).getSchoolYear());
            studentForm.setTeacherId(student.getTeacher().getId());
            redirectAttrs.addFlashAttribute("studentForm",studentForm);
            model.addAttribute("studentForm",studentForm);

        }
        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        model.addAttribute("userType",userExtraDTO.getUserType());

        // return "redirect:/student/createteacher";
        return "updateStudent";
    }

    @PutMapping ("/updateStudent")
    public String updateStudent(@Valid @ModelAttribute("studentForm") StudentForm studentForm, BindingResult bindingResult, Model model, HttpServletRequest request) throws URISyntaxException {

        log.info("Updating Student {}" ,studentForm.getEmail());

        if (bindingResult.hasErrors()) {

            model.addAttribute("studentForm", studentForm);
            return "studentform";
        }

        Student student = new Student();
        student.setId(studentForm.getId());
        student.setCreatedBy(studentForm.getCreatedBy());
        student.setCreated(studentForm.getCreated());
        student.setEmail(studentForm.getEmail());
        student.setFirstName(studentForm.getFirstName());
        student.setLastName(studentForm.getLastName());
        student.setPhone(studentForm.getPhone());
        student.setSchoolYear(studentForm.getSchoolYear());
        Teacher teacher= new Teacher();

        teacher.setId(studentForm.getTeacherId());
        teacher.setEmail(getUsername());

        student.setTeacher(teacher);
        Student student1= teacherService.updateStudent(student);
        model.addAttribute("student",student1);
        model.addAttribute("username",getUsername());
        model.addAttribute("loggedinUserName",getLoggedInUser());

        return "student";
    }

    private String getLoggedInUser() {
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        UserDTO  userDTO=userExtraDTO.getUserDTO();
        return userDTO.getFirstName() + " "+ userDTO.getLastName();
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
