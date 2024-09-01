package com.janioofi.monitoramento.domain.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record LogResponseDto(
        UUID idLog,
        String device,
        LocalDateTime timestamp,
        String message,
        String level
) {
}
