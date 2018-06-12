package com.blito.repositories;

import com.blito.enums.OperatorState;
import com.blito.models.Event;
import com.blito.models.EventHost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Optional;

public interface EventHostRepository extends JpaRepository<EventHost,Long>, JpaSpecificationExecutor<EventHost>  {
	Optional<EventHost> findByHostNameAndIsDeletedFalse(String hostName);
	Page<EventHost> findByUserUserIdAndIsDeletedFalse(long userId,Pageable pagable);
	Optional<EventHost> findByEventHostIdAndIsDeletedFalse(long eventHostId);
	Page<EventHost> findByIsDeletedFalse(Pageable pageable);
	Optional<EventHost> findByEventHostLinkAndIsDeletedFalse(String link);
	Specification<EventHost> orderByCountOfApprovedEvents =
			(root, query, cb) -> {
				Subquery<Event> eventSubquery = query.subquery(Event.class);
				Root<Event> subqueryRoot = eventSubquery.from(Event.class);
				query.groupBy(root.get("hostName"))
						.orderBy(cb.desc(cb.count(root.get("eventHostId"))));
				eventSubquery.select(subqueryRoot)
						.where(cb.equal(subqueryRoot.get("isPrivate"),false))
						.where(cb.equal(subqueryRoot.get("operatorState"), OperatorState.APPROVED.name()));
				return cb.exists(eventSubquery);
			};
}
