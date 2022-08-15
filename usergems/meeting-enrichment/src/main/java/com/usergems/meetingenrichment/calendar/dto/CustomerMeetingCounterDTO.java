package com.usergems.meetingenrichment.calendar.dto;

public class CustomerMeetingCounterDTO {

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
