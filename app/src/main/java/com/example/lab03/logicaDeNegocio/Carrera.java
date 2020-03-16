package com.example.lab03.logicaDeNegocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Carrera implements Serializable {

    private String codCarrera;
    private String nombre;
    private String titulo;
    private List<Curso> cursos;

    public Carrera(String codCarrera, String nombre, String titulo) {
        this.codCarrera = codCarrera;
        this.nombre = nombre;
        this.titulo = titulo;
        this.cursos = new ArrayList<>();
    }

    public Carrera(String codCarrera, String nombre) {
        this.codCarrera = codCarrera;
        this.nombre = nombre;
        this.titulo = "Bachiller";
        this.cursos = new ArrayList<>();
    }

    public Carrera() {
        codCarrera = new String();
        nombre = new String();
        titulo = new String();
        cursos = new ArrayList<>();
    }

    public void addCurso(Curso curso){
        this.cursos.add(curso);
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return "codCarrera: " + codCarrera + "\n"+
                "Nombre: " + nombre;
    }
}
