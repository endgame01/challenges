package com.usergems.meetingenrichment.email.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usergems.meetingenrichment.calendar.dto.MeetingDTO;
import com.usergems.meetingenrichment.calendar.repository.MeetingRepository.MeetingCountProjection;
import com.usergems.meetingenrichment.calendar.repository.MeetingUserRepository;
import com.usergems.meetingenrichment.calendar.service.CalendarService;
import com.usergems.meetingenrichment.email.model.Email;
import com.usergems.meetingenrichment.email.model.MailContent;
import com.usergems.meetingenrichment.email.repository.EmailRepository;
import com.usergems.meetingenrichment.person.dto.PersonDataDTO;
import com.usergems.meetingenrichment.person.service.PersonDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RichCalendarService {

    private static final Logger log = LoggerFactory.getLogger(RichCalendarService.class);

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private PersonDataService personDataService;

    @Autowired
    private MeetingUserRepository userRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    // Using fixed delay to local test, production would be a scheduled job
//    @Scheduled(cron = "0 0 08 ? * MON-FRI")
    @Scheduled(initialDelay = 10, fixedDelay = 10000000, timeUnit = TimeUnit.SECONDS)
    public void sendMails() {
        log.info("Starting to send user enriched meetings emails");
        List<Email> emailsToSend = emailRepository.findAllBySentAtIsNull();
        emailsToSend.forEach(Email::send);
        log.info("Sent {} emails to users", emailsToSend.size());
        emailRepository.saveAll(emailsToSend);
        log.info("Finished sending user enriched meetings emails\n");
    }

    @Transactional
    // Using fixed delay to local test, production would be a scheduled job
//    @Scheduled(cron = "0 0 07 ? * MON-FRI")
    @Scheduled(initialDelay = 5, fixedDelay = 10000000, timeUnit = TimeUnit.SECONDS)
    public void createMailsToSend() {
        log.info("Starting user meetings email creation");
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("hh:mm a").localizedBy(Locale.US);
        var meetingsCounter = createMeetingsCounter();
        userRepository.findAllByTokenIsNotNull()
                .forEach(user -> {
                    List<Email> emails = calendarService.getAllSortedMeetings(user.getEmail(), null, 0).stream()
                            .map(meeting -> createMailContent(pattern, meeting, user.getEmail(), meetingsCounter))
                            .map(content -> new Email(user.getEmail(), getContentAsString(content)))
                            .filter(Email::hasContent)
                            .collect(Collectors.toList());
                    log.info("Saving {} emails for user {}", emails.size(), user.getEmail());
                    emailRepository.saveAll(emails);
                });
        log.info("Finished user meetings email creation\n");
    }

    private CustomerMeetingCounterWrapper createMeetingsCounter() {
        return new CustomerMeetingCounterWrapper(calendarService.getCustomerMeetingsCount());
    }

    private MailContent createMailContent(DateTimeFormatter pattern, MeetingDTO meeting,
                                          String userMail, CustomerMeetingCounterWrapper meetingCountMap) {

        MailContent mailContent = new MailContent();
        mailContent.setTitle(meeting.getTitle());
        mailContent.setStartTime(meeting.getStartDateFormatted(pattern));
        mailContent.setFinishTime(meeting.getEndDateFormatted(pattern));
        mailContent.setLength(meeting.getMeetingLengthString());
        mailContent.setUserGemsAttendees(meeting.getUserGemsAttendeesNames(userMail));

        mailContent.setCustomerAccepted(getCustomerData(userMail, meeting.getCustomersAcceptedEmails(), meetingCountMap));
        mailContent.setCustomerRejected(getCustomerData(userMail, meeting.getCustomersRejectedEmails(), meetingCountMap));

        mailContent.setCustomerCompany();
        return mailContent;
    }

    private List<PersonDataDTO> getCustomerData(String userMail, List<String> customerEmails,
                                                CustomerMeetingCounterWrapper meetingCountMap) {
        return customerEmails.stream()
                .map(em -> {
                    var data = personDataService.getPersonData(em);
                    String meetings = meetingCountMap.getMeetingsCount(em, userMail);
                    data.setMeetings(meetings);
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

    static class CustomerMeetingCounterWrapper {
        Map<String, Map<String, Integer>> meetingCountMap;

        public CustomerMeetingCounterWrapper(List<MeetingCountProjection> meetingsCounts) {
            meetingCountMap = meetingsCounts.stream()
                    .collect(Collectors.groupingBy(MeetingCountProjection::getCustomerEmail,
                            Collectors.groupingBy(MeetingCountProjection::getUserMail,
                                    Collectors.summingInt(MeetingCountProjection::getCustomerMeetingCount))));
        }

        public String getMeetingsCount(String customerEmail, String userEmail) {
            var result = new StringBuilder();
            Map<String, Integer> customerMeetings = meetingCountMap.get(customerEmail);
            if (customerMeetings != null) {
                Integer integer = customerMeetings.get(userEmail);
                if (integer != null) {
                    result.append(integer).append(getModifier(integer)).append(" Meeting");
                }
                if (customerMeetings.size() > 1)
                    result.append("| Met with ");
                customerMeetings.forEach((k, v) -> {
                    if (!userEmail.equalsIgnoreCase(k)) {
                        result.append(k.split("@")[0]).append(" (").append(v).append("x)").append(" & ");
                    }
                });
                result.setLength(result.length() - 3);
            }

            return result.toString();
        }

        private String getModifier(int num) {
            switch (num) {
                case 1:
                    return "st";
                case 2:
                    return "nd";
                case 3:
                    return "rd";
                default:
                    return "th";
            }
        }
    }


}
