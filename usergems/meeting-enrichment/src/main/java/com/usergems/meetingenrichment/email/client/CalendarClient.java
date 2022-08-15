package com.usergems.meetingenrichment.email.client;

import com.usergems.meetingenrichment.calendar.dto.CustomerMeetingCounterDTO;
import com.usergems.meetingenrichment.email.dto.CalendarMeetingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;


@FeignClient(name = "CalendarClient", url = "${calendar-api.url}")
public interface CalendarClient {

    @GetMapping("/events")
    Page<CalendarMeetingDTO> findLatestUserMeetings(@RequestHeader("Authorization") String key,
                                                    @RequestParam(required = false)
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                    Pageable pageable);

    @GetMapping("/events/count")
    List<CustomerMeetingCounterDTO> countUserMeetings(@RequestHeader("Authorization") String key);
}
