package com.bluebird.pipit.user.repository;

import com.bluebird.pipit.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPipitId(String pipitId);

    Optional<User> findByDisplayName(String displayName);
}
