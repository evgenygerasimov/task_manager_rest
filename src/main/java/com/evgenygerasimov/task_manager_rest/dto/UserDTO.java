package com.evgenygerasimov.task_manager_rest.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User DTO")
public class UserDTO {

    @Schema(description = "User ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Unique username", example = "JohnDoe", accessMode = Schema.AccessMode.READ_ONLY)
    private String username;

    @Schema(description = "Enabling status of the user (0 - disabled, 1 - enabled)",
            example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int enabled;

    @Schema(description = "User role", example = "USER", accessMode = Schema.AccessMode.READ_ONLY)
    private String role;
}
