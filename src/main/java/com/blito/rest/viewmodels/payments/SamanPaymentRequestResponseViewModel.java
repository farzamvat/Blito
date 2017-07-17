package com.blito.rest.viewmodels.payments;

public class SamanPaymentRequestResponseViewModel extends PaymentRequestViewModel {
	String Token;
	String RedirectURL;
	
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	public String getRedirectURL() {
		return RedirectURL;
	}
	public void setRedirectURL(String redirectURL) {
		RedirectURL = redirectURL;
	}
}
