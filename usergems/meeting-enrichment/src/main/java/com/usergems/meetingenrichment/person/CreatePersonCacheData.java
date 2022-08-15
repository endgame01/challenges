package com.usergems.meetingenrichment.person;

import com.usergems.meetingenrichment.person.model.PersonDataCache;
import com.usergems.meetingenrichment.person.repository.PersonDataCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CreatePersonCacheData {

    @Autowired
    private PersonDataCacheRepository personDataCacheRepository;

    @EventListener
    @Transactional
    public void startCalendarData(ApplicationReadyEvent event) {

        PersonDataCache demi = new PersonDataCache("demi@algolia.com", "Demi", "Malnar", "https://www.someimagecdn.com/63ff29ad3.jpg",
                "GTM Chief of Staff", "https://www.linkedin.com/in/demimalnar", "Algolia",
                "https://www.linkedin.com/company/algolia", 700);
        PersonDataCache joshua = new PersonDataCache("joshua@algolia.com", "Joshua", "Mateer", "https://www.someimagecdn.com/63ff29ad4.jpg",
                "Sr Manager, Marketing Operations and Technology", "https://www.linkedin.com/in/joshua", "Algolia",
                "https://www.linkedin.com/company/algolia", 700);
        PersonDataCache woojin = new PersonDataCache("woojin@algolia.com", "Woojin", "Shin", "https://www.someimagecdn.com/63ff29ad5.jpg",
                "Manager, North America Business Development", "https://www.linkedin.com/in/woojin", "Algolia",
                "https://www.linkedin.com/company/algolia", 700);
        PersonDataCache aletta = new PersonDataCache("aletta@algolia.com", "Aletta", "Noujaim", "https://www.someimagecdn.com/63ff29ad6.jpg",
                "Director of Sales Development, EMEA & emerging markets", "https://www.linkedin.com/in/aletta", "Algolia",
                "https://www.linkedin.com/company/algolia", 700);

        personDataCacheRepository.saveAll(List.of(demi, joshua, woojin, aletta));
    }
}
