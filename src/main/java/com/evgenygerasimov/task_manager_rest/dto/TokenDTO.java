package com.evgenygerasimov.task_manager_rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Token DTO")
public class TokenDTO {

    @Schema(description = "Access token",
            example = "eyJhbGciOiJIUzI1NiIsInR56POk6yJV_adQssw5c")
    private String accessToken;

    @Schema(description = "Refresh token",
            example = "eyJhbGciOiJIUzI1NiIsINTE2MjM6yJV_adQssw5c")
    private String refreshToken;
}
