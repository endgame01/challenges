package com.usergems.meetingenrichment.calendar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public class MeetingDTO {

    private Long id;
    private String title;

    private LocalDateTime changed;
    private LocalDateTime start;
    private LocalDateTime end;

    @JsonProperty("accepted")
    private List<String> acceptedEmails;

    @JsonProperty("rejected")
    private List<String> rejectedEmails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getChanged() {
        return changed;
    }

    public void setChanged(LocalDateTime changed) {
        this.changed = changed;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public List<String> getAcceptedEmails() {
        return acceptedEmails;
    }

    public void setAcceptedEmails(List<String> acceptedEmails) {
        this.acceptedEmails = acceptedEmails;
    }

    public List<String> getRejectedEmails() {
        return rejectedEmails;
    }

    public void setRejectedEmails(List<String> rejectedEmails) {
        this.rejectedEmails = rejectedEmails;
    }
}
