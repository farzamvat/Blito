package com.blito.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.blito.enums.EventType;
import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.models.Event;

public interface EventRepository
		extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event>, PagingAndSortingRepository<Event, Long> {
	Optional<Event> findByEventLinkAndIsDeletedFalse(String link);
	Page<Event> findByEventTypeAndIsDeletedFalse(String type,Pageable pageable);
	Page<Event> findByEventStateOrEventStateOrderByCreatedAtDesc(String state,String secondState,Pageable pageable);
	Page<Event> findByEventHostUserUserIdAndIsDeletedFalse(long userId,Pageable pageable);
	Page<Event> findByOperatorStateAndIsDeletedFalse(String operatorState, Pageable pageable);
	Optional<Event> findByEventIdAndIsDeletedFalse(long eventId);
	Page<Event> findByIsDeletedFalse(Pageable pageable);
	Set<Event> findByIsDeletedFalse();	
	Page<Event> 
	findByIsDeletedFalseAndEventTypeIsAndEventStateNotAndOperatorStateIsOrderByOrderNumberDesc(String eventType,String eventState,String operatorState, Pageable pageable);

	Set<Event> findByOperatorStateIsAndEventStateNotAndIsDeletedFalse(String operatorState, String eventState);

}
