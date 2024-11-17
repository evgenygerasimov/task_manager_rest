package com.evgenygerasimov.task_manager_rest.repository;

import com.evgenygerasimov.task_manager_rest.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
