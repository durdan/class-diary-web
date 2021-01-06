package com.dd.classdiary.security;

import com.dd.classdiary.config.WebSecurityConfig;
import com.dd.classdiary.service.AccountService;
import com.dd.classdiary.service.dto.UserDTO;
import com.dd.classdiary.service.dto.UserExtraDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Autowired
    AccountService accountService;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        log.info("IsAuth: {}",authentication.isAuthenticated());
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        log.info("Auth: {} {}",name,password);
        String token=accountService.login(name,password,true);
        log.info("Token {}",token);

        if (token!=null || !token.isEmpty()) {
            UserExtraDTO userExtraDTO = accountService.getAccount(token);
            userExtraDTO.setToken(token);
            final List<GrantedAuthority> grantedAuths = new ArrayList<>();
            Set<String> authoritiesList=userExtraDTO.getUserDTO().getAuthorities();
            Iterator itr = authoritiesList.iterator();
            while(itr.hasNext())
            {
                String authoritiy = itr.next().toString();
                grantedAuths.add(new SimpleGrantedAuthority(authoritiy));
                log.info("Granted Auth {}",authoritiy);
            }

            final UserDetails principal = new User(name, password, grantedAuths);
            final Authentication auth = new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);
            ((UsernamePasswordAuthenticationToken) auth).setDetails(userExtraDTO);
            log.info("Return Auth {}",auth);
            return auth;
        } else {
            throw new BadCredentialsException("Authentication failed");
        }

//
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
