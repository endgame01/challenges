package com.usergems.meetingenrichment.email.model;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "RICH_EMAIL")
public class Email {
    @Id
    @GeneratedValue
    private Long id;
    private String email;
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    @UpdateTimestamp
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    public Email() {
    }

    public Email(String email, String content) {
        this.email = email;
        this.content = content;
    }

    public boolean hasContent() {
        return content != null && !content.isBlank();
    }
}
