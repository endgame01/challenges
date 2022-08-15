package com.usergems.meetingenrichment.calendar.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MEETING_USER")
public class MeetingUser {

    @Id
    private String email;
    private String token;

    @ManyToMany(mappedBy = "accepted")
    private Set<Meeting> acceptedMeetings = new HashSet<>();

    @ManyToMany(mappedBy = "rejected")
    private Set<Meeting> rejectedMeetings = new HashSet<>();

    public MeetingUser() {
    }

    public MeetingUser(String email) {
        this.email = email;
    }

    public MeetingUser(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBearerToken() {
        return "Bearer " + getToken();
    }

    public Set<Meeting> getAcceptedMeetings() {
        return acceptedMeetings;
    }

    public void setAcceptedMeetings(Set<Meeting> acceptedMeetings) {
        this.acceptedMeetings = acceptedMeetings;
    }

    public Set<Meeting> getRejectedMeetings() {
        return rejectedMeetings;
    }

    public void setRejectedMeetings(Set<Meeting> rejectedMeetings) {
        this.rejectedMeetings = rejectedMeetings;
    }
}
