package com.usergems.meetingenrichment.person.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompanyDTO {
    private String name;
    @JsonProperty("linkedin_url")
    private String linkedinUrl;
    private int employees;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public int getEmployees() {
        return employees;
    }

    public void setEmployees(int employees) {
        this.employees = employees;
    }
}
