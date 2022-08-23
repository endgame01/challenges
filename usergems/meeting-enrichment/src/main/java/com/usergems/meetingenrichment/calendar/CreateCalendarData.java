package com.usergems.meetingenrichment.calendar;

import com.usergems.meetingenrichment.calendar.model.MeetingUser;
import com.usergems.meetingenrichment.calendar.repository.MeetingUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CreateCalendarData {

    @Autowired
    private MeetingUserRepository userRepository;

    @EventListener
    @Transactional
    public void startCalendarData(ApplicationReadyEvent event) {
        MeetingUser stephan = new MeetingUser("stephan@usergems.com", "7S$16U^FmxkdV!1b");
        MeetingUser christian = new MeetingUser("christian@usergems.com", "Ay@T3ZwF3YN^fZ@M");
        MeetingUser joss = new MeetingUser("joss@usergems.com", "PK7UBPVeG%3pP9%B");
        MeetingUser blaise = new MeetingUser("blaise@usergems.com", "c0R*4iQK21McwLww");

        userRepository.saveAllAndFlush(List.of(stephan, christian, joss, blaise));
    }

}
