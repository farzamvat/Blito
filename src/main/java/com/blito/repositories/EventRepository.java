package com.blito.repositories;

import com.blito.models.Event;
import com.blito.rest.viewmodels.event.EventTypeStatisticsViewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
	Long countByOperatorState(String operatorState);
	Page<Event> 
	findByIsDeletedFalseAndEventTypeIsAndEventStateNotAndOperatorStateIsOrderByOrderNumberDesc(String eventType,String eventState,String operatorState, Pageable pageable);

	Set<Event> findByOperatorStateIsAndEventStateNotAndIsDeletedFalse(String operatorState, String eventState);

	// Analytics
	@Query(value = "select new com.blito.rest.viewmodels.event.EventTypeStatisticsViewModel(e.eventType,avg(bt.capacity),avg(bt.price),sum(bt.soldCount),sum(bt.capacity),count(distinct e)) " +
			"from event as e join e.eventDates as ed join ed.blitTypes as bt " +
			"where e.operatorState =  :operatorState \n" +
			"group by e.eventType")
	List<EventTypeStatisticsViewModel> averageCapacityAndPriceGroupByEventType(@Param("operatorState") String operatorState);

}
