package com.evgenygerasimov.task_manager_rest.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "authorities")
@Schema(description = "Role entity")
public class Role {

    @Id
    @Column(name = "username")
    @Schema(description = "Username user whose role is being assigned",
            example = "user1", accessMode = Schema.AccessMode.READ_ONLY)
    private String username;

    @Column(name = "authority")
    @Schema(description = "Role assigned to user",
            example = "ROLE_ADMIN", accessMode = Schema.AccessMode.READ_ONLY)
    private String authority;
}
