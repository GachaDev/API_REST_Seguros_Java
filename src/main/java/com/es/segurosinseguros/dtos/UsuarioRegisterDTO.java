package com.es.segurosinseguros.dtos;

public class UsuarioRegisterDTO {
    private String username;
    private String password;
    private String rol;

    public UsuarioRegisterDTO(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public UsuarioRegisterDTO(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}