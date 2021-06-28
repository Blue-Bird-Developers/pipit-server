package com.bluebird.pipit.user.domain;

import java.util.HashSet;
import java.util.Set;

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

import com.bluebird.pipit.security.Role;
import com.bluebird.pipit.user.domain.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@NotNull
	private String pipitId;

	@NotNull
	@JsonIgnore
	private String pipitPassword;

	@NotNull
	private String portalId;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles",
		joinColumns = @JoinColumn(name = "_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@Builder
	public User(String pipitId, String portalId, String pipitPassword) {
		this.pipitId = pipitId;
		this.portalId = portalId;
		this.pipitPassword = pipitPassword;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void setPipitPassword(String password) {
		this.pipitPassword = password;
	}
}
