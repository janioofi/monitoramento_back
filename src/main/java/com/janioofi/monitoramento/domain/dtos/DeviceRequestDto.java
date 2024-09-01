package com.janioofi.monitoramento.domain.dtos;

import com.janioofi.monitoramento.domain.enums.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DeviceRequestDto(
        @NotEmpty(message = "Nome é obrigatório") String name,
        @NotNull(message = "Status é obrigatório") Status status,
        String location
) {
}
