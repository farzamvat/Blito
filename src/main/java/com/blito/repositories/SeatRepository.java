package com.blito.repositories;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat,Long> {
    List<Seat> findBySeatUidIn(List<String> uids);
}
