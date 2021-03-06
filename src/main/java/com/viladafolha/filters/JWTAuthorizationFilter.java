package com.viladafolha.filters;

import com.viladafolha.controllers.service.InhabitantService;
import com.viladafolha.util.JWTUtil;
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
	private final InhabitantService inhabitantService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
                                  InhabitantService inhabitantService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.inhabitantService = inhabitantService;
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
			UserDetails user = inhabitantService.loadUserByUsername(email);
			return new UsernamePasswordAuthenticationToken(
					user.getUsername(), null, user.getAuthorities());
		}
		return null;
	}
}