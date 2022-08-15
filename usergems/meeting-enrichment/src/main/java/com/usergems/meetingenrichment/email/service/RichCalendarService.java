package com.usergems.meetingenrichment.email.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usergems.meetingenrichment.calendar.dto.CustomerMeetingCounterDTO;
import com.usergems.meetingenrichment.calendar.model.MeetingUser;
import com.usergems.meetingenrichment.calendar.repository.MeetingUserRepository;
import com.usergems.meetingenrichment.email.client.CalendarClient;
import com.usergems.meetingenrichment.email.client.PersonDataInternalClient;
import com.usergems.meetingenrichment.email.dto.CalendarMeetingDTO;
import com.usergems.meetingenrichment.email.dto.PersonDataInternalDTO;
import com.usergems.meetingenrichment.email.model.Email;
import com.usergems.meetingenrichment.email.model.MailContent;
import com.usergems.meetingenrichment.email.repository.EmailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RichCalendarService {

    private static final Logger log = LoggerFactory.getLogger(RichCalendarService.class);

    @Autowired
    private CalendarClient calendarClient;

    @Autowired
    private PersonDataInternalClient personDataInternalClient;

    // Reused user repository here, but we would have another DB with the data for the cron
    @Autowired
    private MeetingUserRepository userRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    @Scheduled(cron = "0 59 07 ? * MON-FRI")
    public void sendMails() {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("hh:mm a").localizedBy(Locale.US);
        userRepository.findAllByTokenIsNotNull()
                .forEach(user -> {

                    MeetingCounterWrapper meetingsCounter = createMeetingsCounter(user);

                    List<Email> emails = getTodaysUserMeetings(user).stream()
                            .map(meeting -> createMailContent(pattern, meeting, user.getEmail(), meetingsCounter))
                            .map(content -> new Email(user.getEmail(), getContentAsString(content)))
                            .filter(Email::hasContent)
                            .collect(Collectors.toList());
                    emailRepository.saveAll(emails);
                });
    }

    private MeetingCounterWrapper createMeetingsCounter(MeetingUser user) {
        Map<String, Integer> meetingCountMap = calendarClient.countUserMeetings(user.getBearerToken()).stream()
                .collect(Collectors.toMap(CustomerMeetingCounterDTO::getCustomerEmail, CustomerMeetingCounterDTO::getCustomerMeetingCount));
        log.debug("Map has {} customer emails", meetingCountMap.size());
        return new MeetingCounterWrapper(meetingCountMap);
    }

    private Page<CalendarMeetingDTO> getTodaysUserMeetings(MeetingUser user) {
        return calendarClient
                .findLatestUserMeetings(user.getBearerToken(),
                        LocalDate.now(), PageRequest.of(0, 10));
    }

    private MailContent createMailContent(DateTimeFormatter pattern, CalendarMeetingDTO meeting,
                                          String userMail, MeetingCounterWrapper meetingCountMap) {

        MailContent mailContent = new MailContent();
        mailContent.setTitle(meeting.getTitle());
        mailContent.setStartTime(meeting.getStartDateFormatted(pattern));
        mailContent.setFinishTime(meeting.getEndDateFormatted(pattern));
        mailContent.setLength(meeting.getMeetingLengthString());
        mailContent.setUserGemsAttendees(meeting.getUserGemsAttendeesNames(userMail));

        mailContent.setCustomerAccepted(getCustomerData(meeting.getCustomersAcceptedEmails(), meetingCountMap));
        mailContent.setCustomerRejected(getCustomerData(meeting.getCustomersRejectedEmails(), meetingCountMap));

        mailContent.setCustomerCompany();
        return mailContent;
    }

    private List<PersonDataInternalDTO> getCustomerData(List<String> customerEmails,
                                                        MeetingCounterWrapper meetingCountMap) {
        return customerEmails.stream()
                .map(em -> {
                    PersonDataInternalDTO data = personDataInternalClient.getPersonByEmail(em);
                    data.setMeetingCount(meetingCountMap.getMeetingCount(em));
                    return data;
                })
                .collect(Collectors.toList());
    }


    private String getContentAsString(MailContent content) {
        try {
            return objectMapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            log.error("error converting content");
            return null;
        }
    }

    /**
     * Meeting counter solution for one user at time,
     * With time would evolve to count all user meetings and send them as the wireframe shows
     */
    static class MeetingCounterWrapper {
        private final Map<String, Integer> meetingCountMap;

        public MeetingCounterWrapper(Map<String, Integer> meetingCountMap) {
            this.meetingCountMap = meetingCountMap;
        }

        public int getMeetingCount(String email) {
            if (meetingCountMap.containsKey(email))
                return meetingCountMap.get(email);
            return 0;
        }
    }


}
