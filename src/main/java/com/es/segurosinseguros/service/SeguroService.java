package com.es.segurosinseguros.service;

import com.es.segurosinseguros.exception.DataBaseException;
import com.es.segurosinseguros.model.Seguro;
import com.es.segurosinseguros.model.SeguroDTO;
import com.es.segurosinseguros.repository.SeguroRepository;
import com.es.segurosinseguros.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeguroService {
    @Autowired
    private SeguroRepository repository;

    public List<SeguroDTO> getAll() {
        List<Seguro> seguros;

        try {
            seguros = repository.findAll();
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        List<SeguroDTO> seguroDTOS = new ArrayList<>();

        seguros.forEach(seguro -> {
            SeguroDTO seguroDTO = Mapper.entityToDTO(seguro);

            seguroDTOS.add(seguroDTO);
        });

        return seguroDTOS;
    }

    public SeguroDTO getById(String id) {
        Long idL = 0L;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("La id debe de ser un número correcto");
        }

        Seguro seguro = null;

        try {
            seguro = repository.findById(idL).orElse(null);
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        if (seguro != null) {
            SeguroDTO seguroDTO = Mapper.entityToDTO(seguro);

            return seguroDTO;
        }

        return null;
    }
}
