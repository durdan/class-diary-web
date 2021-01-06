package com.dd.classdiary.model;

public class SearchFilter {

    Long filterId;
    String startDate;
    String endDate;
    boolean payment;
    boolean confirmed;
    boolean rescheduled;

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

    public boolean isPayment() {
        return payment;
    }

    public void setPayment(boolean payment) {
        this.payment = payment;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isRescheduled() {
        return rescheduled;
    }

    public void setRescheduled(boolean rescheduled) {
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

