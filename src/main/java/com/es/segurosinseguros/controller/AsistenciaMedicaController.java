package com.es.segurosinseguros.controller;

import com.es.segurosinseguros.dtos.AsistenciaMedicaDTO;
import com.es.segurosinseguros.exception.BadRequestException;
import com.es.segurosinseguros.exception.NotFoundException;
import com.es.segurosinseguros.service.AsistenciaMedicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/asistencias")
public class AsistenciaMedicaController {
    @Autowired
    private AsistenciaMedicaService service;

    @GetMapping("/")
    public ResponseEntity<List<AsistenciaMedicaDTO>> getAll() {
        List<AsistenciaMedicaDTO> asistenciaMedicaDTOS = service.getAll();

        if (asistenciaMedicaDTOS.isEmpty()) {
            return new ResponseEntity<>(asistenciaMedicaDTOS, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(asistenciaMedicaDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsistenciaMedicaDTO> getById(@PathVariable String id) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("id no válida");
        }

        AsistenciaMedicaDTO asistenciaMedicaDTO = service.getById(id);

        if (asistenciaMedicaDTO == null) {
            throw new NotFoundException("asistencia médica no encontrada");
        }

        return new ResponseEntity<>(asistenciaMedicaDTO, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<AsistenciaMedicaDTO> create(@RequestBody AsistenciaMedicaDTO asistenciaMedicaDTO) {
        if (asistenciaMedicaDTO == null) {
            throw new BadRequestException("Los datos de la asistencia médica son requeridos");
        }

        AsistenciaMedicaDTO createdAsistenciaMedica = service.create(asistenciaMedicaDTO);

        return new ResponseEntity<>(createdAsistenciaMedica, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsistenciaMedicaDTO> update(@PathVariable String id, @RequestBody AsistenciaMedicaDTO asistenciaMedicaDTO) {
        if (id == null || id.isBlank() || asistenciaMedicaDTO == null) {
            throw new BadRequestException("ID y datos de la asistencia médica son requeridos");
        }

        AsistenciaMedicaDTO updatedAsistenciaMedica = service.update(id, asistenciaMedicaDTO);

        return new ResponseEntity<>(updatedAsistenciaMedica, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("ID no puede ser vacia");
        }

        service.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
