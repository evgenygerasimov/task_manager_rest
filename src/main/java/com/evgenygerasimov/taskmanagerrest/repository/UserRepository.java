package com.evgenygerasimov.taskmanagerrest.repository;

import com.evgenygerasimov.taskmanagerrest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
