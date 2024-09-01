package com.janioofi.monitoramento.domain.entities;

import com.janioofi.monitoramento.domain.enums.Level;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idLog;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    private LocalDateTime timestamp;

    private String message;

    @Enumerated(EnumType.STRING)
    private Level level;
}
