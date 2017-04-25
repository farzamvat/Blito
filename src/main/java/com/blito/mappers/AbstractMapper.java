package com.blito.mappers;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

public abstract class AbstractMapper {
	public <T,U> List<U> toList(List<T> list, Function<T,U> function)
	{
		return list.stream().map(element -> function.apply(element)).collect(Collectors.toList());
	}
	
	public <T,U> Page<U> toPage(Page<T> page, Function<T,U> function)
	{
		return page.map(element -> function.apply(element));
	}
}
