package com.janioofi.monitoramento.domain.services;

import com.janioofi.monitoramento.domain.dtos.LoginResponseDto;
import com.janioofi.monitoramento.domain.dtos.UserRequestDto;
import com.janioofi.monitoramento.domain.entities.User;
import com.janioofi.monitoramento.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository repository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ValidUser_ReturnsToken() {
        // Arrange
        UserRequestDto userLoginDto = new UserRequestDto("username", "password");
        Authentication authentication = mock(Authentication.class);
        String token = "token";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenService.generateToken(any(User.class))).thenReturn(token);
        when(authentication.getPrincipal()).thenReturn(new User(1L, "username", "encodedPassword"));

        // Act
        LoginResponseDto response = authenticationService.login(userLoginDto);

        // Assert
        assertEquals(token, response.token());
    }

    @Test
    void register_ValidUser_ReturnsSuccessMessage() {
        // Arrange
        UserRequestDto userRequestDto = new UserRequestDto("username", "password");
        when(repository.findByUsername(userRequestDto.username())).thenReturn(Optional.empty());
        String encryptedPassword = new BCryptPasswordEncoder().encode(userRequestDto.password());
        User user = new User(null, userRequestDto.username(), encryptedPassword);
        when(repository.save(any(User.class))).thenReturn(user);

        // Act
        String result = authenticationService.register(userRequestDto);

        // Assert
        assertEquals("Usuário registrado com sucesso", result);
    }

    @Test
    void register_UsernameAlreadyExists_ThrowsDataIntegrityViolationException() {
        UserRequestDto userRequestDto = new UserRequestDto("username", "password");
        when(repository.findByUsername(userRequestDto.username())).thenReturn(Optional.of(new User()));

        DataIntegrityViolationException thrown = assertThrows(
                DataIntegrityViolationException.class,
                () -> authenticationService.register(userRequestDto),
                "Expected register() to throw DataIntegrityViolationException, but it didn't"
        );
        assertEquals("Já existe um usuário cadastrado com o mesmo username", thrown.getMessage());
    }


    @Test
    void register_EmptyFields_ThrowsDataIntegrityViolationException() {
        // Arrange
        UserRequestDto userRequestDto = new UserRequestDto("",  "");

        // Act & Assert
        DataIntegrityViolationException thrown = assertThrows(
                DataIntegrityViolationException.class,
                () -> authenticationService.register(userRequestDto),
                "Expected register() to throw DataIntegrityViolationException, but it didn't"
        );
        assertEquals("Todos os campos precisam ser preenchidos", thrown.getMessage());
    }

    @Test
    void login_EmptyFields_ThrowsDataIntegrityViolationException() {
        // Arrange
        UserRequestDto userLoginDto = new UserRequestDto("", "");

        // Act & Assert
        DataIntegrityViolationException thrown = assertThrows(
                DataIntegrityViolationException.class,
                () -> authenticationService.login(userLoginDto),
                "Expected login() to throw DataIntegrityViolationException, but it didn't"
        );
        assertEquals("Todos os campos precisam ser preenchidos", thrown.getMessage());
    }
}