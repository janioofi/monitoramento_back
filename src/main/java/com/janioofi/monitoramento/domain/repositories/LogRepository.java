package com.janioofi.monitoramento.domain.repositories;

import com.janioofi.monitoramento.domain.entities.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface LogRepository extends JpaRepository<Log, UUID> {
    @Query("SELECT l FROM Log l WHERE l.device.idDevice = :deviceId")
    List<Log> findByDeviceId(UUID deviceId);
}
