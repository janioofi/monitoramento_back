package com.janioofi.monitoramento.domain.dtos;

import java.util.UUID;

public record AlertResponseDto(
        UUID id,
        String level,
        String message,
        UUID deviceId
) {
}
