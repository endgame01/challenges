package com.usergems.meetingenrichment.person.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonDataCacheTest {

    @ParameterizedTest
    @CsvSource(value = {"true,10,7", "true,5,1", "false,5,7"})
    void isExpired(boolean expectedResult, int lastUpdatedDay, int expireInDays) {
        PersonDataCache personDataCache = new PersonDataCache();

        LocalDateTime now = LocalDateTime.now();
        personDataCache.setUpdateDateTime(now.minusDays(lastUpdatedDay));

        boolean result = personDataCache.isExpired(expireInDays);

        assertEquals(expectedResult, result);
    }
}
