package com.bluebird.pipit.user.domain;

import com.bluebird.pipit.security.Role;
import com.bluebird.pipit.user.domain.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {
	@UniqueConstraint(columnNames = {
		"pipitId"
	})
})
public class User extends DateAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Nonnull
	private String pipitId;

	@Nonnull
	@JsonIgnore
	private String pipitPassword;

	@Nonnull
	private String portalId;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(name = "user_roles",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@Builder
	public User(@Nonnull String pipitId, @Nonnull String portalId, @Nonnull String pipitPassword,
				@Nonnull Set<Role> roles) {
		this.pipitId = pipitId;
		this.portalId = portalId;
		this.pipitPassword = pipitPassword;
		this.roles = roles;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void setPipitPassword(@Nonnull String password) {
		this.pipitPassword = password;
	}
}
