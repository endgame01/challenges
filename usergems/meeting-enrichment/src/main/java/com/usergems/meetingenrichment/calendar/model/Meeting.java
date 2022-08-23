package com.usergems.meetingenrichment.calendar.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "MEETING")
public class Meeting {

    @Id
    @Column(name = "meeting_id")
    private Long id;
    private String title;

    @Column(name = "meeting_changed")
    private LocalDateTime changed;

    @Column(name = "meeting_start")
    private LocalDateTime start;
    @Column(name = "meeting_end")
    private LocalDateTime end;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_accepted_meetings",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "email")
    )
    private Set<MeetingUser> accepted = new HashSet<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_rejected_meetings",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "email")
    )
    private Set<MeetingUser> rejected = new HashSet<>();

    public Meeting() {
    }

    public Meeting(String title,
                   LocalDateTime start, LocalDateTime end,
                   Set<MeetingUser> accepted, Set<MeetingUser> rejected) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.accepted.addAll(accepted);
        this.rejected.addAll(rejected);
    }

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

    public Set<MeetingUser> getAccepted() {
        return accepted;
    }

    public void setAccepted(Set<MeetingUser> accepted) {
        this.accepted = accepted;
    }

    public Set<MeetingUser> getRejected() {
        return rejected;
    }

    public void setRejected(Set<MeetingUser> rejected) {
        this.rejected = rejected;
    }

    public List<String> getAcceptedEmails() {
        return accepted.stream().map(MeetingUser::getEmail).collect(Collectors.toList());
    }

    public List<String> getRejectedEmails() {
        return rejected.stream().map(MeetingUser::getEmail).collect(Collectors.toList());
    }
}
