package com.blito.repositories;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.models.Salon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalonRepository extends JpaRepository<Salon,Long> {
    Optional<Salon> findByName(String name);
    Optional<Salon> findByPlanPath(String planPathName);
    Optional<Salon> findBySalonUid(String salonUid);
}
