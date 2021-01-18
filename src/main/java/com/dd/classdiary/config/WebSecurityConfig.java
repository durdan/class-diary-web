package com.dd.classdiary.config;

import com.dd.classdiary.controller.AccountController;
import com.dd.classdiary.security.CustomAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);


    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //HTTP Basic authentication
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/register/**").permitAll()
                .antMatchers(HttpMethod.GET,"/math/**").permitAll()
                .antMatchers(HttpMethod.GET,"/getSharedInfo/**").permitAll()
                .antMatchers(HttpMethod.GET,"/privacy-policy/**").permitAll()
                .antMatchers(HttpMethod.GET,"/terms-and-conditions/**").permitAll()
                .antMatchers(HttpMethod.GET,"/").permitAll()
                .antMatchers(HttpMethod.GET,"/account/activate/**").permitAll()
                .antMatchers(HttpMethod.GET,"/account/resetform/**").permitAll()
                .antMatchers(HttpMethod.GET,"/account/reset/finish/**").permitAll()
                .antMatchers(HttpMethod.POST,"/account/passwordResetForm/**").permitAll()
                .antMatchers(HttpMethod.POST,"/account/resetform/**").permitAll()
                .antMatchers(HttpMethod.POST,"/create/**").permitAll()
                .antMatchers(HttpMethod.GET,"/hello/**").permitAll()
                .antMatchers(HttpMethod.GET,"/signin/**").permitAll()
                .antMatchers(HttpMethod.GET,"/login/**").permitAll().anyRequest()
//                .antMatchers(HttpMethod.GET, "/register/**").hasRole("USER")
//                .antMatchers(HttpMethod.POST, "/books").hasRole("ADMIN")
//                .antMatchers(HttpMethod.PUT, "/books/**").hasRole("ADMIN")
//                .antMatchers(HttpMethod.PATCH, "/books/**").hasRole("ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/books/**").hasRole("ADMIN")
                .authenticated()
                    .and()
                    .csrf().disable()
                .formLogin()
                    .loginPage("/login").failureUrl("/login?error=true")
                    .defaultSuccessUrl("/default")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/").and().exceptionHandling()
                .accessDeniedPage("/access-denied");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("I am inside AuthProvider");
        auth.eraseCredentials(false);
        auth.authenticationProvider(customAuthenticationProvider);

    }

    public void configure(WebSecurity web) throws Exception{
        web.ignoring()
                .antMatchers("/resources/**","/static/**","/css/**","/js/**","/img/**","/assets/**");
    }
}
