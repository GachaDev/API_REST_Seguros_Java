package com.es.segurosinseguros.utils;

import com.es.segurosinseguros.model.AsistenciaMedica;
import com.es.segurosinseguros.dtos.AsistenciaMedicaDTO;
import com.es.segurosinseguros.model.Seguro;
import com.es.segurosinseguros.dtos.SeguroDTO;
import com.es.segurosinseguros.model.Usuario;

public class Mapper {
    public static SeguroDTO entityToDTO(Seguro seguro) {
        SeguroDTO seguroDTO = new SeguroDTO();

        seguroDTO.setNif(seguro.getNif());
        seguroDTO.setNombre(seguro.getNombre());
        seguroDTO.setApe1(seguro.getApe1());
        seguroDTO.setApe2(seguro.getApe2());
        seguroDTO.setEdad(seguro.getEdad());
        seguroDTO.setNumHijos(seguro.getNumHijos());
        seguroDTO.setSexo(seguro.getSexo());
        seguroDTO.setCasado(seguro.getCasado());
        seguroDTO.setEmbarazada(seguro.getEmbarazada());
        seguroDTO.setId_usuario(seguro.getUsuario().getId());

        return seguroDTO;
    }

    public static Seguro DTOToEntity(SeguroDTO seguroDTO, Usuario usuario) {
        Seguro seguro = new Seguro();

        seguro.setNif(seguroDTO.getNif());
        seguro.setNombre(seguroDTO.getNombre());
        seguro.setApe1(seguroDTO.getApe1());
        seguro.setApe2(seguroDTO.getApe2());
        seguro.setEdad(seguroDTO.getEdad());
        seguro.setNumHijos(seguroDTO.getNumHijos());
        seguro.setSexo(seguroDTO.getSexo());
        seguro.setCasado(seguroDTO.isCasado());
        seguro.setEmbarazada(seguroDTO.isEmbarazada());
        seguro.setUsuario(usuario);

        return seguro;
    }

    public static AsistenciaMedicaDTO entityToDTO(AsistenciaMedica asistenciaMedica) {
        AsistenciaMedicaDTO asistenciaMedicaDTO = new AsistenciaMedicaDTO();

        asistenciaMedicaDTO.setBreveDescripcion(asistenciaMedica.getBreveDescripcion());
        asistenciaMedicaDTO.setLugar(asistenciaMedica.getLugar());
        asistenciaMedicaDTO.setExplicacion(asistenciaMedica.getExplicacion());
        asistenciaMedicaDTO.setTipoAsistencia(asistenciaMedica.getTipoAsistencia());
        asistenciaMedicaDTO.setFecha(asistenciaMedica.getFecha());
        asistenciaMedicaDTO.setHora(asistenciaMedica.getHora());
        asistenciaMedicaDTO.setImporte(asistenciaMedica.getImporte());
        asistenciaMedicaDTO.setSeguroId(asistenciaMedica.getSeguro().getIdSeguro());

        return asistenciaMedicaDTO;
    }

    public static AsistenciaMedica DTOToEntity(AsistenciaMedicaDTO asistenciaMedicaDTO, Seguro seguro) {
        AsistenciaMedica asistenciaMedica = new AsistenciaMedica();

        asistenciaMedica.setBreveDescripcion(asistenciaMedicaDTO.getBreveDescripcion());
        asistenciaMedica.setLugar(asistenciaMedicaDTO.getLugar());
        asistenciaMedica.setExplicacion(asistenciaMedicaDTO.getExplicacion());
        asistenciaMedica.setTipoAsistencia(asistenciaMedicaDTO.getTipoAsistencia());
        asistenciaMedica.setFecha(asistenciaMedicaDTO.getFecha());
        asistenciaMedica.setHora(asistenciaMedicaDTO.getHora());
        asistenciaMedica.setImporte(asistenciaMedicaDTO.getImporte());
        asistenciaMedica.setSeguro(seguro);

        return asistenciaMedica;
    }
}
