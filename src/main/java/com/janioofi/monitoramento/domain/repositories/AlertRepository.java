package com.janioofi.monitoramento.domain.repositories;

import com.janioofi.monitoramento.domain.entities.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlertRepository extends JpaRepository<Alert, UUID> {
    List<Alert> findByDeviceId(UUID deviceId);
}