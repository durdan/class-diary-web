package com.dd.classdiary.service;

import com.dd.classdiary.model.Student;
import com.dd.classdiary.model.Teacher;
import com.dd.classdiary.model.TeacherList;
import com.dd.classdiary.service.dto.TeacherDTO;
import com.dd.classdiary.service.dto.UserDTO;
import com.dd.classdiary.service.dto.UserExtraDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class StudentService {

    private final Logger log = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AccountService accountService;

    @Value("${backend.url}")
    private String restUrl;
    // http://localhost:8080/api/teachers?studentId.equals=1202

    public Teacher createTeacher(Teacher teacher) throws URISyntaxException {
        log.info("REST request to Create Teacher : {}", teacher);
        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

// request body parameters
        Map<String, Object> requestMap = new HashMap<>();


        requestMap.put("firstName", teacher.getFirstName());
        requestMap.put("lastName", teacher.getLastName());
        requestMap.put("email", teacher.getEmail());
        requestMap.put("phone", teacher.getPhone());
        requestMap.put("createdBy", teacher.getCreatedBy());
        requestMap.put("createdDate", teacher.getCreated());
        requestMap.put("profileContent", teacher.getProfileContent());
        Student student = teacher.getStudent();


        // String token=accountService.login(userDetails.getUsername(),userDetails.getPassword(),true);


        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        student.setEmail(userExtraDTO.getUserDTO().getEmail());
        requestMap.put("student", student);
        log.info("Details {}", getAuthentication().getDetails());

//        byte[] encodedAuth = Base64.encodeBase64(
//                auth.getBytes(Charset.forName("US-ASCII")) );
//        String authHeader = "Basic " + new String( encodedAuth );
//        set( "Authorization", authHeader );
        headers.set("Authorization", "Bearer " + userExtraDTO.getToken());

// build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

// send POST request

        ResponseEntity<Teacher> response = restTemplate.postForEntity(restUrl + "/teachers", entity, Teacher.class);

        if (response.getStatusCode() != HttpStatus.CREATED) {

        }
        log.info("Response from Create Teacher request {}", response.getBody());
        return response.getBody();
    }

    public Teacher updateTeacher(Teacher teacher) throws URISyntaxException {
        log.info("REST request to update a Teacher : {}", teacher);
        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

// request body parameters
        Map<String, Object> requestMap = new HashMap<>();


        requestMap.put("firstName", teacher.getFirstName());
        requestMap.put("id", teacher.getId());
        requestMap.put("lastName", teacher.getLastName());
        requestMap.put("email", teacher.getEmail());
        requestMap.put("phone", teacher.getPhone());

        requestMap.put("profileContent", teacher.getProfileContent());
        Student student = teacher.getStudent();


        // String token=accountService.login(userDetails.getUsername(),userDetails.getPassword(),true);

        log.info("student {}",student);
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        student.setEmail(userExtraDTO.getUserDTO().getEmail());
        student.setId(userExtraDTO.getUserId());
        requestMap.put("student", student);
        requestMap.put("createdBy", teacher.getCreatedBy());
        requestMap.put("createdDate", teacher.getCreated());
        requestMap.put("updatedBy", userExtraDTO.getUserDTO().getEmail());
        requestMap.put("update", Instant.now());
       // log.info("Details {}", getAuthentication().getDetails());

//        byte[] encodedAuth = Base64.encodeBase64(
//                auth.getBytes(Charset.forName("US-ASCII")) );
//        String authHeader = "Basic " + new String( encodedAuth );
//        set( "Authorization", authHeader );
        headers.set("Authorization", "Bearer " + userExtraDTO.getToken());

// build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

// send POST request

        ResponseEntity<Teacher> response = restTemplate.exchange(restUrl + "/teachers",HttpMethod.PUT, entity, Teacher.class);


        log.info("Response from update Teacher request {}", response.getBody());
        return response.getBody();
    }

    public List<Teacher> getTeachers() throws URISyntaxException {


        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        // request body parameters
        Map<String, String> params = new HashMap<>();

        log.info("REST request to get Teacherrs for student ", userExtraDTO.getUserId());

        params.put("studentId.equals", userExtraDTO.getUserId().toString());

        log.info("Details {}", getAuthentication().getDetails());
        params.put("size.equals","500");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(restUrl + "/students/teachers");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        headers.set("Authorization", "Bearer " + userExtraDTO.getToken());

// build the request
        HttpEntity entity = new HttpEntity<>(headers);


// send POST request

        ResponseEntity<List> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, List.class, params);

        if (response.getStatusCode() != HttpStatus.CREATED) {

        }
        log.info("Response from My Teachers request {}", response.getBody().size());
        return response.getBody();
    }

    public List<Teacher> getTeacherById(Long id){

        log.info("REST request to get a Teacher with Id: {}", id);
       // http://localhost:8080/api/teachers?id.equals=11&studentId.equals=11
        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        // request body parameters
        Map<String, String> params = new HashMap<>();

        log.info("REST request to get Teachers for student{} ", userExtraDTO.getUserId());

        params.put("studentId.equals", userExtraDTO.getUserId().toString());
        params.put("Id.equals", id.toString());


        log.info("Details {}", getAuthentication().getDetails());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(restUrl + "/teachers");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        headers.set("Authorization", "Bearer " + userExtraDTO.getToken());

// build the request
        HttpEntity entity = new HttpEntity<>(headers);


// send POST request

        ResponseEntity<TeacherList> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, TeacherList.class, params);

        if (response.getStatusCode() != HttpStatus.CREATED) {

        }
        //ObjectMapper mapper = new ObjectMapper();

//        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//        //Jackson's use of generics here are completely unsafe, but that's another issue
//        List<Teacher> teacherList = mapper.convertValue(
//                response,
//                new TypeReference<List<Teacher>>(){}
//        );
        log.info("Response from My Teachers request {}", response.getBody().get(0));


        return response.getBody();
    }



    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert (authentication.isAuthenticated());
        return authentication;
    }

}
