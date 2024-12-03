package com.es.segurosinseguros.controller;

import com.es.segurosinseguros.exception.BadRequestException;
import com.es.segurosinseguros.exception.NotFoundException;
import com.es.segurosinseguros.dtos.SeguroDTO;
import com.es.segurosinseguros.service.SeguroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/seguros")
public class SeguroController {
    @Autowired
    private SeguroService service;

    @GetMapping("/")
    public ResponseEntity<List<SeguroDTO>> getAll() {
        List<SeguroDTO> seguroDTOS = service.getAll();

        if (seguroDTOS.isEmpty()) {
            return new ResponseEntity<>(seguroDTOS, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(seguroDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeguroDTO> getById(@PathVariable String id) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("id no válida");
        }

        SeguroDTO seguroDTO = service.getById(id);

        if (seguroDTO == null) {
            throw new NotFoundException("seguro no encontrado");
        }

        return new ResponseEntity<>(seguroDTO, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<SeguroDTO> create(@RequestBody SeguroDTO seguroDTO) {
        if (seguroDTO == null) {
            throw new BadRequestException("El cuerpo de la solicitud no puede estar vacío.");
        }

        SeguroDTO createdSeguro = service.create(seguroDTO);

        return new ResponseEntity<>(createdSeguro, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeguroDTO> update(@PathVariable String id, @RequestBody SeguroDTO seguroDTO) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("id no válida");
        }

        SeguroDTO updatedSeguro = service.update(id, seguroDTO);

        return new ResponseEntity<>(updatedSeguro, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("id no válida");
        }

        service.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
