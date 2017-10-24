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
    Set<BlitTypeSeat> findBySeatSeatUidInAndBlitTypeEventDateEventDateId(Set<String> uids,Long eventDateId);
    @Transactional
    void deleteByBlitTypeBlitTypeIdAndStateNotIn(Long blitTypeId,List<String> states);
    Set<BlitTypeSeat> findByBlitTypeBlitTypeIdAndStateIs(Long blitTypeId, String state);
    int countByBlitTypeBlitTypeIdAndSeatBlitCustomerEmail(Long blitTypeId,String customerEmail);
}
