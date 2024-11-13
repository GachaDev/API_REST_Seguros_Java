package com.es.segurosinseguros.controller;

import com.es.segurosinseguros.exception.BadRequestException;
import com.es.segurosinseguros.model.SeguroDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/seguros")
public class SeguroController {
    //AUTOWIRED

    @GetMapping("/{id}")
    public ResponseEntity<SeguroDTO> getById(@PathVariable String id) {
        //Compruebo que el id no es null

        if (id == null || id.isBlank()) {
            //lanzo excepcion propia
            throw new BadRequestException("id no válida");
        }

        return null;
    }
}
