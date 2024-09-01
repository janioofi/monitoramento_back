package com.janioofi.monitoramento.controllers;

import com.janioofi.monitoramento.domain.dtos.AlertRequestDto;
import com.janioofi.monitoramento.domain.dtos.AlertResponseDto;
import com.janioofi.monitoramento.domain.entities.Alert;
import com.janioofi.monitoramento.domain.services.AlertService;
import com.janioofi.monitoramento.exceptions.AlertTriggerException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/alerts")
@Tag(name = "Alert", description = "Alert API")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @PostMapping
    public ResponseEntity<AlertResponseDto> createAlert(@RequestBody AlertRequestDto requestDto) {
        AlertResponseDto createdAlert = alertService.createAlert(requestDto);
        return new ResponseEntity<>(createdAlert, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AlertResponseDto>> getAllAlerts() {
        List<AlertResponseDto> alerts = alertService.getAllAlerts();
        return new ResponseEntity<>(alerts, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable UUID id) {
        alertService.deleteAlert(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
