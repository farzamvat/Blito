package com.blito.repositories;

import java.util.List;
import java.util.Optional;

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
	Optional<Event> findByEventLink(String link);
	List<Event> findByEventType(EventType type,Pageable pageable);
	Page<Event> findByEventStateOrEventStateOrderByCreatedAtDesc(State state,State secondState,Pageable pageable);
	List<Event> findByEventHostUserUserIdAndIsDeletedFalse(long userId);
	Page<Event> findByOperatorState(OperatorState operatorState, Pageable pageable);
}
