package com.usergems.meetingenrichment.email.dto;

public class CalendarMeetingCounterDTO {

    public String customerEmail;
    public int customerMeetingCount;

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public int getCustomerMeetingCount() {
        return customerMeetingCount;
    }

    public void setCustomerMeetingCount(int customerMeetingCount) {
        this.customerMeetingCount = customerMeetingCount;
    }
}
