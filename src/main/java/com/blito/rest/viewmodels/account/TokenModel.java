package com.blito.rest.viewmodels.account;

public class TokenModel {
	String accessToken;
	String refreshToken;
	Long accessTokenExipreTime;
	Long refreshtokenExpireTime;
	String tokenType = "Bearer";
	boolean isFirstTime;
	
	public boolean isFirstTime() {
		return isFirstTime;
	}
	public void setFirstTime(boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	
	public Long getAccessTokenExipreTime() {
		return accessTokenExipreTime;
	}
	public void setAccessTokenExipreTime(Long accessTokenExipreTime) {
		this.accessTokenExipreTime = accessTokenExipreTime;
	}
	public Long getRefreshtokenExpireTime() {
		return refreshtokenExpireTime;
	}
	public void setRefreshtokenExpireTime(Long refreshtokenExpireTime) {
		this.refreshtokenExpireTime = refreshtokenExpireTime;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
}
