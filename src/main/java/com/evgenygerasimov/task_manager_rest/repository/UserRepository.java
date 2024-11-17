package com.evgenygerasimov.task_manager_rest.repository;

import com.evgenygerasimov.task_manager_rest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
