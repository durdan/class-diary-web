package com.dd.classdiary.service;


import com.dd.classdiary.model.UserExtra;
import com.dd.classdiary.service.dto.UserDTO;
import com.dd.classdiary.service.dto.UserExtraDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerErrorException;

import java.util.*;

@Service
public class AccountService {

    private final Logger log = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.url}")
    private String restUrl;

    public String login(String userName,String password,boolean keepAlive){

        log.info("Login Request {} {}",userName,password);

// create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

// request body parameters
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("username", userName);
        requestMap.put("password", password);
        requestMap.put("rememberMe", keepAlive);

// build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

// send POST request
        log.info("Login Request");
        ResponseEntity<JWTToken> response = restTemplate.postForEntity(restUrl+"/authenticate", entity, JWTToken.class);
        log.info("Response from login request {}",response.getBody().idToken);

        return response.getBody().idToken;

    }

    public UserExtraDTO getAccount(String token){


        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);

// request body parameters



// build the request
        HttpEntity entity = new HttpEntity<>(headers);

// send POST request
        log.info("Login Request");
        ResponseEntity<UserExtraDTO> response = restTemplate.exchange(restUrl+"/accountextra", HttpMethod.GET, entity, UserExtraDTO.class);

        log.info("Response from login request {}",response);
        return response.getBody();

    }

    public UserDTO createAccount(UserDTO userDTO,String userType) {

        log.info("Creating Account {}",userType);
// create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

// request body parameters
        Map<String, Object> requestMap = new HashMap<>();


        requestMap.put("activated", true);
        requestMap.put("createdBy", userDTO.getCreatedBy());
        requestMap.put("createdDate", userDTO.getCreatedDate());
        requestMap.put("email",userDTO.getEmail());
        requestMap.put("lastName",userDTO.getLastName());
        requestMap.put("firstName",userDTO.getFirstName());
        requestMap.put("login",userDTO.getEmail());
        requestMap.put("password",userDTO.getPassword());
        requestMap.put("authorities",userDTO.getAuthorities());
        requestMap.put("userType",userType);
       // requestMap.put("profileContent",userDTO.);



// build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

// send POST request
        log.info("Login Request");
        ResponseEntity<UserDTO> response = restTemplate.postForEntity(restUrl+"/register", entity, UserDTO.class);
        if(response.getStatusCode()!=HttpStatus.CREATED){

        }

        log.info("Response from login request {}",response.getBody());
        return response.getBody();

    }

    public UserDTO getAccountProfile(String token){


        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);

// request body parameters



// build the request
        HttpEntity entity = new HttpEntity<>(headers);

// send POST request
        log.info("Login Request");
        ResponseEntity<UserDTO> response = restTemplate.exchange(restUrl+"/account", HttpMethod.GET, entity, UserDTO.class);

        log.info("Response from login request {}",response);
        return response.getBody();

    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        public JWTToken(){

        }

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }

        @Override
        public String toString() {
            return "JWTToken{" +
                    "idToken='" + idToken + '\'' +
                    '}';
        }
    }


}
