package com.service.crud.Repo;

import com.service.crud.Domain.ERole;
import com.service.crud.Domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
