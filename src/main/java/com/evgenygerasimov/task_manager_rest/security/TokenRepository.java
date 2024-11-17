package com.evgenygerasimov.task_manager_rest.security;


import com.evgenygerasimov.task_manager_rest.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByAccessToken(String accessToken);

    Optional<Token> findByRefreshToken(String accessToken);
}