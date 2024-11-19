package com.evgenygerasimov.task_manager_rest.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Schema(description = "User entity")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "User ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "username", unique = true)
    @Pattern(regexp = "[a-z]{2,50}", message = "The name is required field and must be " +
            "a minimum of 2 characters and a maximum of 50 characters!")
    @Schema(description = "Unique username", example = "johndoe", accessMode = Schema.AccessMode.READ_WRITE)
    private String username;

    @Column(name = "password")
    @NotBlank(message = "Don not use the blanks")
    @Size(min = 4, max = 100, message = "The password must be a minimum of 4 characters " +
            "and a maximum of 100 characters!")
    @Schema(description = "User password", example = "JohnDoe", accessMode = Schema.AccessMode.READ_WRITE)
    private String password;

    @Column(name = "enabled")
    @Schema(description = "Enabling status of the user (0 - disabled, 1 - enabled), default is 1",
            example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int enabled = 1;

    @Column(name = "role")
    @NotBlank(message = "The role is required field!")
    @Pattern(regexp = "ROLE_EXECUTOR|ROLE_ADMIN", message = "The role must be either ROLE_USER or ROLE_ADMIN!")
    @Schema(description = "User role", example = "ROLE_EXECUTOR or ROLE_ADMIN", accessMode = Schema.AccessMode.READ_WRITE)
    private String role;

    @Schema(description = "User tasks", accessMode = Schema.AccessMode.READ_ONLY)
    @OneToMany(mappedBy = "user")
    private List<Task> tasks;

    @Schema(description = "Granted authorities", accessMode = Schema.AccessMode.READ_ONLY)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(role));
    }

    @Schema(description = "User account is not expired", accessMode = Schema.AccessMode.READ_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Schema(description = "Is user account not locked", accessMode = Schema.AccessMode.READ_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Schema(description = "User credentials are not expired", accessMode = Schema.AccessMode.READ_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Schema(description = "User is enabled", accessMode = Schema.AccessMode.READ_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
