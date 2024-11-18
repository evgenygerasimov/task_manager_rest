package com.evgenygerasimov.task_manager_rest.config.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Authentication request")
public class AuthRequest {

    @Schema(description = "Username", example = "admin")
    private String username;
    @Schema(description = "Password", example = "admin123")
    private String password;
}
