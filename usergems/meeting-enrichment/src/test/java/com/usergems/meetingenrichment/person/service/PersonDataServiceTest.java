package com.usergems.meetingenrichment.person.service;

import com.usergems.meetingenrichment.person.client.PersonDataClient;
import com.usergems.meetingenrichment.person.dto.PersonDataDTO;
import com.usergems.meetingenrichment.person.model.PersonDataCache;
import com.usergems.meetingenrichment.person.repository.PersonDataCacheRepository;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonDataServiceTest {

    @InjectMocks
    PersonDataService service;

    @Mock
    PersonDataCacheRepository personDataCacheRepository;

    @Mock
    PersonDataClient personDataClient;

    @Spy
    ModelMapper modelMapper;

    @Test
    void getPersonDataAndCache_whenCacheNotExpiredExists_returnValueFromCache() {
        var email = "found@email.com";
        PersonDataCache personDataCache = mock(PersonDataCache.class);
        doReturn(email).when(personDataCache).getEmail();
        doReturn(false).when(personDataCache).isExpired(anyInt());
        doReturn(Optional.of(personDataCache)).when(personDataCacheRepository).findById(email);

        PersonDataDTO result = service.getPersonData(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());

        Mockito.verifyNoInteractions(personDataClient);
    }

    @Test
    void getPersonDataAndCache_whenCacheExpiredExistsClientValidResult_returnValueFromClient() {
        var email = "found@email.com";
        PersonDataCache personDataCache = mock(PersonDataCache.class);
        doReturn(true).when(personDataCache).isExpired(anyInt());
        doReturn(Optional.of(personDataCache)).when(personDataCacheRepository).findById(email);

        PersonDataDTO expectedResultFromClient = mock(PersonDataDTO.class);
        doReturn(expectedResultFromClient).when(personDataClient).getPersonData(eq(email), any());

        PersonDataDTO result = service.getPersonData(email);

        assertEquals(expectedResultFromClient, result);
        verify(personDataCacheRepository).save(any(PersonDataCache.class));
    }

    @Test
    void getPersonDataAndCache_whenNoCacheAndNoClientResult_returnsNull() {
        var email = "not.found@email.com";
        doReturn(Optional.empty()).when(personDataCacheRepository).findById(email);

        doReturn(null).when(personDataClient).getPersonData(eq(email), any());

        PersonDataDTO result = service.getPersonData(email);

        assertNull(result);
        verify(personDataCacheRepository, never()).save(any(PersonDataCache.class));
    }

    @Test
    void getPersonDataAndCache_whenNoCacheAndNoClientException_returnsNull() {
        var email = "not.found@email.com";
        doReturn(Optional.empty()).when(personDataCacheRepository).findById(email);

        FeignException exe = mock(FeignException.class);
        doReturn("500").when(exe).getMessage();
        doThrow(exe).when(personDataClient).getPersonData(eq(email), any());

        PersonDataDTO result = service.getPersonData(email);

        assertNull(result);
        verify(personDataCacheRepository, never()).save(any(PersonDataCache.class));
    }
}
