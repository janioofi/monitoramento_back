package com.janioofi.monitoramento.controllers;

import com.janioofi.monitoramento.domain.dtos.DeviceRequestDto;
import com.janioofi.monitoramento.domain.dtos.DeviceResponseDto;
import com.janioofi.monitoramento.domain.dtos.LogResponseDto;
import com.janioofi.monitoramento.domain.services.DeviceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
@Tag(name = "Device", description = "Device API")
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping
    public ResponseEntity<List<DeviceResponseDto>> getAllDevices() {
        List<DeviceResponseDto> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponseDto> getDeviceById(@PathVariable UUID id) {
        DeviceResponseDto device = deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @PostMapping
    public ResponseEntity<DeviceResponseDto> createDevice(@RequestBody @Valid DeviceRequestDto request) {
        DeviceResponseDto createdDevice = deviceService.createDevice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDevice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponseDto> updateDevice(@PathVariable UUID id, @RequestBody @Valid DeviceRequestDto request) {
        DeviceResponseDto updatedDevice = deviceService.updateDevice(id, request);
        return ResponseEntity.ok(updatedDevice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id) {
        deviceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/logs")
    public ResponseEntity<List<LogResponseDto>> getLogsByDeviceId(@PathVariable UUID id) {
        List<LogResponseDto> logs = deviceService.getLogsByDeviceId(id);
        return ResponseEntity.ok(logs);
    }
}
