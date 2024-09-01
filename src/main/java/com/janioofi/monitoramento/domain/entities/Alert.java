package com.janioofi.monitoramento.domain.entities;

import com.janioofi.monitoramento.domain.enums.Level;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idAlert;

    private String message;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Level level;
}
