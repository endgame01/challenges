package com.usergems.meetingenrichment.calendar;

import com.usergems.meetingenrichment.calendar.model.Meeting;
import com.usergems.meetingenrichment.calendar.model.MeetingUser;
import com.usergems.meetingenrichment.calendar.repository.MeetingRepository;
import com.usergems.meetingenrichment.calendar.repository.MeetingUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class CreateCalendarData {

    @Autowired
    private MeetingUserRepository userRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @EventListener
    @Transactional
    public void startCalendarData(ApplicationReadyEvent event) {
        MeetingUser stephan = new MeetingUser("stephan@usergems.com", "7S$16U^FmxkdV!1b");
        MeetingUser christian = new MeetingUser("christian@usergems.com", "Ay@T3ZwF3YN^fZ@M");
        MeetingUser joss = new MeetingUser("joss@usergems.com", "PK7UBPVeG%3pP9%B");
        MeetingUser blaise = new MeetingUser("blaise@usergems.com", "c0R*4iQK21McwLww");

        MeetingUser demi = new MeetingUser("demi@algolia.com");
        MeetingUser joshua = new MeetingUser("joshua@algolia.com");
        MeetingUser woojin = new MeetingUser("woojin@algolia.com");
        MeetingUser aletta = new MeetingUser("aletta@algolia.com");

        // add previous 11 meetings between Stephan and Demi
        for (int i = 1; i < 12; i++) {
            meetingRepository.save(new Meeting("Stephan x Demi",
                    LocalDateTime.of(2022, 6, i, 9, 30, 0),
                    LocalDateTime.of(2022, 6, i, 10, 0, 0),
                    Set.of(stephan, demi),
                    Set.of()));
        }

        // add previous 10 meetings between Stephan and Joshua
        for (int i = 1; i < 11; i++) {
            meetingRepository.save(new Meeting("Stephan x Joshua",
                    LocalDateTime.of(2022, 6, i, 10, 30, 0),
                    LocalDateTime.of(2022, 6, i, 11, 0, 0),
                    Set.of(stephan, joshua),
                    Set.of()));
        }

        // add previous 2 meetings between Stephan and Woojin
        for (int i = 1; i < 3; i++) {
            meetingRepository.save(new Meeting("Stephan x Woojin",
                    LocalDateTime.of(2022, 6, i, 11, 30, 0),
                    LocalDateTime.of(2022, 6, i, 12, 0, 0),
                    Set.of(stephan, woojin),
                    Set.of()));
        }

        // add previous 3 meetings between Stephan and Aletta
        for (int i = 1; i < 4; i++) {
            meetingRepository.save(new Meeting("Stephan x Aletta",
                    LocalDateTime.of(2022, 6, i, 12, 30, 0),
                    LocalDateTime.of(2022, 6, i, 13, 0, 0),
                    Set.of(stephan, aletta),
                    Set.of()));
        }

        // add one meeting between Christian and Algolia
        meetingRepository.save(new Meeting("Christian x Algolia",
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(15),
                Set.of(christian, demi, joshua, aletta),
                Set.of()));

        // add previous 3 meetings between Blaise and Demi
        for (int i = 1; i < 4; i++) {
            meetingRepository.save(new Meeting("Blaise x Demi",
                    LocalDateTime.of(2022, 6, i, 12, 30, 0),
                    LocalDateTime.of(2022, 6, i, 13, 0, 0),
                    Set.of(blaise, demi),
                    Set.of()));
        }

        // add 1 meeting between Blaise, Demi and Joshua
        meetingRepository.save(new Meeting("Blaise x Demi & Joshua",
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(45),
                Set.of(blaise, demi, joshua),
                Set.of()));


        // Add current wireframe meeting to send in today's e-mail
        Meeting userGemsXAlgolia = new Meeting("UserGems x Algolia",
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(30),
                Set.of(stephan, joss, demi, joshua, woojin),
                Set.of(aletta));

        meetingRepository.save(userGemsXAlgolia);
    }

}
