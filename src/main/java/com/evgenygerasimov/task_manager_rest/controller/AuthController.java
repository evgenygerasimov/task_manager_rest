package com.evgenygerasimov.task_manager_rest.controller;

import com.evgenygerasimov.task_manager_rest.config.security.AuthRequest;
import com.evgenygerasimov.task_manager_rest.dto.TokenDTO;
import com.evgenygerasimov.task_manager_rest.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "This endpoint is used to authenticate the user and get a JWT token.")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(
            summary = "Login to the system",
            description = "This endpoint is used to login to the system. " +
                    "It returns a JWT token that can be used for further authentication." +
                    "The username and password of the user in the format: " +
                    "{\"username\": \"username\", \"password\": \"password\"}")
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Parameter(description = "You need to provide the username and password in the format: json", required = true) AuthRequest authRequest) {
        TokenDTO tokenDTO = authService.login(authRequest);
        return ResponseEntity.ok(tokenDTO);
    }

    @Operation(
            summary = "Refresh the token",
            description = "This endpoint is used to refresh the token. " +
                    "It returns a new JWT token that can be used for further authentication." +
                    "The JWT token in the format: " + "{\"refreshToken\": \"your_refresh_token\"}")
    @PostMapping("/refresh")
    public TokenDTO refresh(@RequestBody @Parameter(
            description = "You need to provide the refresh token in the format: json",
            required = true) TokenDTO tokenDTO) {
        return authService.refreshToken(tokenDTO);
    }

    @Operation(
            summary = "Login from the system",
            description = "This endpoint is used to logout from the system. " +
                    "It invalidates the JWT token and returns a message that the user has logged out." +
                    "The JWT token in the format: {\"accessToken\": \"your_access_token\"}")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody @Parameter(
            description = "You need to provide the access token in the format: json",
            required = true) TokenDTO tokenDTO) {
        String response = authService.logout(tokenDTO);
        if (response.equals("You have already logged out")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }
}