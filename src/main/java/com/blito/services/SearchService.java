package com.blito.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import com.blito.enums.Response;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.GenericMapper;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.search.SearchViewModel;

@Service
@Scope("prototype")
public class SearchService {

	public <E, V, R extends JpaSpecificationExecutor<E> & JpaRepository<E, Long>> Page<V> search(
			SearchViewModel<E> searchViewModel, Pageable pageable, GenericMapper<E, V> mapper, R repository) {
		if (searchViewModel.getRestrictions().isEmpty())
			return mapper.toPage(repository.findAll(pageable));
		Page<E> searchResult = searchViewModel.getRestrictions().stream().map(r -> r.action())
				.reduce((s1, s2) -> Specifications.where(s1).and(s2))
				.map(specification -> repository.findAll(specification, pageable))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.SEARCH_UNSUCCESSFUL)));
		return new PageImpl<>(searchResult.getContent().stream().distinct().map(mapper::createFromEntity)
				.collect(Collectors.toList()), pageable, searchResult.getTotalElements());
	}
	
	public <E, V, R extends JpaSpecificationExecutor<E> & JpaRepository<E, Long>> Page<E> search(
			SearchViewModel<E> searchViewModel, Pageable pageable, R repository) {
		if (searchViewModel.getRestrictions().isEmpty())
			return repository.findAll(pageable);
		Page<E> searchResult = searchViewModel.getRestrictions().stream().map(r -> r.action())
				.reduce((s1, s2) ->
				Specifications.where(s1).and(s2))
				.map(specification -> repository.findAll(specification, pageable))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.SEARCH_UNSUCCESSFUL)));
		return new PageImpl<>(searchResult.getContent().stream().distinct()
				.collect(Collectors.toList()), pageable, searchResult.getTotalElements());
	}
	
	public <E, V, R extends JpaSpecificationExecutor<E> & JpaRepository<E, Long>> Set<V> search(
			SearchViewModel<E> searchViewModel, GenericMapper<E, V> mapper, R repository) {
		if (searchViewModel.getRestrictions().isEmpty())
			return mapper.createFromEntities(repository.findAll().stream().collect(Collectors.toSet()));
		List<E> searchResult = searchViewModel.getRestrictions().stream().map(r -> r.action())
				.reduce((s1, s2) -> 
				Specifications.where(s1).and(s2))
				.map(specification -> repository.findAll(specification))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.SEARCH_UNSUCCESSFUL)));
		return searchResult.stream().distinct().map(mapper::createFromEntity)
				.collect(Collectors.toSet());
	}
}