package com.usergems.meetingenrichment.email.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.usergems.meetingenrichment.person.dto.CompanyDTO;
import com.usergems.meetingenrichment.person.dto.PersonDataDTO;

import java.util.List;
import java.util.stream.Stream;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MailContent {
    private String startTime;
    private String finishTime;
    private String length;
    private String title;
    private List<String> userGemsAttendees;

    private CompanyDTO customerCompany;
    private List<PersonDataDTO> customerAccepted;
    private List<PersonDataDTO> customerRejected;

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

    public CompanyDTO getCustomerCompany() {
        return customerCompany;
    }

    public void setCustomerCompany(CompanyDTO customerCompany) {
        this.customerCompany = customerCompany;
    }

    public void setCustomerCompany() {
        var customerCOmpany = Stream.concat(getCustomerAccepted().stream(), getCustomerRejected().stream())
                .findFirst().map(PersonDataDTO::getCompany)
                .orElse(null);
        setCustomerCompany(customerCOmpany);

        getCustomerAccepted().forEach(c -> c.setCompany(null));
        getCustomerRejected().forEach(c -> c.setCompany(null));
    }

    public List<PersonDataDTO> getCustomerAccepted() {
        return customerAccepted;
    }

    public void setCustomerAccepted(List<PersonDataDTO> customerAccepted) {
        this.customerAccepted = customerAccepted;
    }

    public List<PersonDataDTO> getCustomerRejected() {
        return customerRejected;
    }

    public void setCustomerRejected(List<PersonDataDTO> customerRejected) {
        this.customerRejected = customerRejected;
    }
}

