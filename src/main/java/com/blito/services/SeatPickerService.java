package com.blito.services;

import com.blito.models.Event;
import com.blito.rest.viewmodels.event.EventViewModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
    @author Farzam Vatanzadeh
*/
@Service
public class SeatPickerService {

    @Transactional
    public void createBlitTypeSeats(Event event, EventViewModel eventViewModel) {

    }
}
