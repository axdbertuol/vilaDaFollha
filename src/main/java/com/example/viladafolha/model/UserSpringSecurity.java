package com.example.viladafolha.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UserSpringSecurity implements UserDetails {
    @Serial
	private static final long serialVersionUID = 1L;

	private final String email;
	private final String password;
	private final Collection<? extends GrantedAuthority> authorities;

	public UserSpringSecurity(String email, String password, Collection<? extends GrantedAuthority> authorities) {
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}

	public UserSpringSecurity(String email, String password, Set<String> authorities) {
		this.email = email;
		this.password = password;
		this.authorities = authorities.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet());;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
