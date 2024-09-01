package com.janioofi.monitoramento.domain.entities;

import lombok.Data;

@Data
public class EmailModel{
    private String emailFrom;
    private String emailTo;
    private String subject;
    private String text;
}
