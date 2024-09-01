package com.janioofi.monitoramento.domain.enums;

import lombok.Getter;

@Getter
public enum Status {
    ATIVO(0, "Ativo"),
    INATIVO(1, "Inativo"),
    EM_FALHA(2, "Em Falha");

    private final Integer code;
    private final String description;

    Status(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
