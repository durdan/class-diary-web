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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static java.time.ZoneOffset.UTC;


@Service
public class ClassScheduleService {

    private final Logger log = LoggerFactory.getLogger(ClassScheduleService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AccountService accountService;

    @Value("${backend.url}")
    private String restUrl;
    // http://localhost:8080/api/teachers?studentId.equals=1202

    String pattern1 = "MM/dd/yyyy hh:mm a";
    String pattern = "hh:mm:ss a";
    DateFormat dateFormat = new SimpleDateFormat(pattern1);
    SimpleDateFormat sdf1 = new SimpleDateFormat(pattern1);
    String pattern2 = "yyyy-MM-dd hh:mm";
    SimpleDateFormat sdf2 = new SimpleDateFormat(pattern2);
    String pattern3 = "yyyy-MM-dd";
    SimpleDateFormat sdf3 = new SimpleDateFormat(pattern3);

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");;

    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("MM/dd/YYYY hh:mm a")
            .parseDefaulting(ChronoField.HOUR_OF_AMPM, 0) // this is required
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0) // optional, but you can set other value
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0) // optional as well
            .toFormatter();


    public ClassSchedule createClass(ClassScheduleForm classScheduleForm) throws URISyntaxException {
        log.info("REST request to Create Class : {}", classScheduleForm);
        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

// request body parameters
        Map<String, Object> requestMap = new HashMap<>();


        ;
        // create headers
        requestMap.put("name", classScheduleForm.getName());
        Course course = new Course();
        course.setId(classScheduleForm.getCourseId());
        requestMap.put("course", course);
        requestMap.put("confirmed", false);
        requestMap.put("payment", false);
        requestMap.put("rescheduled", false);


      //  OffsetDateTime odtInstanceAtOffset = OffsetDateTime.parse(classScheduleForm.getSchedule(), formatter);
//      LocalDateTime dateTime = LocalDateTime.parse(classScheduleForm.getSchedule(), dtf);

        SimpleDateFormat fallback = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        fallback.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {

            log.info("Schedule date is : {}",fallback.parse(classScheduleForm.getSchedule()));
            requestMap.put("schedule", fallback.parse(classScheduleForm.getSchedule()));

        } catch (ParseException e) {
            e.printStackTrace();
        }





        //LocalDate localDate=LocalDate.parse(classScheduleForm.getSchedule(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS").withZone(UTC));

//        ZonedDateTime d = ZonedDateTime.parse(classScheduleForm.getSchedule());
//        getWeeklyMeetingDates(d.toLocalDate(),5);


        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();


        if(userExtraDTO.getUserType().equals(UserType.STUDENT.name())){
            Student student = new Student();
            student.setId(userExtraDTO.getUserId());
            student.setEmail(userExtraDTO.getUserDTO().getEmail());
            Teacher teacher = new Teacher();
            teacher.setId(classScheduleForm.getTeacherId());
            requestMap.put("student", student);
            requestMap.put("teacher", teacher);


        }
        if(userExtraDTO.getUserType().equals(UserType.PARENT.name())){
            Parent parent = new Parent();
            parent.setId(userExtraDTO.getUserId());
            parent.setEmail(userExtraDTO.getUserDTO().getEmail());
            requestMap.put("parent", parent);
            Teacher teacher = new Teacher();
            teacher.setId(classScheduleForm.getTeacherId());
            requestMap.put("teacher", teacher);

        }
        if(userExtraDTO.getUserType().equals(UserType.TEACHER.name())){
            log.info("Creating a call by teacher {} for student {} ",userExtraDTO.getUserDTO().getEmail());
            Teacher teacher = new Teacher();
            Student student = new Student();
            student.setId(classScheduleForm.getStudentId());
            requestMap.put("student", student);
            teacher.setId(userExtraDTO.getUserId());
            teacher.setEmail(userExtraDTO.getUserDTO().getEmail());
            requestMap.put("teacher", teacher);

        }

        requestMap.put("reoccurringType", classScheduleForm.getReoccurringType());
        requestMap.put("created", Instant.now());
        requestMap.put("createdBy", userExtraDTO.getUserDTO().getEmail());

        headers.set("Authorization", "Bearer " + userExtraDTO.getToken());

// build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

// send POST request

        ResponseEntity<ClassSchedule> response = restTemplate.postForEntity(restUrl + "/class-schedules", entity, ClassSchedule.class);

        if (response.getStatusCode() != HttpStatus.CREATED) {

        }
        log.info("Response from Create Class request {}", response.getBody());
        return response.getBody();
    }

    public ClassSchedule updateClass(ClassScheduleForm classScheduleForm) throws URISyntaxException {
        log.info("REST request to Update Class : {}", classScheduleForm);
        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

// request body parameters
        Map<String, Object> requestMap = new HashMap<>();


        ;
        // create headers
        requestMap.put("name", classScheduleForm.getName());
        Course course = new Course();
        course.setId(classScheduleForm.getCourseId());

        requestMap.put("course", course);
        requestMap.put("confirmed", classScheduleForm.getConfirmed());
        requestMap.put("rescheduled", classScheduleForm.getRescheduled());
        requestMap.put("id", classScheduleForm.getId());
        requestMap.put("created", classScheduleForm.getCreated());
        requestMap.put("createdBy", classScheduleForm.getCreatedBy());
        requestMap.put("confirmedByStudent", classScheduleForm.getConfirmedByStudent());
        requestMap.put("confirmedByTeacher", classScheduleForm.getConfirmedByTeacher());
        requestMap.put("payment", classScheduleForm.getPayment());


        //  OffsetDateTime odtInstanceAtOffset = OffsetDateTime.parse(classScheduleForm.getSchedule(), formatter);
//      LocalDateTime dateTime = LocalDateTime.parse(classScheduleForm.getSchedule(), dtf);


        SimpleDateFormat fallback = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        fallback.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {

            log.info("Schedule date is : {}",fallback.parse(classScheduleForm.getSchedule()));
            requestMap.put("schedule", fallback.parse(classScheduleForm.getSchedule()));

        } catch (ParseException e) {
            e.printStackTrace();
        }


        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        requestMap.put("updatedBy",userExtraDTO.getUserDTO().getEmail());
        requestMap.put("updated",Instant.now());

        if(userExtraDTO.getUserType().equals(UserType.STUDENT.name())){
            Student student = new Student();
            student.setId(userExtraDTO.getUserId());
            student.setEmail(userExtraDTO.getUserDTO().getEmail());
            Teacher teacher = new Teacher();
            teacher.setId(classScheduleForm.getTeacherId());
            requestMap.put("student", student);
            requestMap.put("teacher", teacher);


        }
        if(userExtraDTO.getUserType().equals(UserType.PARENT.name())){
            Parent parent = new Parent();
            parent.setId(userExtraDTO.getUserId());
            parent.setEmail(userExtraDTO.getUserDTO().getEmail());
            requestMap.put("parent", parent);
            Teacher teacher = new Teacher();
            teacher.setId(classScheduleForm.getTeacherId());
            requestMap.put("teacher", teacher);

        }
        if(userExtraDTO.getUserType().equals(UserType.TEACHER.name())){
            Teacher teacher = new Teacher();
            Student student = new Student();
            student.setId(classScheduleForm.getStudentId());
            requestMap.put("student", student);
            teacher.setId(userExtraDTO.getUserId());
            teacher.setEmail(userExtraDTO.getUserDTO().getEmail());
            requestMap.put("teacher", teacher);

        }

        requestMap.put("reoccurringType", classScheduleForm.getReoccurringType());

        headers.set("Authorization", "Bearer " + userExtraDTO.getToken());

// build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

// send POST request

        ResponseEntity<ClassSchedule> response = restTemplate.exchange(restUrl + "/class-schedules",HttpMethod.PUT, entity, ClassSchedule.class);

        if (response.getStatusCode() != HttpStatus.CREATED) {

        }
        log.info("Response from Create Class request {}", response.getBody());
        return response.getBody();
    }

    public List<ClassSchedule> getClassSchedules(Long filterId,Boolean payment,Boolean confirmed,Boolean rescheduled,String startDate,String endDate) throws URISyntaxException {


        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        // request body parameters
        Map<String, String> params = new HashMap<>();

        log.info("REST request to get Class Schedule ", userExtraDTO.getUserId());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(restUrl + "/class-schedules");


// build the request
        HttpEntity entity = new HttpEntity<>(headers);

        //params.put("page","1");
        if(startDate!=null && !startDate.isBlank()){
            ZonedDateTime d = ZonedDateTime.parse(startDate);
            params.put("schedule.greaterThanOrEqual",d.toLocalDateTime().toInstant(ZoneOffset.UTC).toString());
        }else{
           // params.put("schedule.greaterThanOrEqual",Instant.now().toString());
        }
        if(endDate!=null && !endDate.isBlank() ){
            ZonedDateTime d = ZonedDateTime.parse(endDate);

            params.put("schedule.lessThanOrEqual",d.toLocalDateTime().toInstant(ZoneOffset.UTC).toString());
        }else{
            params.put("schedule.lessThanOrEqual",Instant.now().plus(Period.ofDays(30)).toString());
        }
        if(startDate==null && endDate==null){
            params.put("schedule.lessThanOrEqual",Instant.now().plus(Period.ofDays(30)).toString());

        }


        if(payment!=null){
            params.put("payment.equals",payment.toString());
        }
        if(confirmed!=null){
            params.put("confirmed.equals",confirmed.toString());
        }
        if(rescheduled!=null){
            params.put("rescheduled.equals",rescheduled.toString());
        }


// send GET request
        if(userExtraDTO.getUserType().equals(UserType.STUDENT.name())){
            Student student = new Student();
            student.setId(userExtraDTO.getUserId());
            student.setEmail(userExtraDTO.getUserDTO().getEmail());
            params.put("studentId.equals", userExtraDTO.getUserId().toString());
            if(filterId!=null && filterId!=0){
                params.put("teacherId.equals",Long.toString(filterId));

            }

        }
        if(userExtraDTO.getUserType().equals(UserType.PARENT.name())){
            Parent parent = new Parent();
            parent.setId(userExtraDTO.getUserId());
            parent.setEmail(userExtraDTO.getUserDTO().getEmail());
            params.put("parentId.equals", userExtraDTO.getUserId().toString());

        }
        if(userExtraDTO.getUserType().equals(UserType.TEACHER.name())){
            Teacher teacher = new Teacher();
            teacher.setId(userExtraDTO.getUserId());
            teacher.setEmail(userExtraDTO.getUserDTO().getEmail());
            params.put("teacherId.equals", userExtraDTO.getUserId().toString());
            if(filterId!=null && filterId!=0){
                params.put("studentId.equals",Long.toString(filterId));

            }

        }
        params.put("size","1000");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        headers.set("Authorization", "Bearer " + userExtraDTO.getToken());
        ParameterizedTypeReference<List<ClassSchedule>> responseType = new ParameterizedTypeReference<List<ClassSchedule>>() {};

        ResponseEntity<List<ClassSchedule>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, responseType, params);

        if (response.getStatusCode() != HttpStatus.CREATED) {

        }
        log.info("Response from Class Schedules {}", response.getBody().size());


        return response.getBody();
    }

    public ClassSchedule getClassScheduleById(Long id) throws URISyntaxException {


        // create headers
        HttpHeaders headers = new HttpHeaders();
// set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        UserExtraDTO userExtraDTO = (UserExtraDTO) getAuthentication().getDetails();
        // request body parameters
        Map<String, String> params = new HashMap<>();

        log.info("REST request to get Class Schedule with id ", userExtraDTO.getUserId());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(restUrl + "/class-schedules/{id}");


// build the request
        HttpEntity entity = new HttpEntity<>(headers);


        params.put("id",id.toString());

        headers.set("Authorization", "Bearer " + userExtraDTO.getToken());

        ResponseEntity<ClassSchedule> response = restTemplate.exchange(restUrl + "/class-schedules/{id}", HttpMethod.GET, entity,ClassSchedule.class, params);

        if (response.getStatusCode() != HttpStatus.CREATED) {

        }
        log.info("Response from Class Schedules with id {}", response.getBody());


        return response.getBody();
    }


    private  List<LocalDate>
    getWeeklyMeetingDates(LocalDate localDate, int count)
    {
        //Custom temporal adjuster with lambda
        TemporalAdjuster temporalAdjuster = t -> t.plus(Period.ofDays(7));
        List<LocalDate> dates = new ArrayList<>();
        for(int i = 0; i < count; i++)
        {
            localDate = localDate
                    .with(TemporalAdjusters.next(localDate.getDayOfWeek()));

            dates.add(localDate);
            log.info("Weekly {}",localDate);
            log.info("BI-WEEKLY{}",localDate.with(TemporalAdjusters.next(localDate.getDayOfWeek())));
            //log.info("MONTHLY {}",localDate.with(TemporalAdjusters.firstInMonth(0,localDate.getDayOfWeek())));
        }
        return dates;
    }


    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert (authentication.isAuthenticated());
        return authentication;
    }

}
