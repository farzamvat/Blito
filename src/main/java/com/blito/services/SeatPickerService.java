package com.blito.services;

import com.blito.enums.BlitTypeSeatState;
import com.blito.exceptions.NotFoundException;
import com.blito.models.BlitType;
import com.blito.models.BlitTypeSeat;
import com.blito.models.Seat;
import com.blito.repositories.BlitTypeSeatRepository;
import com.blito.repositories.SeatRepository;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
    @author Farzam Vatanzadeh
**/
@Service
public class SeatPickerService {

    private SeatRepository seatRepository;
    private BlitTypeSeatRepository blitTypeSeatRepository;

    @Autowired
    public void setBlitTypeSeatRepository(BlitTypeSeatRepository blitTypeSeatRepository) {
        this.blitTypeSeatRepository = blitTypeSeatRepository;
    }

    @Autowired
    public void setSeatRepository(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Transactional
    public Set<BlitTypeSeat> createBlitTypeSeats(BlitTypeViewModel viewModel, BlitType blitType) {
        List<Seat> seats = seatRepository.findBySeatUidIn(new ArrayList<>(viewModel.getSeatUids()));
        if(seats.size() == 0 || seats.size() != viewModel.getSeatUids().size()) {
            throw new NotFoundException("seats not found");
        }
        return seats.stream().map(seat -> new BlitTypeSeat(BlitTypeSeatState.AVAILABLE.name(),seat,blitType))
                .collect(Collectors.toSet());
    }

    @Transactional
    public Set<BlitTypeSeat> updateBlitTypeSeats(BlitTypeViewModel viewModel,BlitType blitType) {
        List<BlitTypeSeat> shouldUpdate = blitTypeSeatRepository.findBySeatSeatUidIn(new ArrayList<>(viewModel.getSeatUids()));
        List<String> shouldUpdateUids = shouldUpdate.stream().map(blitTypeSeat -> blitTypeSeat.getSeat().getSeatUid()).collect(Collectors.toList());
        // Update existing blit type seats
        shouldUpdate.forEach(blitTypeSeat -> {
            if(!blitTypeSeat.getState().equals(BlitTypeSeatState.AVAILABLE.name())) {
                throw new RuntimeException("this seat is not available");
            }
            blitTypeSeat.setBlitType(blitType);
        });
        // Remove non existing blit type seats
        blitType.getBlitTypeSeats().stream()
                .filter(blitTypeSeat -> !viewModel.getSeatUids().contains(blitTypeSeat.getSeat().getSeatUid()))
                .forEach(blitTypeSeat -> blitType.getBlitTypeSeats().removeIf(b -> b.getSeat().getSeatUid().equals(blitTypeSeat.getSeat().getSeatUid())));

        // Add new blit type seats
        seatRepository.findBySeatUidIn(viewModel.getSeatUids().stream().filter(uid -> !shouldUpdateUids.contains(uid)).collect(Collectors.toList())).forEach(seat -> new BlitTypeSeat(BlitTypeSeatState.AVAILABLE.name(),seat,blitType));
        return blitType.getBlitTypeSeats();
    }
}
