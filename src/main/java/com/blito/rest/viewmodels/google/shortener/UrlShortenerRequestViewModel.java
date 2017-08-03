package com.blito.rest.viewmodels.google.shortener;

public class UrlShortenerRequestViewModel {
	private String longUrl;
	
	public UrlShortenerRequestViewModel(String url)
	{
		longUrl = url;
	}
	
	public UrlShortenerRequestViewModel() {}

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}
	
}
