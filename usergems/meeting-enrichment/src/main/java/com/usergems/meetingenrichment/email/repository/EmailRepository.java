package com.usergems.meetingenrichment.email.repository;

import com.usergems.meetingenrichment.email.model.Email;
import org.springframework.data.repository.CrudRepository;

public interface EmailRepository extends CrudRepository<Email, Long> {
}
