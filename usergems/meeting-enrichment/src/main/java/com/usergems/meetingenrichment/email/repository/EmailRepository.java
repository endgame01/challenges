package com.usergems.meetingenrichment.email.repository;

import com.usergems.meetingenrichment.email.model.Email;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmailRepository extends CrudRepository<Email, Long> {
    List<Email> findAllBySentAtIsNull();
}
