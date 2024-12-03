package com.es.segurosinseguros.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "seguros")
public class Seguro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSeguro;
    @Column(nullable = false, unique = true, length = 10)
    private String nif;
    @Column(nullable = false, length = 100)
    private String nombre;
    @Column(nullable = false, length = 100)
    private String ape1;
    @Column(length = 100)
    private String ape2;
    @Column(nullable = false)
    private int edad;
    @Column(nullable = false)
    private int numHijos;
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;
    @Column(nullable = false, length = 10)
    private String sexo;
    @Column(nullable = false)
    private Boolean casado;
    @Column(nullable = false)
    private Boolean embarazada;

    @OneToMany(mappedBy = "seguro", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AsistenciaMedica> asistencias;

    public Seguro(String nif, String nombre, String ape1, String ape2, int edad, int numHijos, String sexo, Boolean casado, Boolean embarazada) {
        this.nif = nif;
        this.nombre = nombre;
        this.ape1 = ape1;
        this.ape2 = ape2;
        this.edad = edad;
        this.numHijos = numHijos;
        this.fechaCreacion = LocalDate.now();
        this.sexo = sexo;
        this.casado = casado;
        this.embarazada = embarazada;
    }

    public Seguro() {
        this.fechaCreacion = LocalDate.now();
    }

    public Long getIdSeguro() {
        return idSeguro;
    }

    public void setIdSeguro(Long idSeguro) {
        this.idSeguro = idSeguro;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApe1() {
        return ape1;
    }

    public void setApe1(String ape1) {
        this.ape1 = ape1;
    }

    public String getApe2() {
        return ape2;
    }

    public void setApe2(String ape2) {
        this.ape2 = ape2;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getNumHijos() {
        return numHijos;
    }

    public void setNumHijos(int numHijos) {
        this.numHijos = numHijos;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Boolean getCasado() {
        return casado;
    }

    public void setCasado(Boolean casado) {
        this.casado = casado;
    }

    public Boolean getEmbarazada() {
        return embarazada;
    }

    public void setEmbarazada(Boolean embarazada) {
        this.embarazada = embarazada;
    }
}
