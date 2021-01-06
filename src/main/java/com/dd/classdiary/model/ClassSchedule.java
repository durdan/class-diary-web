package com.dd.classdiary.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ClassSchedule.
 */

public class ClassSchedule  {


    private Long id;


    private String name;


    private Instant created;


    private Instant updated;


    private String createdBy;


    private String updatedBy;


    private String confirmedByStudent;


    private String confirmedByTeacher;


    private String comment;


    private Boolean connected;


    private Student student;


    private Teacher teacher;


    private Parent parent;


    private Course course;
    private Instant schedule;

    private Boolean reoccurring;
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

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getConnected() {
        return connected;
    }

    public Instant getSchedule() {
        return schedule;
    }

    public void setSchedule(Instant schedule) {
        this.schedule = schedule;
    }

    public Boolean getReoccurring() {
        return reoccurring;
    }

    public void setReoccurring(Boolean reoccurring) {
        this.reoccurring = reoccurring;
    }

    public String getName() {
        return name;
    }

    public ClassSchedule name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreated() {
        return created;
    }

    public ClassSchedule created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public ClassSchedule updated(Instant updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ClassSchedule createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public ClassSchedule updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getConfirmedByStudent() {
        return confirmedByStudent;
    }

    public ClassSchedule confirmedByStudent(String confirmedByStudent) {
        this.confirmedByStudent = confirmedByStudent;
        return this;
    }

    public void setConfirmedByStudent(String confirmedByStudent) {
        this.confirmedByStudent = confirmedByStudent;
    }

    public String getConfirmedByTeacher() {
        return confirmedByTeacher;
    }

    public ClassSchedule confirmedByTeacher(String confirmedByTeacher) {
        this.confirmedByTeacher = confirmedByTeacher;
        return this;
    }

    public void setConfirmedByTeacher(String confirmedByTeacher) {
        this.confirmedByTeacher = confirmedByTeacher;
    }

    public String getComment() {
        return comment;
    }

    public ClassSchedule comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean isConnected() {
        return connected;
    }

    public ClassSchedule connected(Boolean connected) {
        this.connected = connected;
        return this;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public Student getStudent() {
        return student;
    }

    public ClassSchedule student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public ClassSchedule teacher(Teacher teacher) {
        this.teacher = teacher;
        return this;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Parent getParent() {
        return parent;
    }

    public ClassSchedule parent(Parent parent) {
        this.parent = parent;
        return this;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Course getCourse() {
        return course;
    }

    public ClassSchedule course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassSchedule)) {
            return false;
        }
        return id != null && id.equals(((ClassSchedule) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore


    @Override
    public String toString() {
        return "ClassSchedule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", confirmedByStudent='" + confirmedByStudent + '\'' +
                ", confirmedByTeacher='" + confirmedByTeacher + '\'' +
                ", comment='" + comment + '\'' +
                ", connected=" + connected +
                ", student=" + student +
                ", teacher=" + teacher +
                ", parent=" + parent +
                ", course=" + course +
                ", schedule=" + schedule +
                ", reoccurring=" + reoccurring +
                ", payment=" + payment +
                ", confirmed=" + confirmed +
                ", rescheduled=" + rescheduled +
                '}';
    }
}
