package com.blito.security;

import java.util.ArrayList;
import java.util.List;

public class PathBuilder {
	
	private ArrayList<String> paths;
	private static PathBuilder _instance;
	private String baseUrl;
	private PathBuilder()
	{
		paths = new ArrayList<>();
	}
	public static PathBuilder build()
	{
		if(_instance == null)
		{
			_instance = new PathBuilder();
		}
		return _instance;
	}
	public PathBuilder setBaseUrl(String baseUrl)
	{
		this.baseUrl = baseUrl;
		return _instance;
	}
	public PathBuilder addMatcher(String urlPattern)
	{
		_instance.paths.add(this.baseUrl + urlPattern);
		return _instance;
	}
	public List<String> getUrlPatterns()
	{
		return paths;
	}
}
