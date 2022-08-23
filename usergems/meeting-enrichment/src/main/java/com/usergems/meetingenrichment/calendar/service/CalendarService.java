package com.usergems.meetingenrichment.calendar.service;

import com.usergems.meetingenrichment.calendar.client.CalendarClient;
import com.usergems.meetingenrichment.calendar.dto.MeetingDTO;
import com.usergems.meetingenrichment.calendar.dto.MeetingsDTO;
import com.usergems.meetingenrichment.calendar.model.Meeting;
import com.usergems.meetingenrichment.calendar.model.MeetingUser;
import com.usergems.meetingenrichment.calendar.repository.MeetingRepository;
import com.usergems.meetingenrichment.calendar.repository.MeetingRepository.MeetingCountProjection;
import com.usergems.meetingenrichment.calendar.repository.MeetingUserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    private static final Logger log = LoggerFactory.getLogger(CalendarService.class);

    @Autowired
    private CalendarClient calendarClient;

    @Autowired
    private MeetingUserRepository userRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    // Using fixed delay to local test, production would be a scheduled job
//    @Scheduled(cron = "0 0 06 ? * MON-FRI")
    @Scheduled(initialDelay = 1, fixedDelay = 10000000, timeUnit = TimeUnit.SECONDS)
    public void getAndSaveUserMeetings() {
        log.info("Starting user meetings data load");
        List<MeetingUser> userGemsUsers = userRepository.findAllByTokenIsNotNull();
        log.info("we have {} users to get meetings", userGemsUsers.size());
        Map<String, MeetingUser> usersMap = userGemsUsers.stream()
                .collect(Collectors.toMap(MeetingUser::getEmail, Function.identity()));

        userGemsUsers.forEach(u -> {
            MeetingsDTO meetingsDto = calendarClient.findLatestUserMeetings(u.getBearerToken(), 1);

            List<Meeting> meetings = createMeetings(usersMap, meetingsDto);
            int pages = meetingsDto.getPages();

            for (int i = 2; i <= pages; i++) {
                meetings.addAll(createMeetings(usersMap,
                        calendarClient.findLatestUserMeetings(u.getBearerToken(), i)));
            }

            log.info("saving {} meetings for user {}", meetings.size(), u.getEmail());
            meetingRepository.saveAllAndFlush(meetings);
        });
        log.info("Finished user meetings data load\n");
    }

    private List<Meeting> createMeetings(Map<String, MeetingUser> usersMap, MeetingsDTO meetings) {
        return meetings.getData().stream()
                .map(m -> {
                    Meeting meeting = modelMapper.map(m, Meeting.class);

                    Set<MeetingUser> acceptedUsers = m.getAcceptedEmails().stream()
                            .map(acc -> usersMap.computeIfAbsent(acc, MeetingUser::new))
                            .collect(Collectors.toSet());
                    meeting.setAccepted(acceptedUsers);

                    Set<MeetingUser> rejectedUsers = m.getRejectedEmails().stream()
                            .map(acc -> usersMap.computeIfAbsent(acc, MeetingUser::new))
                            .collect(Collectors.toSet());
                    meeting.setRejected(rejectedUsers);

                    return meeting;
                }).collect(Collectors.toList());
    }

    public List<MeetingCountProjection> getCustomerMeetingsCount() {
        return meetingRepository.findCustomerMeetingCount();
    }

    public List<MeetingDTO> getAllSortedMeetings(String email, LocalDate fromDate, int pageNum) {
        log.info("Getting meetings from email {}", email);
        PageRequest page = PageRequest.of(pageNum, 10, Sort.Direction.DESC, "changed");
        Page<Meeting> pageResult;
        if (fromDate == null)
            pageResult = meetingRepository.findAllByAccepted_Email(email, page);
        else
            pageResult = meetingRepository.findAllByAccepted_EmailAndStartAfter(email, fromDate.atStartOfDay(), page);

        List<MeetingDTO> dtos = pageResult.stream()
                .limit(1)
                .map(meeting -> modelMapper.map(meeting, MeetingDTO.class))
                .collect(Collectors.toList());
        log.info("Found {} meetings from email {}", pageResult.getTotalElements(), email);
        return dtos;
    }
}
