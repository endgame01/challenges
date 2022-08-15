package com.usergems.meetingenrichment.email.client;

import com.usergems.meetingenrichment.email.dto.PersonDataInternalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "PersonDataInternalClient", url = "${person-date-internal-api.url}")
public interface PersonDataInternalClient {
    @GetMapping("/person/{email}")
    PersonDataInternalDTO getPersonByEmail(@PathVariable("email") String email);
}
