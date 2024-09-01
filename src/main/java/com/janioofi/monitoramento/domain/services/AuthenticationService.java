package com.janioofi.monitoramento.domain.services;

import com.janioofi.monitoramento.domain.dtos.LoginResponseDto;
import com.janioofi.monitoramento.domain.dtos.UserRequestDto;
import com.janioofi.monitoramento.domain.entities.User;
import com.janioofi.monitoramento.domain.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final TokenService tokenService;

    public LoginResponseDto login(@Valid UserRequestDto user){
        var usuarioSenha = new UsernamePasswordAuthenticationToken(user.username(), user.password());
        var auth = authenticationManager.authenticate(usuarioSenha);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        log.info("{} Logging in", user.username());
        return new LoginResponseDto(token);
    }

    public String register(@Valid UserRequestDto user){
        validateRegister(user);
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());
        User data = new User(null, user.username(), encryptedPassword);
        repository.save(data);
        log.info("{} registered", user.username());
        return "Usuário registrado com sucesso";
    }

    private void validateRegister(UserRequestDto user){
        if(this.repository.findByUsername(user.username()).isPresent()){
            throw new DataIntegrityViolationException("Já existe um usuário cadastrado com o mesmo username");
        }
    }
}
