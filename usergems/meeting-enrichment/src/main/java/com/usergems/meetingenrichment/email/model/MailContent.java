package com.usergems.meetingenrichment.email.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.usergems.meetingenrichment.email.dto.CompanyInternalDTO;
import com.usergems.meetingenrichment.email.dto.PersonDataInternalDTO;

import java.util.List;
import java.util.stream.Stream;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MailContent {
    private String startTime;
    private String finishTime;
    private String length;
    private String title;
    private List<String> userGemsAttendees;

    private CompanyInternalDTO customerCompany;
    private List<PersonDataInternalDTO> customerAccepted;
    private List<PersonDataInternalDTO> customerRejected;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getUserGemsAttendees() {
        return userGemsAttendees;
    }

    public void setUserGemsAttendees(List<String> userGemsAttendees) {
        this.userGemsAttendees = userGemsAttendees;
    }

    public CompanyInternalDTO getCustomerCompany() {
        return customerCompany;
    }

    public void setCustomerCompany(CompanyInternalDTO customerCompany) {
        this.customerCompany = customerCompany;
    }

    public void setCustomerCompany() {
        var customerCOmpany = Stream.concat(getCustomerAccepted().stream(), getCustomerRejected().stream())
                .findFirst().map(PersonDataInternalDTO::getCompany)
                .orElse(null);
        setCustomerCompany(customerCOmpany);

        getCustomerAccepted().forEach(c -> c.setCompany(null));
        getCustomerRejected().forEach(c -> c.setCompany(null));
    }

    public List<PersonDataInternalDTO> getCustomerAccepted() {
        return customerAccepted;
    }

    public void setCustomerAccepted(List<PersonDataInternalDTO> customerAccepted) {
        this.customerAccepted = customerAccepted;
    }

    public List<PersonDataInternalDTO> getCustomerRejected() {
        return customerRejected;
    }

    public void setCustomerRejected(List<PersonDataInternalDTO> customerRejected) {
        customerRejected.forEach(c -> c.setMeetingCount(c.getMeetingCount() + 1));
        this.customerRejected = customerRejected;
    }
}

