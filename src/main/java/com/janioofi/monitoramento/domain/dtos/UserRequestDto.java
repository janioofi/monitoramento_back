package com.janioofi.monitoramento.domain.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(
        @NotBlank(message = "Username é obrigatório") String username,
        @NotBlank(message = "Password é obrigatório") String password
) {
}
