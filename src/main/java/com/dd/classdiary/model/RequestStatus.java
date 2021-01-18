package com.dd.classdiary.model;

import java.io.Serializable;


public class RequestStatus implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    private Integer statusCode;


    private String status;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public RequestStatus statusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public RequestStatus status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestStatus)) {
            return false;
        }
        return id != null && id.equals(((RequestStatus) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequestStatus{" +
            "id=" + getId() +
            ", statusCode=" + getStatusCode() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
