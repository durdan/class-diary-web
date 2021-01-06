package com.dd.classdiary.service;

import com.dd.classdiary.config.UserType;
import com.dd.classdiary.model.*;
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
public class CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AccountService accountService;

    @Value("${backend.url}")
    private String restUrl;
    // http://localhost:8080/api/teachers?studentId.equals=1202

    public List<Course> getCourses() throws URISyntaxException {


        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        // request body parameters
        Map<String, String> params = new HashMap<>();

        log.info("REST request to get Courses ", userExtraDTO.getUserId());
        params.put("size.equals","500");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(restUrl + "/courses");
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
        log.info("Response from My Courses request {}", response.getBody().size());
        return response.getBody();
    }





    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert (authentication.isAuthenticated());
        return authentication;
    }

}
