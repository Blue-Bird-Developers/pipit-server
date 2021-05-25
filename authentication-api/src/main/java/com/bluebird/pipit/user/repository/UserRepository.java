package com.bluebird.pipit.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bluebird.pipit.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPipitId(String pipitId);
	Optional<User> findByPortalId(String portalId);
    Optional<User> findByDisplayName(String displayName);
}
