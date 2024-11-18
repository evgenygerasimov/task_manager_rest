package com.evgenygerasimov.task_manager_rest.service;

import static org.junit.jupiter.api.Assertions.*;

import com.evgenygerasimov.task_manager_rest.config.security.AuthRequest;
import com.evgenygerasimov.task_manager_rest.config.security.JwtService;
import com.evgenygerasimov.task_manager_rest.dto.TokenDTO;
import com.evgenygerasimov.task_manager_rest.entity.Token;
import com.evgenygerasimov.task_manager_rest.exception.AuthenticationException;
import com.evgenygerasimov.task_manager_rest.exception.InvalidTokenExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private AuthRequest authRequest;
    private TokenDTO validTokenDTO;
    private Token token;

    @BeforeEach
    public void setUp() {
        authRequest = new AuthRequest();
        authRequest.setUsername("validUser");
        authRequest.setPassword("validPassword");

        validTokenDTO = new TokenDTO();
        validTokenDTO.setAccessToken("validAccessToken");
        validTokenDTO.setRefreshToken("validRefreshToken");

        token = new Token();
        token.setValid(true);
        token.setAccessToken("validAccessToken");
        token.setRefreshToken("validRefreshToken");
        token.setUsername("validUser");
    }

    @Test
    public void shouldReturnValidTokenDTOWhenLoginInRest() {
        //setup
        when(jwtService.getTokens()).thenReturn(Arrays.asList(token));
        //execute
        TokenDTO result = authService.login(authRequest);
        //assert
        assertNotNull(result);
        assertEquals("validAccessToken", result.getAccessToken());
        assertEquals("validRefreshToken", result.getRefreshToken());
    }

    @Test
    public void shouldThrowAuthenticationExceptionWhenLoginOrPasswordIsInvalidTest() {
        //setup
        when(jwtService.getTokens()).thenReturn(Arrays.asList());
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
        when(authenticationManager.authenticate(authToken)).thenThrow(new AuthenticationException("Invalid username or password"));
        //execute and assert
        assertThrows(AuthenticationException.class, () -> {
            authService.login(authRequest);
        });
    }

    @Test
    public void shouldReturnValidTokenDTOWhenRefreshTokenTest() {
        //setup
        when(jwtService.extractUserName(anyString())).thenReturn("validUser");
        when(jwtService.generateAccessToken(anyString())).thenReturn("newAccessToken");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("newRefreshToken");
        //execute
        TokenDTO result = authService.refreshToken(validTokenDTO);
        //assert
        assertNotNull(result);
        assertEquals("newAccessToken", result.getAccessToken());
        assertEquals("newRefreshToken", result.getRefreshToken());
    }

    @Test
    public void shouldThrowInvalidTokenExceptionHandlerWhenInvalidRefreshTokenTest() {
        //setup
        when(jwtService.extractUserName(anyString())).thenThrow(new InvalidTokenExceptionHandler("Invalid refresh token"));
        //execute and assert
        assertThrows(InvalidTokenExceptionHandler.class, () -> {
            authService.refreshToken(validTokenDTO);
        });
    }

    @Test
    public void shouldLogoutTest() {
        //setup
        when(jwtService.getAccessToken(anyString())).thenReturn(token);
        //execute
        String result = authService.logout(validTokenDTO);
        //assert
        assertEquals("Successfully logged out", result);
    }

    @Test
    public void shouldReturnRightMessageWhenLogoutAlreadyLoggedOutTest() {
        //setup
        token.setValid(false);
        when(jwtService.getAccessToken(anyString())).thenReturn(token);
        //execute
        String result = authService.logout(validTokenDTO);
        //assert
        assertEquals("You have already logged out", result);

    }
}