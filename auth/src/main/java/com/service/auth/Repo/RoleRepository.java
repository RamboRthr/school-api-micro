package com.service.auth.Repo;

import com.service.auth.Domain.ERole;
import com.service.auth.Domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
