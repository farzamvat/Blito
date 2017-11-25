package com.blito.repositories;

import com.blito.models.BlitTypeSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Farzam Vatanzadeh
 * 10/10/17
 * Mailto : farzam.vat@gmail.com
 **/

public interface BlitTypeSeatRepository extends JpaRepository<BlitTypeSeat,Long> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    Set<BlitTypeSeat> findBySeatSeatUidInAndBlitTypeEventDateEventDateId(Set<String> uids,Long eventDateId);
    Optional<BlitTypeSeat> findBySeatSeatUidAndBlitTypeEventDateEventDateIdIs(String seatUid, Long eventDateId);
    @Transactional
    void deleteByBlitTypeBlitTypeIdAndStateNotIn(Long blitTypeId,List<String> states);
    Set<BlitTypeSeat> findByBlitTypeBlitTypeIdAndStateIs(Long blitTypeId, String state);
    int countByBlitTypeEventDateEventDateIdAndSeatBlitCustomerEmail(Long blitTypeId,String customerEmail);
}
