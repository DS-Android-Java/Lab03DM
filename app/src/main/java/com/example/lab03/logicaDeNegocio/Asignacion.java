package com.example.lab03.logicaDeNegocio;

import java.io.Serializable;

public class Asignacion implements Serializable {

    private String codigo;
    private String cedula;

    public Asignacion(String codigo, String cedula) {
        this.codigo = codigo;
        this.cedula = cedula;
    }

    public Asignacion() {
        codigo = new String();
        cedula = new String();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    @Override
    public String toString() {
        return "Asignacion{" + "codigo=" + codigo + ", cedula=" + cedula + '}';
    }
}
