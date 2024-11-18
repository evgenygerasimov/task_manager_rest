package com.evgenygerasimov.task_manager_rest.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tokens")
@Schema(description = "Token entity")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "Token ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "username")
    @Schema(description = "Username user who created token", example = "user1", accessMode = Schema.AccessMode.READ_ONLY)
    private String username;

    @Column(name = "access_token")
    @Schema(description = "Access token",
            example = "eyJhbGciOiJIUmFtZSI6IfwpMeJf36POk6yJV_adQssw5c", accessMode = Schema.AccessMode.READ_ONLY)
    private String accessToken;

    @Column(name = "refresh_token")
    @Schema(description = "Refresh token",
            example = "eyJhbGciOiM0NTY3ODQT4fwpMeJf36POk6yJV_adQssw5c", accessMode = Schema.AccessMode.READ_ONLY)
    private String refreshToken;

    @Column(name = "is_valid")
    @Schema(description = "Token is valid", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private boolean isValid;
}