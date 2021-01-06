package com.dd.classdiary.service;


import com.dd.classdiary.config.ReportType;
import com.dd.classdiary.model.StudentReport;
import com.dd.classdiary.model.TeacherReport;
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

import java.net.URISyntaxException;
import java.util.*;

@Service
public class TeacherReportService {

    private final Logger log = LoggerFactory.getLogger(TeacherReportService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${backend.url}")
    private String restUrl;

    public List<TeacherReport> getTotalClasses(Optional<String> studentId, ReportType reportType) throws URISyntaxException {


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
        if(studentId.isPresent()){
            params.put("studentId",studentId.get());
        }else{
            params.put("studentId","");
        }

        headers.set("Authorization", "Bearer " + userExtraDTO.getToken());
        ParameterizedTypeReference<List<TeacherReport>> responseType = new ParameterizedTypeReference<List<TeacherReport>>() {};
        String partReportUrl = "";
        if(reportType.equals(ReportType.TOTAL))
        {
            partReportUrl="/teacher-report/total-classes/{id}/{studentId}";
        }
        if(reportType.equals(ReportType.CONFIRMED))
        {
            partReportUrl="/teacher-report/total-Confirmed/{id}/{studentId}";
        }
        if(reportType.equals(ReportType.CONFIRMED_PAID))
        {
            partReportUrl="/teacher-report/total-Confirmed-Paid/{id}/{studentId}";
        }
        if(reportType.equals(ReportType.CONFIRMED_NOT_PAID))
        {
            partReportUrl="/teacher-report/total-Confirmed-NotPaid/{id}/{studentId}";
        }
        if(reportType.equals(ReportType.UNCONFIRMED))
        {
            partReportUrl="/teacher-report/total-UnConfirmed/{id}/{studentId}";
        }

        ResponseEntity<List<TeacherReport>> response = restTemplate.exchange(restUrl + partReportUrl, HttpMethod.GET, entity,responseType, params);

        if (response.getStatusCode() != HttpStatus.OK) {

        }
        log.info("Response from TeacherReports Total Classes {}", response.getBody());


        return response.getBody();
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert (authentication.isAuthenticated());
        return authentication;
    }


}
