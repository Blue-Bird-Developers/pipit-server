package com.bluebird.pipit.security;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bluebird.pipit.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class UserPrincipal implements UserDetails {
	private final Long id;

	private final String pipitId;

	@JsonIgnore
	private final String pipitPassword;

	private Collection<? extends GrantedAuthority> authorities;

	public UserPrincipal(long id, String pipitId, String pipitPassword,
						 Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.pipitId = pipitId;
		this.pipitPassword = pipitPassword;
		this.authorities = authorities;
	}

	public static UserPrincipal create(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
			new SimpleGrantedAuthority(role.getName().name())
		).collect(Collectors.toList());

		return new UserPrincipal(
			user.getId(),
			user.getPipitId(),
			user.getPipitPassword(),
			authorities
		);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return pipitPassword;
	}

	@Override
	public String getUsername() {
		return pipitId;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserPrincipal that = (UserPrincipal) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id);
	}
}
