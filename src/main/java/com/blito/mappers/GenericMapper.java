package com.blito.mappers;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

public interface GenericMapper <E,V> {
	E createFromViewModel(V viewModel);
	V createFromEntity(E entity);
	E updateEntity(V viewModel,E entity);
	
	default List<V> createFromEntities(List<E> entities)
	{
		return entities.stream().map(this::createFromEntity).collect(Collectors.toList());
	}
	
	default List<E> createFromViewModels(List<V> viewModels)
	{
		return viewModels.stream().map(this::createFromViewModel).collect(Collectors.toList());
	}
	
	default Page<V> toPage(Page<E> page)
	{
		return page.map(this::createFromEntity);
	}
	
	default <T,U> List<U> toList(List<T> list,Function<T,U> function)
	{
		return list.stream().map(function::apply).collect(Collectors.toList());
	}
	
	default <T,U> Page<U> toPage(Page<T> page,Function<T,U> function)
	{
		return page.map(function::apply);
	}
	
}
