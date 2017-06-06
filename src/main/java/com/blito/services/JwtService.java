package com.blito.services;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.blito.enums.Response;
import com.blito.exceptions.UnauthorizedException;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.account.TokenModel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
	@Value("${token.secret}")
	String tokenSecret;

	@Value("${fifteenMins.accessToken}")
	private long accessTokenMilliSeconds;
	@Value("${thirtyDays.refreshToken}")
	private long refreshTokenMilliSeconds;

	

	public CompletableFuture<TokenModel> generateToken(Long userid) {
		TokenModel tokenModel = new TokenModel();
		Long expire = System.currentTimeMillis();
		tokenModel.setAccessTokenExipreTime(expire + accessTokenMilliSeconds);
		tokenModel.setRefreshtokenExpireTime(expire + refreshTokenMilliSeconds);

		return CompletableFuture.supplyAsync(() -> {
			return generateJwtToken(userid, expire + accessTokenMilliSeconds);
		}).thenCombine(CompletableFuture.supplyAsync(() -> {
			return generateJwtToken(userid, expire + refreshTokenMilliSeconds);
		}), (accessToken, refreshToken) -> {
			tokenModel.setAccessToken(accessToken);
			tokenModel.setRefreshToken(refreshToken);
			return tokenModel;
		});
	}
	
	public CompletableFuture<TokenModel> generateAccessToken(Long userId)
	{
		TokenModel tokenModel = new TokenModel();
		Long expire = System.currentTimeMillis();
		tokenModel.setAccessTokenExipreTime(expire + accessTokenMilliSeconds);
		
		return CompletableFuture.supplyAsync(() -> {
			return generateJwtToken(userId, expire + accessTokenMilliSeconds); 
		}).thenApply(accessToken -> {
			tokenModel.setAccessToken(accessToken);
			return tokenModel;
		});
	}
	
	public long refreshTokenValidation(String refresh_token)
	{
		try {
			Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(refresh_token).getBody();
			return Long.parseLong(claims.getSubject());
		} catch(Exception e)
		{
			throw new UnauthorizedException(ResourceUtil.getMessage(Response.ACCESS_DENIED));
		}
	}

	private String generateJwtToken(Long userid, Long expireDate) {
		String generatedToken = Jwts.builder().setSubject(String.valueOf(userid))
				.signWith(SignatureAlgorithm.HS256, tokenSecret).setExpiration(new Date(expireDate)).compact();
		return generatedToken;
	}
}
