package com.usergems.meetingenrichment.calendar.repository;

import com.usergems.meetingenrichment.calendar.model.MeetingUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeetingUserRepository extends JpaRepository<MeetingUser, String> {
    Optional<MeetingUser> findByToken(String token);

    List<MeetingUser> findAllByTokenIsNotNull();
}
