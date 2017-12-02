package com.blito.services;

import com.blito.configs.Constants;
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
    public Set<BlitTypeSeat> createBlitTypeSeats(BlitTypeViewModel viewModel, BlitType blitType,BlitTypeSeatState blitTypeSeatState) {
        List<Seat> seats = seatRepository.findBySeatUidIn(new ArrayList<>(viewModel.getSeatUids()));
        blitType.setCapacity(seats.size());
        if(seats.size() == 0 || seats.size() != viewModel.getSeatUids().size()) {
            throw new NotFoundException("seats not found");
        }
        return seats.stream().map(seat -> new BlitTypeSeat(blitTypeSeatState.name(),seat,blitType))
                .collect(Collectors.toSet());
    }

    @Transactional
    public Set<BlitTypeSeat> updateBlitTypeSeats(BlitTypeViewModel viewModel,BlitType blitType,BlitTypeSeatState blitTypeSeatState) {

        Set<BlitTypeSeat> availableBlitTypeSeats =
                blitType.getName().equals(Constants.HOST_RESERVED_SEATS) ? blitTypeSeatRepository.findByBlitTypeBlitTypeIdAndStateIs(blitType.getBlitTypeId(),
                        BlitTypeSeatState.NOT_AVAILABLE.name()) :
                        blitTypeSeatRepository.findByBlitTypeBlitTypeIdAndStateIs(blitType.getBlitTypeId(),
                                BlitTypeSeatState.AVAILABLE.name());
        Set<Long> shouldDeleteBlitTypeSeats =
                availableBlitTypeSeats.stream().filter(blitTypeSeat -> !viewModel.getSeatUids().contains(blitTypeSeat.getSeat().getSeatUid()))
                        .map(BlitTypeSeat::getBlitTypeSeatId)
                        .collect(Collectors.toSet());
        blitType.getBlitTypeSeats().removeIf(blitTypeSeat -> shouldDeleteBlitTypeSeats.contains(blitTypeSeat.getBlitTypeSeatId()));

        seatRepository.findBySeatUidIn(
                viewModel.getSeatUids().stream()
                        .filter(uid ->
                                !blitType.getBlitTypeSeats().stream().map(blitTypeSeat -> blitTypeSeat.getSeat().getSeatUid()).collect(Collectors.toSet()).contains(uid))
                        .collect(Collectors.toList())
        ).stream().map(seat -> new BlitTypeSeat(blitTypeSeatState.name(),seat,blitType))
                .forEach(blitTypeSeat -> blitType.getBlitTypeSeats().add(blitTypeSeat));
        blitType.setCapacity(blitType.getBlitTypeSeats().size());
        return blitType.getBlitTypeSeats();
    }
}
