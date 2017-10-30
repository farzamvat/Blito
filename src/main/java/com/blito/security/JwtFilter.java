package com.blito.security;

import com.blito.configs.Constants;
import com.blito.models.User;
import com.blito.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

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
			servletResponse.setStatus(401);
			return;
		}
		final String token = authHeader.substring(7);
		try {
			final Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
			if(claims.getAudience().equals(Constants.REFRESH_TOKEN_AUDIENCE)) {
				servletResponse.setStatus(401);
				return;
			}
			Optional<User> currentUser = userRepository.findByEmail(claims.getSubject());
			if (currentUser.isPresent()) {
				SecurityContextHolder.setCurrentUser(currentUser.get());
			} else {
				servletResponse.setStatus(401);
				return;
			}

		} catch (final Exception e) {
			servletResponse.setStatus(401);
			return;
		}
		chain.doFilter(request, response);

		SecurityContextHolder.removeCurrentUser();
	}

}
