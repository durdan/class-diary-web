package com.dd.classdiary.service;

import com.dd.classdiary.config.UserType;
import com.dd.classdiary.model.*;
import com.dd.classdiary.service.dto.UserExtraDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;


@Service
public class SharedInfoService {

    private final Logger log = LoggerFactory.getLogger(SharedInfoService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AccountService accountService;

    @Value("${backend.url}")
    private String restUrl;
    // http://localhost:8080/api/teachers?studentId.equals=1202


    public RequestTask sharedInfo(Long sharedUserId) throws URISyntaxException {
        log.info("REST request to Shared Info : {}", sharedUserId);
        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

// request body parameters
        Map<String, Object> requestMap = new HashMap<>();


        requestMap.put("requiredActionFromUserId",sharedUserId);

        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        if(userExtraDTO.getUserType().equalsIgnoreCase(UserType.STUDENT.name())){
            requestMap.put("requestType",RequestTypeEnum.STUDENT_SHARED.name());
        }
        if(userExtraDTO.getUserType().equalsIgnoreCase(UserType.TEACHER.name())){
            requestMap.put("requestType",RequestTypeEnum.TEACHER_SHARED.name());
        }

        requestMap.put("requestedUserId",userExtraDTO.getUserId());
        UUID uuid=UUID.randomUUID(); //Generates random UUID.

        requestMap.put("created",Instant.now());
        requestMap.put("createdBy",userExtraDTO.getUserDTO().getFirstName()+" "+userExtraDTO.getUserDTO().getLastName());
        User user = new User();
        user.setId(userExtraDTO.getUserDTO().getId());
        //requestMap.put("requestStatus",RequestStatusEnum.ACTIVE);
        requestMap.put("user",user);
        requestMap.put("requestCode",uuid);
        headers.set("Authorization", "Bearer " + userExtraDTO.getToken());

// build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

// send POST request

        ResponseEntity<RequestTask> response = restTemplate.postForEntity(restUrl + "/request-tasks", entity, RequestTask.class);

        if (response.getStatusCode() != HttpStatus.CREATED) {

        }
        log.info("Response from Create Class request {}", response.getBody());
        return response.getBody();
    }

    public List<ClassSchedule> getSharedClassSchedule(String key) throws URISyntaxException {


        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

       // UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        // request body parameters
        Map<String, String> params = new HashMap<>();

        log.info("REST request to get Shared Class Schedule with key ", key);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(restUrl + "/getSharedDetails/{key}");


// build the request
        HttpEntity entity = new HttpEntity<>(headers);


        params.put("key",key);

      //  headers.set("Authorization", "Bearer " + userExtraDTO.getToken());
        ParameterizedTypeReference<List<ClassSchedule>> responseType = new ParameterizedTypeReference<List<ClassSchedule>>() {};


        ResponseEntity<List<ClassSchedule>> response = restTemplate.exchange(restUrl + "/getSharedDetails/{key}", HttpMethod.GET, entity,responseType, params);

        if (response.getStatusCode() != HttpStatus.OK) {
            return Collections.emptyList();
        }
        log.info("Response from Shared Class Schedules with size {}", response.getBody().size());


        return response.getBody();
    }




    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert (authentication.isAuthenticated());
        return authentication;
    }

}
