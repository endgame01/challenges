package com.usergems.meetingenrichment.calendar.controller;

import com.usergems.meetingenrichment.calendar.dto.CustomerMeetingCounterDTO;
import com.usergems.meetingenrichment.calendar.dto.MeetingDTO;
import com.usergems.meetingenrichment.calendar.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @GetMapping("/events")
    public ResponseEntity<Page<MeetingDTO>> findLatestUserMeetings(@RequestHeader("Authorization") String key,
                                                                   @RequestParam(required = false)
                                                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                   Pageable pageable) {
        String userEmail = calendarService.getEmailFromToken(key);
        if (userEmail.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(calendarService.getAllSortedMeetings(userEmail, fromDate, pageable.getPageNumber()));
    }

    /**
     * With time, evolve this to receive the list of customer emails not to return every possible customer email count
     */
    @GetMapping("/events/count")
    public ResponseEntity<List<CustomerMeetingCounterDTO>> countUserMeetings(@RequestHeader("Authorization") String key) {
        String userEmail = calendarService.getEmailFromToken(key);
        if (userEmail.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(calendarService.getCustomerMeetingCount(userEmail));
    }
}
