package com.usergems.meetingenrichment.calendar.client;

import com.usergems.meetingenrichment.calendar.dto.MeetingsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "CalendarClient", url = "${base-api.url}")
public interface CalendarClient {

    @GetMapping("/events")
    MeetingsDTO findLatestUserMeetings(@RequestHeader("Authorization") String key,
                                       @RequestParam("page") int page);
}
