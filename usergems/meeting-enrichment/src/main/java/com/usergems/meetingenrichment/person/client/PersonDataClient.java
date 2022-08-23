package com.usergems.meetingenrichment.person.client;

import com.usergems.meetingenrichment.person.dto.PersonDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Paid external API
 */
@FeignClient(name = "PersonDataClient", url = "${base-api.url}")
public interface PersonDataClient {

    @GetMapping(value = "/person/{email}", consumes = "application/json", produces = "application/json")
    PersonDataDTO getPersonData(@PathVariable("email") String email,
                                @RequestHeader("Authorization") String authorization);
}
