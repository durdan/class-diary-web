package com.dd.classdiary.model;

public class SharedRequestForm {

    Long sharedUserId;
    String email;

    public Long getSharedUserId() {
        return sharedUserId;
    }

    public void setSharedUserId(Long sharedUserId) {
        this.sharedUserId = sharedUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
