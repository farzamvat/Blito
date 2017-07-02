package com.blito.mappers;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

public interface GenericMapper <E,V> {
	E createFromViewModel(V viewModel);
	V createFromEntity(E entity);
	E updateEntity(V viewModel,E entity);
	
	default Set<V> createFromEntities(Set<E> entities)
	{
		return entities.stream().map(this::createFromEntity).collect(Collectors.toSet());
	}
	
	default Set<E> createFromViewModels(Set<V> viewModels)
	{
		return viewModels.stream().map(this::createFromViewModel).collect(Collectors.toSet());
	}
	
	default Page<V> toPage(Page<E> page)
	{
		return page.map(this::createFromEntity);
	}
	
	default <T,U> List<U> toList(List<T> list,Function<T,U> function)
	{
		return list.stream().map(function::apply).collect(Collectors.toList());
	}
	
	default <T,U> Set<U> toSet(Set<T> set,Function<T,U> function)
	{
		return set.stream().map(function::apply).collect(Collectors.toSet());
	}
	
	default <T,U> Page<U> toPage(Page<T> page,Function<T,U> function)
	{
		return page.map(function::apply);
	}
	
}
