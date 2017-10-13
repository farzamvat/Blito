package com.blito.repositories;

import com.blito.models.BlitTypeSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author Farzam Vatanzadeh
 * 10/10/17
 * Mailto : farzam.vat@gmail.com
 **/

public interface BlitTypeSeatRepository extends JpaRepository<BlitTypeSeat,Long> {
    List<BlitTypeSeat> findBySeatSeatUidIn(List<String> seatUids);
    @Transactional
    void deleteByBlitTypeBlitTypeIdAndStateNotIn(Long blitTypeId,List<String> states);
    Set<BlitTypeSeat> findByBlitTypeBlitTypeIdAndStateNotIn(Long blitTypeId, List<String> states);
}
