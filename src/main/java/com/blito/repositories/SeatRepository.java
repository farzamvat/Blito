package com.blito.repositories;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat,Long> {
}
