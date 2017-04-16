package com.blito.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import com.blito.models.User;
import com.blito.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtFilter extends GenericFilterBean {
	
	String tokenSecret;
	UserRepository userRepository;
	
	public JwtFilter(UserRepository userRepository,String tokenSecret)
	{
		this.userRepository = userRepository;
		this.tokenSecret = tokenSecret;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		final HttpServletRequest servletRequest = (HttpServletRequest) request;
		final HttpServletResponse servletResponse = (HttpServletResponse) response;
		final String authHeader = servletRequest.getHeader("X-AUTH-TOKEN");
		if (authHeader == null || !authHeader.startsWith("Bearer")) {
			servletResponse.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid token syntax");
			return;
		}
		final String token = authHeader.substring(7);
		try {
			final Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
			Optional<User> currentUser = Optional
					.ofNullable(userRepository.findOne(Long.parseLong(claims.getSubject())));
			if (currentUser.isPresent()) {
				SecurityContextHolder.setCurrentUser(currentUser.get());
			} else {
				servletResponse.sendError(HttpStatus.BAD_REQUEST.value(), "User not found");
				return;
			}

		} catch (final Exception e) {
			servletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
			return;
		}
		chain.doFilter(request, response);

		SecurityContextHolder.removeCurrentUser();
	}

}
