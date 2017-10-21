package com.blito.services;

import com.blito.enums.Response;
import com.blito.exceptions.UnauthorizedException;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.account.TokenModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
public class JwtService {
	@Value("${token.secret}")
	String tokenSecret;

	@Value("${fifteenMins.accessToken}")
	private long accessTokenMilliSeconds;
	@Value("${thirtyDays.refreshToken}")
	private long refreshTokenMilliSeconds;

	

	public CompletableFuture<TokenModel> generateToken(String email) {
		TokenModel tokenModel = new TokenModel();
		Long expire = System.currentTimeMillis();
		tokenModel.setAccessTokenExipreTime(expire + accessTokenMilliSeconds);
		tokenModel.setRefreshtokenExpireTime(expire + refreshTokenMilliSeconds);

		return CompletableFuture.supplyAsync(() -> {
			return generateJwtToken(email, expire + accessTokenMilliSeconds);
		}).thenCombine(CompletableFuture.supplyAsync(() -> {
			return generateJwtToken(email, expire + refreshTokenMilliSeconds);
		}), (accessToken, refreshToken) -> {
			tokenModel.setAccessToken(accessToken);
			tokenModel.setRefreshToken(refreshToken);
			return tokenModel;
		});
	}
	
	public CompletableFuture<TokenModel> generateAccessToken(String email)
	{
		TokenModel tokenModel = new TokenModel();
		Long expire = System.currentTimeMillis();
		tokenModel.setAccessTokenExipreTime(expire + accessTokenMilliSeconds);
		
		return CompletableFuture.supplyAsync(() -> {
			return generateJwtToken(email, expire + accessTokenMilliSeconds); 
		}).thenApply(accessToken -> {
			tokenModel.setAccessToken(accessToken);
			return tokenModel;
		});
	}
	
	public String refreshTokenValidation(String refresh_token)
	{
		try {
			Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(refresh_token).getBody();
			return claims.getSubject();
		} catch(Exception e)
		{
			throw new UnauthorizedException(ResourceUtil.getMessage(Response.ACCESS_DENIED));
		}
	}

	private String generateJwtToken(String email, Long expireDate) {
		String generatedToken = Jwts.builder().setSubject(email)
				.signWith(SignatureAlgorithm.HS256, tokenSecret).setExpiration(new Date(expireDate)).compact();
		return generatedToken;
	}
}
