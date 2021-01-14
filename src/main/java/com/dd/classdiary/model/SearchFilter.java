package com.dd.classdiary.model;

public class SearchFilter {

    Long filterId;
    String startDate;
    String endDate;
    Boolean payment;
    Boolean confirmed;
    Boolean rescheduled;

    public Long getFilterId() {
        return filterId;
    }

    public void setFilterId(Long filterId) {
        this.filterId = filterId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Boolean getPayment() {
        return payment;
    }

    public void setPayment(Boolean payment) {
        this.payment = payment;
    }

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

    @Override
    public String toString() {
        return "SearchFilter{" +
                "filterId=" + filterId +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", payment=" + payment +
                ", confirmed=" + confirmed +
                ", rescheduled=" + rescheduled +
                '}';
    }
}

