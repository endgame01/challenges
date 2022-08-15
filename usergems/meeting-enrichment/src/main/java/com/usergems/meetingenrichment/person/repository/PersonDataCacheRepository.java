package com.usergems.meetingenrichment.person.repository;

import com.usergems.meetingenrichment.person.model.PersonDataCache;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonDataCacheRepository extends JpaRepository<PersonDataCache, String> {
}
