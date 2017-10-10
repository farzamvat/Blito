package com.blito.repositories;

import com.blito.models.BlitTypeSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Farzam Vatanzadeh
 * 10/10/17
 * Mailto : farzam.vat@gmail.com
 **/

public interface BlitTypeSeatRepository extends JpaRepository<BlitTypeSeat,Long> {
    List<BlitTypeSeat> findBySeatSeatUidIn(List<String> seatUids);
}
