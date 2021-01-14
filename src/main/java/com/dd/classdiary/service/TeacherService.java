package com.dd.classdiary.service;

import com.dd.classdiary.model.Student;
import com.dd.classdiary.model.StudentList;
import com.dd.classdiary.model.Teacher;
import com.dd.classdiary.model.TeacherList;
import com.dd.classdiary.service.dto.UserExtraDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TeacherService {

    private final Logger log = LoggerFactory.getLogger(TeacherService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AccountService accountService;

    @Value("${backend.url}")
    private String restUrl;
    // http://localhost:8080/api/teachers?studentId.equals=1202

    public Student createStudent(Student student) throws URISyntaxException {
        log.info("REST request to Create Student by Teacher : {}", student);
        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

// request body parameters
        Map<String, Object> requestMap = new HashMap<>();


        requestMap.put("firstName", student.getFirstName());
        requestMap.put("lastName", student.getLastName());
        requestMap.put("email", student.getEmail());
        requestMap.put("phone", student.getPhone());
        requestMap.put("createdBy", student.getCreatedBy());
        requestMap.put("created", student.getCreated());
        requestMap.put("schoolYear", student.getSchoolYear());
        Teacher teacher = student.getTeacher();

        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        teacher.setEmail(userExtraDTO.getUserDTO().getEmail());
        teacher.setId(userExtraDTO.getUserId());
        requestMap.put("teacher", teacher);
        log.info("Details {}", getAuthentication().getDetails());
        log.info("TeacherId is {}", userExtraDTO.getUserId());

        headers.set("Authorization", "Bearer " + userExtraDTO.getToken());

// build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

// send POST request

        ResponseEntity<Student> response = restTemplate.postForEntity(restUrl + "/students", entity, Student.class);

        if (response.getStatusCode() != HttpStatus.CREATED) {

        }
        log.info("Response from Create Student request {}", response.getBody());
        return response.getBody();
    }

    public List<Student> getStudents() throws URISyntaxException {


        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        // request body parameters
        Map<String, String> params = new HashMap<>();

        log.info("REST request to get Students for teacher ", userExtraDTO.getUserId());

        params.put("teacherId.equals", userExtraDTO.getUserId().toString());

        log.info("Details {}", getAuthentication().getDetails());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(restUrl + "/students");
        params.put("size","1000");
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

    public List<Student> getStudentById(Long id){

        log.info("REST request to get a Student with Id: {}", id);
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

        log.info("REST request to get Student for Teacher {} ", userExtraDTO.getUserId());

        params.put("teacherId.equals", userExtraDTO.getUserId().toString());
        params.put("Id.equals", id.toString());


        log.info("Details {}", getAuthentication().getDetails());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(restUrl + "/students");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        headers.set("Authorization", "Bearer " + userExtraDTO.getToken());

// build the request
        HttpEntity entity = new HttpEntity<>(headers);


// send POST request

        ResponseEntity<StudentList> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, StudentList.class, params);

        if (response.getStatusCode() != HttpStatus.CREATED) {

        }

        log.info("Response from My Student request {}", response.getBody().get(0));


        return response.getBody();
    }

    public Student updateStudent(Student student) throws URISyntaxException {
        log.info("REST request to update a Student : {}", student);
        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

// request body parameters
        Map<String, Object> requestMap = new HashMap<>();


        requestMap.put("firstName", student.getFirstName());
        requestMap.put("id", student.getId());
        requestMap.put("lastName", student.getLastName());
        requestMap.put("email", student.getEmail());
        requestMap.put("phone", student.getPhone());

        requestMap.put("schoolYear", student.getSchoolYear());
        Teacher teacher = student.getTeacher();


        // String token=accountService.login(userDetails.getUsername(),userDetails.getPassword(),true);

        log.info("student {}",student);
        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        teacher.setEmail(userExtraDTO.getUserDTO().getEmail());
        teacher.setId(userExtraDTO.getUserId());
        requestMap.put("teacher", teacher);
        requestMap.put("createdBy", student.getCreatedBy());
        requestMap.put("created", student.getCreated());
//        requestMap.put("updatedBy", userExtraDTO.getUserDTO().getEmail());
        requestMap.put("updated", Instant.now());

        headers.set("Authorization", "Bearer " + userExtraDTO.getToken());

// build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

// send POST request

        ResponseEntity<Student> response = restTemplate.exchange(restUrl + "/students",HttpMethod.PUT, entity, Student.class);


        log.info("Response from update Teacher request {}", response.getBody());
        return response.getBody();
    }


    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert (authentication.isAuthenticated());
        return authentication;
    }

}
