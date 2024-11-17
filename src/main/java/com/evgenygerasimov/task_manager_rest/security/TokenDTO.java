package com.evgenygerasimov.task_manager_rest.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDTO {

    private String accessToken;
    private String refreshToken;
}
