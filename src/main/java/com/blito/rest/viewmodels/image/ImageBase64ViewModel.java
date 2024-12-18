package com.blito.rest.viewmodels.image;

import com.blito.rest.viewmodels.View;
import com.fasterxml.jackson.annotation.JsonView;

public class ImageBase64ViewModel {
	@JsonView(View.DefaultView.class)
	private String encodedBase64;

	public ImageBase64ViewModel(String encodedBase64) {
		this.encodedBase64 = encodedBase64;
	}

	public ImageBase64ViewModel() { }

	public String getEncodedBase64() {
		return encodedBase64;
	}

	public void setEncodedBase64(String encodedBase64) {
		this.encodedBase64 = encodedBase64;
	}
	
}
