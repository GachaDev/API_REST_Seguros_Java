package com.es.segurosinseguros.controller;

import com.es.segurosinseguros.exception.BadRequestException;
import com.es.segurosinseguros.exception.NotFoundException;
import com.es.segurosinseguros.model.SeguroDTO;
import com.es.segurosinseguros.service.SeguroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/seguros")
public class SeguroController {
    @Autowired
    private SeguroService service;

    @GetMapping("/")
    public ResponseEntity<List<SeguroDTO>> getAll() {
        List<SeguroDTO> seguroDTOS = service.getAll();

        if (seguroDTOS.size() < 1) {
            return new ResponseEntity<List<SeguroDTO>>(seguroDTOS, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<SeguroDTO>>(seguroDTOS, HttpStatus.OK);
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

        return new ResponseEntity<SeguroDTO>(seguroDTO, HttpStatus.OK);
    }
}
