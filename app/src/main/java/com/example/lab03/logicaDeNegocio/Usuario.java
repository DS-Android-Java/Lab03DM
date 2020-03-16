package com.example.lab03.logicaDeNegocio;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String usuario;
    private String clave;
    private String rol;//Rol se puede tomar como privilegio

    public Usuario(String usuario, String clave, String rol) {
        this.usuario = usuario;
        this.clave = clave;
        this.rol = rol;
    }

    public Usuario() {
        usuario = new String();
        clave = new String();
        rol = new String();
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" + "usuario=" + usuario + ", clave=" + clave + ", rol=" + rol + '}';
    }
}
