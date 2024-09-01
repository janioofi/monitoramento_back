package com.janioofi.monitoramento.controllers;

import com.janioofi.monitoramento.domain.dtos.LoginResponseDto;
import com.janioofi.monitoramento.domain.dtos.UserRequestDto;
import com.janioofi.monitoramento.domain.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private UserRequestDto userRequestDto;
    private LoginResponseDto loginResponseDto;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto("testuser", "password123");
        loginResponseDto = new LoginResponseDto("token123");
    }

    @Test
    void login_ShouldReturnLoginResponse() {
        // Arrange
        when(authenticationService.login(userRequestDto)).thenReturn(loginResponseDto);

        // Act
        ResponseEntity<LoginResponseDto> response = authenticationController.login(userRequestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loginResponseDto, response.getBody());
        verify(authenticationService, times(1)).login(userRequestDto);
    }

    @Test
    void register_ShouldReturnCreatedStatus() {
        // Arrange
        String successMessage = "User registered successfully";
        when(authenticationService.register(userRequestDto)).thenReturn(successMessage);

        // Act
        ResponseEntity<String> response = authenticationController.register(userRequestDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(successMessage, response.getBody());
        verify(authenticationService, times(1)).register(userRequestDto);
    }
}
