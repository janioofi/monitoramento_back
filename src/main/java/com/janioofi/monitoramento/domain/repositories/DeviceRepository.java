package com.janioofi.monitoramento.domain.repositories;

import com.janioofi.monitoramento.domain.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
}
