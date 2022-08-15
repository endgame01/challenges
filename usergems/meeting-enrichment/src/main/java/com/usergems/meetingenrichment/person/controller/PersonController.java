package com.usergems.meetingenrichment.person.controller;

import com.usergems.meetingenrichment.person.dto.PersonDataDTO;
import com.usergems.meetingenrichment.person.service.PersonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    @Autowired
    private PersonDataService personDataService;

    @GetMapping("/person/{email}")
    public ResponseEntity<PersonDataDTO> getPersonByEmail(@PathVariable("email") String email) {
        PersonDataDTO personData = personDataService.getPersonData(email);

        if (personData == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(personData);
    }
}
