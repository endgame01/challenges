package com.usergems.meetingenrichment.calendar.repository;

import com.usergems.meetingenrichment.calendar.model.MeetingUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingUserRepository extends JpaRepository<MeetingUser, String> {
    List<MeetingUser> findAllByTokenIsNotNull();
}
