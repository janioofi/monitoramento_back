package com.janioofi.monitoramento.controllers;

import com.janioofi.monitoramento.domain.dtos.LoginResponseDto;
import com.janioofi.monitoramento.domain.dtos.UserRequestDto;
import com.janioofi.monitoramento.domain.services.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserRequestDto user){
        LoginResponseDto response = service.login(user);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequestDto user){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(user));
    }
}
