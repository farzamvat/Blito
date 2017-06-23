package com.blito.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
	Page<Event> findByEventTypeAndIsDeletedFalse(EventType type,Pageable pageable);
	Page<Event> findByEventStateOrEventStateOrderByCreatedAtDesc(State state,State secondState,Pageable pageable);
	Page<Event> findByEventHostUserUserIdAndIsDeletedFalse(long userId,Pageable pageable);
	Page<Event> findByOperatorStateAndIsDeletedFalse(OperatorState operatorState, Pageable pageable);
	Optional<Event> findByEventIdAndIsDeletedFalse(long eventId);
	Page<Event> findByIsDeletedFalse(Pageable pageable);
	List<Event> findByIsDeletedFalse();	
}