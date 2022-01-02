package com.example.viladafolha.filters;

import com.example.viladafolha.controllers.service.UserService;
import com.example.viladafolha.util.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private final JWTUtil jwtUtil;
	private final UserService userService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
                                  UserService userService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getAuthentication(
					header.substring(7));
			if (usernamePasswordAuthenticationToken != null) {
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (jwtUtil.validateToken(token)) {
			String email = jwtUtil.getEmailByToken(token);
			UserDetails user = userService.loadUserByUsername(email);
			return new UsernamePasswordAuthenticationToken(
					user.getUsername(), null, user.getAuthorities());
		}
		return null;
	}
}