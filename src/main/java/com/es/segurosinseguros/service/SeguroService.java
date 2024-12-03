package com.es.segurosinseguros.service;

import com.es.segurosinseguros.exception.BadRequestException;
import com.es.segurosinseguros.exception.DataBaseException;
import com.es.segurosinseguros.exception.NotFoundException;
import com.es.segurosinseguros.model.Seguro;
import com.es.segurosinseguros.dtos.SeguroDTO;
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
            return Mapper.entityToDTO(seguro);
        }

        return null;
    }

    private void validateDTO(SeguroDTO seguroDTO) {
        if (!seguroDTO.getNif().matches("^(\\d{8})([A-Z])$")) {
            throw new BadRequestException("El campo NIF no tiene un formato válido.");
        }

        if (seguroDTO.getNombre() == null || seguroDTO.getNombre().isEmpty()) {
            throw new BadRequestException("El campo nombre no puede estar vacío.");
        }

        if (seguroDTO.getApe1() == null || seguroDTO.getApe1().isEmpty()) {
            throw new BadRequestException("El campo ape1 no puede estar vacío.");
        }

        if (seguroDTO.getEdad() <= 0) {
            throw new BadRequestException("El campo edad debe ser mayor que 0.");
        }
        if (seguroDTO.getEdad() < 18) {
            throw new BadRequestException("No es posible ser menor de edad para hacer un seguro.");
        }

        if (seguroDTO.getNumHijos() < 0) {
            throw new BadRequestException("El número de hijos no puede ser menor que 0.");
        }

        if (!seguroDTO.isCasado() && seguroDTO.getNumHijos() > 0) {
            throw new BadRequestException("Un seguro no puede registrar hijos si no está casado.");
        }

        if (seguroDTO.isEmbarazada() && "Hombre".equalsIgnoreCase(seguroDTO.getSexo())) {
            throw new BadRequestException("El campo embarazada no puede ser true si el asegurado es hombre.");
        }
    }


    public SeguroDTO create(SeguroDTO seguroDTO) {
        validateDTO(seguroDTO);

        try {
            Seguro seguro = Mapper.DTOToEntity(seguroDTO);
            Seguro savedSeguro = repository.save(seguro);

            return Mapper.entityToDTO(savedSeguro);
        } catch (Exception e) {
            throw new DataBaseException("Error al crear el seguro. " + e.getMessage());
        }
    }

    public SeguroDTO update(String id, SeguroDTO seguroDTO) {
        Long idL;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("La id debe ser un número válido.");
        }

        validateDTO(seguroDTO);

        Seguro existingSeguro = repository.findById(idL).orElse(null);

        if (existingSeguro == null) {
            throw new NotFoundException("Seguro no encontrado para actualizar.");
        }

        try {
            existingSeguro.setNif(seguroDTO.getNif());
            existingSeguro.setNombre(seguroDTO.getNombre());
            existingSeguro.setApe1(seguroDTO.getApe1());
            existingSeguro.setApe2(seguroDTO.getApe2());
            existingSeguro.setEdad(seguroDTO.getEdad());
            existingSeguro.setNumHijos(seguroDTO.getNumHijos());
            existingSeguro.setSexo(seguroDTO.getSexo());
            existingSeguro.setCasado(seguroDTO.isCasado());
            existingSeguro.setEmbarazada(seguroDTO.isEmbarazada());

            Seguro updatedSeguro = repository.save(existingSeguro);

            return Mapper.entityToDTO(updatedSeguro);
        } catch (Exception e) {
            throw new DataBaseException("Error al actualizar el seguro. " + e.getMessage());
        }
    }

    public void delete(String id) {
        Long idL;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("La id debe ser un número válido.");
        }

        try {
            if (!repository.existsById(idL)) {
                throw new NotFoundException("Seguro no encontrado para eliminar.");
            }

            repository.deleteById(idL);
        } catch (NotFoundException e) {
            throw new NotFoundException("Seguro no encontrado para eliminar.");
        } catch (Exception e) {
            throw new DataBaseException("Error al eliminar el seguro. " + e.getMessage());
        }
    }
}
