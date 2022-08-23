package com.usergems.meetingenrichment.calendar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.usergems.meetingenrichment.calendar.serializer.MultiDateDeserializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class MeetingDTO {

    private Long id;
    private String title;

    @JsonDeserialize(using = MultiDateDeserializer.class)
    private LocalDateTime changed;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private LocalDateTime start;
    @JsonDeserialize(using = MultiDateDeserializer.class)
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


    public String getStartDateFormatted(DateTimeFormatter pattern) {
        return getStart().format(pattern);
    }

    public String getEndDateFormatted(DateTimeFormatter pattern) {
        return getEnd().format(pattern);
    }

    public String getMeetingLengthString() {
        return ChronoUnit.MINUTES.between(getStart(), getEnd()) + " min";
    }

    public List<String> getUserGemsAttendeesNames(String userName) {
        return getAcceptedEmails().stream()
                .filter(em -> em.contains("@usergems.com"))
                .filter(em -> !em.contains(userName))
                .map(em -> em.split("@")[0])
                .collect(Collectors.toList());
    }

    public List<String> getCustomersAcceptedEmails() {
        return getAcceptedEmails().stream()
                .filter(em -> !em.contains("@usergems.com"))
                .collect(Collectors.toList());
    }

    public List<String> getCustomersRejectedEmails() {
        return getRejectedEmails().stream()
                .filter(em -> !em.contains("@usergems.com"))
                .collect(Collectors.toList());
    }
}
