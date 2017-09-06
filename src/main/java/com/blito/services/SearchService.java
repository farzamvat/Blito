package com.blito.services;

import com.blito.enums.Response;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.GenericMapper;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.search.AbstractSearchViewModel;
import com.blito.search.SearchServiceUtil;
import com.blito.search.SearchViewModel;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class SearchService {

    public <E, V, R extends JpaSpecificationExecutor<E> & JpaRepository<E, Long>> Page<V> search(
            SearchViewModel<E> searchViewModel, Pageable pageable, GenericMapper<E, V> mapper, R repository) {
        if (searchViewModel.getRestrictions().isEmpty())
            return mapper.toPage(repository.findAll(pageable));
        Page<E> searchResult = searchViewModel.getRestrictions().stream().map(AbstractSearchViewModel::action)
                .reduce((s1, s2) -> SearchServiceUtil.combineSpecifications(s1, s2, Optional.ofNullable(searchViewModel.getOperator())))
                .map(specification -> repository.findAll(specification, pageable))
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.SEARCH_UNSUCCESSFUL)));
        return new PageImpl<>(searchResult.getContent().stream().distinct().map(mapper::createFromEntity)
                .collect(Collectors.toList()), pageable, searchResult.getTotalElements());
    }

    public <E, V, R extends JpaSpecificationExecutor<E> & JpaRepository<E, Long>> Page<E> search(
            SearchViewModel<E> searchViewModel, Pageable pageable, R repository) {
        if (searchViewModel.getRestrictions().isEmpty())
            return repository.findAll(pageable);
        Page<E> searchResult = searchViewModel.getRestrictions().stream().map(AbstractSearchViewModel::action)
                .reduce((s1, s2) ->
                        SearchServiceUtil.combineSpecifications(s1, s2, Optional.ofNullable(searchViewModel.getOperator())))
                .map(specification -> repository.findAll(specification, pageable))
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.SEARCH_UNSUCCESSFUL)));
        return new PageImpl<>(searchResult.getContent().stream().distinct()
                .collect(Collectors.toList()), pageable, searchResult.getTotalElements());
    }

    public <E, V, R extends JpaSpecificationExecutor<E> & JpaRepository<E, Long>> Set<V> search(
            SearchViewModel<E> searchViewModel, GenericMapper<E, V> mapper, R repository) {
        if (searchViewModel.getRestrictions().isEmpty())
            return mapper.createFromEntities(new HashSet<>(repository.findAll()));
        List<E> searchResult = searchViewModel.getRestrictions().stream().map(AbstractSearchViewModel::action)
                .reduce((s1, s2) -> SearchServiceUtil.combineSpecifications(s1, s2, Optional.ofNullable(searchViewModel.getOperator())))
                .map(repository::findAll)
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.SEARCH_UNSUCCESSFUL)));
        return searchResult.stream().distinct().map(mapper::createFromEntity)
                .collect(Collectors.toSet());
    }


}