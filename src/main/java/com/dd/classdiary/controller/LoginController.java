package com.dd.classdiary.controller;


import com.dd.classdiary.service.dto.UserDTO;
import com.dd.classdiary.service.dto.UserExtraDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(LoginController.class);



    @RequestMapping(value={"/login"}, method = RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) String error,Model model) {

        log.info("Login Request {}",getAuthentication().isAuthenticated());


        if(isAuthenticated()){
            UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
            model.addAttribute("username",getUsername());
            if (userExtraDTO.getUserType().equalsIgnoreCase("TEACHER")) {

                return "teacher-landing";
            }
            return "/student-landing";
        }

        return "login";
    }

    private String getUsername() {
        User user = (User) getAuthentication().getPrincipal();
        return user.getUsername();
    }


    public boolean isAuthenticated(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();

    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert(authentication.isAuthenticated());
        return authentication;
    }
}
