package com.example.lab03.logicaDeNegocio;

import java.io.Serializable;

public class Curso implements Serializable {
    private String codigo;
    private String codCarrera;
    private String nombre;
    private String creditos;
    private String anio;
    private String ciclo;
    private String hora_semanales;
    private Profesor profesor;

    public Curso(String codigo, String codCarrera, String nombre, String creditos, String anio, String ciclo, String hora_semanales, Profesor profesor) {
        this.codigo = codigo;
        this.codCarrera = codCarrera;
        this.nombre = nombre;
        this.creditos = creditos;
        this.anio = anio;
        this.ciclo = ciclo;
        this.hora_semanales = hora_semanales;
        this.profesor = profesor;
    }



    public Curso() {
        codigo = new String();
        codCarrera = new String();
        nombre = new String();
        creditos = new String();
        anio = new String();
        ciclo = new String();
        hora_semanales = new String();
        profesor = new Profesor();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodCarrera() {
        return codCarrera;
    }

    public void setCodCarrera(String codCarrera) {
        this.codCarrera = codCarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCreditos() {
        return creditos;
    }

    public void setCreditos(String creditos) {
        this.creditos = creditos;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getCiclo() {
        return ciclo;
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }

    public String getHora_semanales() {
        return hora_semanales;
    }

    public void setHora_semanales(String hora_semanales) {
        this.hora_semanales = hora_semanales;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    @Override
    public String toString() {
        return "Curso{" + "codigo=" + codigo + ", codCarrera=" + codCarrera + ", nombre=" + nombre + ", creditos=" + creditos + ", anio=" + anio + ", ciclo=" + ciclo + ", hora_semanales=" + hora_semanales + ", profesor=" + profesor + '}';
    }
}
