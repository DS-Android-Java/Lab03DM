package com.example.lab03.accesoDatos;

import com.example.lab03.logicaDeNegocio.Carrera;
import com.example.lab03.logicaDeNegocio.Curso;
import com.example.lab03.logicaDeNegocio.Profesor;
import com.example.lab03.logicaDeNegocio.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ModelData {
    private List<Profesor> listaProfesor;
    private List<Curso> listaCurso;
    private List<Carrera> listaCarrera;
    private List<Usuario> listaUsuario;

    private static ModelData modelData;

    private ModelData(List<Profesor> listaProfesor, List<Curso> listaCurso, List<Carrera> listaCarrera, List<Usuario> listaUsuario) {
        this.listaProfesor = listaProfesor;
        this.listaCurso = listaCurso;
        this.listaCarrera = listaCarrera;
        this.listaUsuario = listaUsuario;
    }

    private ModelData() {
        listaProfesor = new ArrayList<>();
        listaCarrera = new ArrayList<>();
        listaCurso = new ArrayList<>();
        listaUsuario = new ArrayList<>();
    }

    public static ModelData getInstance(){
        if(modelData == null){
            modelData = new ModelData();
        }
        return modelData;
    }

    public List<Profesor> getListaProfesor() {
        return listaProfesor;
    }

    public void setListaProfesor(List<Profesor> listaProfesor) {
        this.listaProfesor = listaProfesor;
    }

    public List<Curso> getListaCurso() {
        return listaCurso;
    }

    public void setListaCurso(List<Curso> listaCurso) {
        this.listaCurso = listaCurso;
    }

    public List<Carrera> getListaCarrera() {
        return listaCarrera;
    }

    public void setListaCarrera(List<Carrera> listaCarrera) {
        this.listaCarrera = listaCarrera;
    }

    public List<Usuario> getListaUsuario() {
        return listaUsuario;
    }

    public void setListaUsuario(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }
}
