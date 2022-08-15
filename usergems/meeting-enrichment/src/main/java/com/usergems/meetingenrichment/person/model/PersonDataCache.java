package com.usergems.meetingenrichment.person.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "PERSON_DATA_CACHE")
public class PersonDataCache {
    @Id
    private String email;
    private String firstName;
    private String lastName;
    private String avatar;
    private String title;
    private String linkedinUrl;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    private String companyName;
    private String companyLinkedinUrl;
    private int companyEmployees;

    public PersonDataCache() {
    }

    public PersonDataCache(String email, String firstName, String lastName, String avatar, String title,
                           String linkedinUrl, String companyName, String companyLinkedinUrl, int companyEmployees) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.title = title;
        this.linkedinUrl = linkedinUrl;
        this.companyName = companyName;
        this.companyLinkedinUrl = companyLinkedinUrl;
        this.companyEmployees = companyEmployees;
    }

    public boolean isExpired(int days) {
        return updateDateTime.plusDays(days).isBefore(LocalDateTime.now());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLinkedinUrl() {
        return companyLinkedinUrl;
    }

    public void setCompanyLinkedinUrl(String companyLinkedinUrl) {
        this.companyLinkedinUrl = companyLinkedinUrl;
    }

    public int getCompanyEmployees() {
        return companyEmployees;
    }

    public void setCompanyEmployees(int companyEmployees) {
        this.companyEmployees = companyEmployees;
    }

    public void setUpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }
}
