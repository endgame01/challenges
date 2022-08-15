package com.usergems.meetingenrichment.calendar.service;

import com.usergems.meetingenrichment.calendar.dto.CustomerMeetingCounterDTO;
import com.usergems.meetingenrichment.calendar.dto.MeetingDTO;
import com.usergems.meetingenrichment.calendar.model.Meeting;
import com.usergems.meetingenrichment.calendar.model.MeetingUser;
import com.usergems.meetingenrichment.calendar.repository.MeetingRepository;
import com.usergems.meetingenrichment.calendar.repository.MeetingUserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    private static final Logger log = LoggerFactory.getLogger(CalendarService.class);

    @Autowired
    private MeetingUserRepository userRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private ModelMapper modelMapper;

    public String getEmailFromToken(String bearer) {
        String token = bearer.split(" ")[1];
        log.info("Getting email from token {}", token);
        return userRepository.findByToken(token).map(MeetingUser::getEmail).orElse("");
    }

    public Page<MeetingDTO> getAllSortedMeetings(String email, LocalDate fromDate, int pageNum) {
        log.info("Getting meetings from email {}", email);
        PageRequest page = PageRequest.of(pageNum, 10, Sort.Direction.DESC, "changed");
        Page<Meeting> pageResult;
        if (fromDate == null)
            pageResult = meetingRepository.findAllByAccepted_Email(email, page);
        else
            pageResult = meetingRepository.findAllByAccepted_EmailAndStartAfter(email, fromDate.atStartOfDay(), page);

        List<MeetingDTO> dtos = pageResult.stream()
                .map(meeting -> modelMapper.map(meeting, MeetingDTO.class))
                .collect(Collectors.toList());
        log.info("Found {} meetings from email {}", pageResult.getTotalElements(), email);
        return new PageImpl<>(dtos, page, pageResult.getTotalElements());
    }

    public List<CustomerMeetingCounterDTO> getCustomerMeetingCount(String userMail) {
        return meetingRepository.findCustomerMeetingCount(userMail)
                .stream()
                .map(mCount -> modelMapper.map(mCount, CustomerMeetingCounterDTO.class))
                .collect(Collectors.toList());
    }
}
