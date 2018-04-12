package com.blito.services;

import com.blito.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
public class MigrationService {
    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public void updateEventDates_And_BlitTypes_Uids() {
        eventRepository.findAll().forEach(event -> {
            event.getEventDates().forEach(eventDate -> {
                if(eventDate.getUid() == null || eventDate.getUid().isEmpty()) {
                    eventDate.setUid(UUID.randomUUID().toString());
                }
                eventDate.getBlitTypes().forEach(blitType -> {
                    if(blitType.getUid() == null || blitType.getUid().isEmpty()) {
                        blitType.setUid(UUID.randomUUID().toString());
                    }
                });
            });
        });
    }
}
