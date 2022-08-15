package com.usergems.meetingenrichment.person.service;

import com.usergems.meetingenrichment.person.client.PersonDataClient;
import com.usergems.meetingenrichment.person.dto.PersonDataDTO;
import com.usergems.meetingenrichment.person.model.PersonDataCache;
import com.usergems.meetingenrichment.person.repository.PersonDataCacheRepository;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PersonDataService {

    private static final Logger log = LoggerFactory.getLogger(PersonDataService.class);

    @Autowired
    private PersonDataCacheRepository personDataCacheRepository;

    @Autowired
    private PersonDataClient personDataClient;

    @Autowired
    private ModelMapper modelMapper;

    @Value(value = "${person-data-api.authorization}")
    private String authorization;

    @Value(value = "${person-data-api.cache-days:7}")
    private int cacheInDays;

    public PersonDataDTO getPersonData(String email) {
        PersonDataDTO fromCache = getFromCache(email);

        if (fromCache != null) {
            log.debug("Person data found in cache. - {}", email);
            return fromCache;
        }

        return getFromClientAndSaveCache(email);
    }

    /**
     * Using H2 to cache for quick solution, but ideally would use redis/nosql cache with ttl on data
     */
    private PersonDataDTO getFromCache(String email) {
        return personDataCacheRepository.findById(email)
                .filter(cache -> !cache.isExpired(cacheInDays))
                .map(cache -> modelMapper.map(cache, PersonDataDTO.class))
                .orElse(null);
    }

    private PersonDataDTO getFromClientAndSaveCache(String email) {
        PersonDataDTO fromClient = getFromClient(email);

        if (fromClient != null) {
            log.debug("Saving person data on cache - {}", email);
            fromClient.setEmail(email);
            personDataCacheRepository.save(modelMapper.map(fromClient, PersonDataCache.class));
        }
        return fromClient;
    }

    private PersonDataDTO getFromClient(String email) {
        try {
            log.debug("Fetching person data from API - {}", email);
            return personDataClient.getPersonData(email, authorization);
        } catch (FeignException exe) {
            log.error("exception fetching person data from api {}", exe.getMessage(), exe);
        }
        return null;
    }

}
