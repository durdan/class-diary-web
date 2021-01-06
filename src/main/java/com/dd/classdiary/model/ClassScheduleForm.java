package com.dd.classdiary.model;

import java.time.Instant;

/**
 * A ClassSchedule.
 */

public class ClassScheduleForm {


    private Long id;


    private String name;


    private String created;


    private String updated;


    private String createdBy;


    private String updatedBy;


    private String confirmedByStudent;


    private String confirmedByTeacher;


    private String comment;


    private Boolean connected;


    private long studentId;


    private long teacherId;


    private long parentId;


    private long courseId;

    private String schedule;

    private Boolean reoccurring;

    private String reoccurringType;

    private Boolean payment;

    private Boolean confirmed;
    private Boolean rescheduled;

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Boolean getRescheduled() {
        return rescheduled;
    }

    public void setRescheduled(Boolean rescheduled) {
        this.rescheduled = rescheduled;
    }

    public Boolean getPayment() {
        return payment;
    }

    public void setPayment(Boolean payment) {
        this.payment = payment;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getReoccurringType() {
        return reoccurringType;
    }

    public void setReoccurringType(String reoccurringType) {
        this.reoccurringType = reoccurringType;
    }

    public Boolean getReoccurring() {
        return reoccurring;
    }

    public void setReoccurring(Boolean reoccurring) {
        this.reoccurring = reoccurring;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getConfirmedByStudent() {
        return confirmedByStudent;
    }

    public void setConfirmedByStudent(String confirmedByStudent) {
        this.confirmedByStudent = confirmedByStudent;
    }

    public String getConfirmedByTeacher() {
        return confirmedByTeacher;
    }

    public void setConfirmedByTeacher(String confirmedByTeacher) {
        this.confirmedByTeacher = confirmedByTeacher;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "ClassScheduleForm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", confirmedByStudent='" + confirmedByStudent + '\'' +
                ", confirmedByTeacher='" + confirmedByTeacher + '\'' +
                ", comment='" + comment + '\'' +
                ", connected=" + connected +
                ", studentId=" + studentId +
                ", teacherId=" + teacherId +
                ", parentId=" + parentId +
                ", courseId=" + courseId +
                ", schedule='" + schedule + '\'' +
                ", reoccurring=" + reoccurring +
                ", reoccurringType='" + reoccurringType + '\'' +
                ", payment=" + payment +
                ", confirmed=" + confirmed +
                ", rescheduled=" + rescheduled +
                '}';
    }
}
