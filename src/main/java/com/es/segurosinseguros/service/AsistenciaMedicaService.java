package com.es.segurosinseguros.service;

import com.es.segurosinseguros.dtos.AsistenciaMedicaDTO;
import com.es.segurosinseguros.exception.BadRequestException;
import com.es.segurosinseguros.exception.DataBaseException;
import com.es.segurosinseguros.exception.NotFoundException;
import com.es.segurosinseguros.model.AsistenciaMedica;
import com.es.segurosinseguros.model.Seguro;
import com.es.segurosinseguros.repository.AsistenciaMedicaRepository;
import com.es.segurosinseguros.repository.SeguroRepository;
import com.es.segurosinseguros.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AsistenciaMedicaService {
    @Autowired
    private AsistenciaMedicaRepository repository;
    @Autowired
    private SeguroRepository seguroRepository;

    public List<AsistenciaMedicaDTO> getAll() {
        List<AsistenciaMedica> asistenciaMedicas;

        try {
            asistenciaMedicas = repository.findAll();
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        List<AsistenciaMedicaDTO> asistenciaMedicaDTOS = new ArrayList<>();

        asistenciaMedicas.forEach(asistenciaMedica -> {
            AsistenciaMedicaDTO asistenciaMedicaDTO = Mapper.entityToDTO(asistenciaMedica);

            asistenciaMedicaDTOS.add(asistenciaMedicaDTO);
        });

        return asistenciaMedicaDTOS;
    }

    public AsistenciaMedicaDTO getById(String id) {
        Long idL = 0L;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("La id debe de ser un número correcto");
        }

        AsistenciaMedica asistenciaMedica = null;

        try {
            asistenciaMedica = repository.findById(idL).orElse(null);
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        if (asistenciaMedica != null) {
            return Mapper.entityToDTO(asistenciaMedica);
        }

        return null;
    }

    private void validateDTO(AsistenciaMedicaDTO asistenciaMedicaDTO) {
        if (asistenciaMedicaDTO.getBreveDescripcion() == null || asistenciaMedicaDTO.getBreveDescripcion().isEmpty()) {
            throw new BadRequestException("El campo breveDescripcion no puede estar vacío.");
        }

        if (asistenciaMedicaDTO.getExplicacion() == null || asistenciaMedicaDTO.getExplicacion().isEmpty()) {
            throw new BadRequestException("El campo explicación no puede estar vacío.");
        }

        if (asistenciaMedicaDTO.getLugar() == null || asistenciaMedicaDTO.getLugar().isEmpty()) {
            throw new BadRequestException("El campo lugar no puede estar vacío.");
        }

        if (asistenciaMedicaDTO.getTipoAsistencia() == null || asistenciaMedicaDTO.getTipoAsistencia().isEmpty()) {
            throw new BadRequestException("El campo tipoAsistencia no puede estar vacío.");
        }

        if (asistenciaMedicaDTO.getHora() == null || asistenciaMedicaDTO.getFecha() == null) {
            throw new BadRequestException("El campo {fecha/hora} no ser nulo.");
        }

        if (asistenciaMedicaDTO.getImporte() <= 0) {
            throw new BadRequestException("El campo importe debe ser mayor que 0.");
        }
    }

    public AsistenciaMedicaDTO create(AsistenciaMedicaDTO asistenciaMedicaDTO) {
        validateDTO(asistenciaMedicaDTO);

        Seguro seguro = seguroRepository.findById(asistenciaMedicaDTO.getSeguroId()).orElse(null);

        if (seguro == null) {
            throw new NotFoundException("Seguro no encontrado");
        }

        AsistenciaMedica asistenciaMedica = Mapper.DTOToEntity(asistenciaMedicaDTO, seguro);

        try {
            AsistenciaMedica asistenciaMedicaCreated = repository.save(asistenciaMedica);

            return Mapper.entityToDTO(asistenciaMedicaCreated);
        } catch (Exception e) {
            throw new DataBaseException("Error al crear la asistencia médica. " + e.getMessage());
        }
    }

    public AsistenciaMedicaDTO update(String id, AsistenciaMedicaDTO asistenciaMedicaDTO) {
        validateDTO(asistenciaMedicaDTO);

        Long idL;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("La ID debe ser un número válido");
        }

        Seguro seguro = seguroRepository.findById(asistenciaMedicaDTO.getSeguroId()).orElse(null);

        if (seguro == null) {
            throw new NotFoundException("Seguro no encontrado");
        }

        AsistenciaMedica asistenciaExistente = repository.findById(idL)
                .orElseThrow(() -> new NotFoundException("Asistencia médica no encontrada"));

        asistenciaExistente.setBreveDescripcion(asistenciaMedicaDTO.getBreveDescripcion());
        asistenciaExistente.setLugar(asistenciaMedicaDTO.getLugar());
        asistenciaExistente.setExplicacion(asistenciaMedicaDTO.getExplicacion());
        asistenciaExistente.setTipoAsistencia(asistenciaMedicaDTO.getTipoAsistencia());
        asistenciaExistente.setFecha(asistenciaMedicaDTO.getFecha());
        asistenciaExistente.setHora(asistenciaMedicaDTO.getHora());
        asistenciaExistente.setImporte(asistenciaMedicaDTO.getImporte());
        asistenciaExistente.setSeguro(seguro);

        try {
            AsistenciaMedica asistenciaMedica = repository.save(asistenciaExistente);

            return Mapper.entityToDTO(asistenciaMedica);
        } catch (Exception e) {
            throw new DataBaseException("Error al actualizar la asistencia médica. " + e.getMessage());
        }
    }

    public void delete(String id) {
        Long idL;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("La ID debe ser un número válido");
        }

        AsistenciaMedica asistencia = repository.findById(idL)
                .orElseThrow(() -> new NotFoundException("Asistencia médica no encontrada"));

        try {
            repository.delete(asistencia);
        } catch (Exception e) {
            throw new DataBaseException("Error al eliminar la asistencia médica. " + e.getMessage());
        }
    }
}
