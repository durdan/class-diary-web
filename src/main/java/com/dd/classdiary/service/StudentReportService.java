package com.dd.classdiary.service;


import com.dd.classdiary.config.ReportType;
import com.dd.classdiary.model.ClassSchedule;
import com.dd.classdiary.model.StudentReport;
import com.dd.classdiary.service.dto.UserDTO;
import com.dd.classdiary.service.dto.UserExtraDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import java.util.*;

@Service
public class StudentReportService {

    private final Logger log = LoggerFactory.getLogger(StudentReportService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.url}")
    private String restUrl;

    public List<StudentReport> getTotalClasses(Optional<String> teacherId, ReportType reportType) throws URISyntaxException {


        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        // request body parameters
        Map<String, String> params = new HashMap<>();

        log.info("REST request to get Total Classes for user {} {} ", userExtraDTO,userExtraDTO.getUserDTO().getId().toString());



// build the request
        HttpEntity entity = new HttpEntity<>(headers);


        params.put("id",userExtraDTO.getUserId().toString());
        if(teacherId.isPresent()){
            params.put("teacherId",teacherId.get());
        }else{
            params.put("teacherId","");
        }

        headers.set("Authorization", "Bearer " + userExtraDTO.getToken());
        ParameterizedTypeReference<List<StudentReport>> responseType = new ParameterizedTypeReference<List<StudentReport>>() {};
        String partReportUrl = "";
        if(reportType.equals(ReportType.TOTAL))
        {
            partReportUrl="/student-report/total-classes/{id}/{teacherId}";
        }
        if(reportType.equals(ReportType.CONFIRMED))
        {
            partReportUrl="/student-report/total-Confirmed/{id}/{teacherId}";
        }
        if(reportType.equals(ReportType.CONFIRMED_PAID))
        {
            partReportUrl="/student-report/total-Confirmed-Paid/{id}/{teacherId}";
        }
        if(reportType.equals(ReportType.CONFIRMED_NOT_PAID))
        {
            partReportUrl="/student-report/total-Confirmed-NotPaid/{id}/{teacherId}";
        }
        if(reportType.equals(ReportType.UNCONFIRMED))
        {
            partReportUrl="/student-report/total-UnConfirmed/{id}/{teacherId}";
        }

        ResponseEntity<List<StudentReport>> response = restTemplate.exchange(restUrl + partReportUrl, HttpMethod.GET, entity,responseType, params);

        if (response.getStatusCode() != HttpStatus.OK) {

        }
        log.info("Response from Total Classes {}", response.getBody());


        return response.getBody();
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert (authentication.isAuthenticated());
        return authentication;
    }


}
