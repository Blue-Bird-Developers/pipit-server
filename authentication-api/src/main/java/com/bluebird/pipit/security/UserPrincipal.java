package com.bluebird.pipit.security;

import com.bluebird.pipit.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class UserPrincipal implements UserDetails {
	private final Long id;

	private final String pipitId;

	private final String displayName;

	@JsonIgnore
	private final String pipitPassword;

	private Collection<? extends GrantedAuthority> authorities;

	public UserPrincipal(Long id, String pipitId, String displayName, String pipitPassword,
						 Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.pipitId = pipitId;
		this.displayName = displayName;
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
			user.getDisplayName(),
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
		return displayName;
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
