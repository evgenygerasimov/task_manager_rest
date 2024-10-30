package com.evgenygerasimov.taskmanagerrest.repository;

import com.evgenygerasimov.taskmanagerrest.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
