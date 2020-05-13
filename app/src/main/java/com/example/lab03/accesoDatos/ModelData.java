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

        initCarreras();
        //initProfesores();//por que como ya los estoy sacando de la base para que no me salgan los quemados
        //initCursos();
    }

    public static ModelData getInstance(){
        if(modelData == null){
            modelData = new ModelData();
        }
        return modelData;
    }

    public List<Carrera> initCarreras() {
        listaCarrera = new ArrayList<>();
        initCursos();
        Carrera carrera = new Carrera("EIF", "Ingeniería en Sistemas");
        carrera.addCurso(listaCurso.get(0));
        carrera.addCurso(listaCurso.get(1));
        carrera.addCurso(listaCurso.get(2));
        carrera.addCurso(listaCurso.get(3));
        carrera.addCurso(listaCurso.get(4));
        carrera.addCurso(listaCurso.get(5));
        carrera.addCurso(listaCurso.get(6));
        carrera.addCurso(listaCurso.get(7));
        carrera.addCurso(listaCurso.get(8));
        carrera.addCurso(listaCurso.get(9));
        carrera.addCurso(listaCurso.get(10));
        carrera.addCurso(listaCurso.get(11));
        listaCarrera.add(carrera);

        carrera = new Carrera("ADM", "Administración");
        carrera.addCurso(listaCurso.get(12));
        carrera.addCurso(listaCurso.get(13));
        listaCarrera.add(carrera);

        carrera = new Carrera("FIS", "Física");
        carrera.addCurso(listaCurso.get(14));
        carrera.addCurso(listaCurso.get(15));
        listaCarrera.add(carrera);

        carrera = new Carrera("MAT", "Matemáticas");
        carrera.addCurso(listaCurso.get(16));
        carrera.addCurso(listaCurso.get(17));
        listaCarrera.add(carrera);
        return listaCarrera;
    }

    public List<Profesor> initProfesores() {
        listaProfesor = new ArrayList<>();
        Profesor profesor = new Profesor("123", "Jose","87390476", "@jose");
        listaProfesor.add(profesor);

        profesor = new Profesor("234", "Juan", "87390426","@juan");
        listaProfesor.add(profesor);

        profesor = new Profesor("345", "Mario","62439022" ,"@mario");
        listaProfesor.add(profesor);

        profesor = new Profesor("456", "Jesus", "27281923","@Jesus");
        listaProfesor.add(profesor);
        return listaProfesor;
    }

    public void initCursos(){
        initProfesores();
        //CURSOS CARRERA ING SISTEMAS
        listaCurso.add(new Curso("ST", "EIF","Soporte", "3","1","II","12",
                listaProfesor.get(0)));
        listaCurso.add(new Curso("FD", "EIF", "Fundamentos", "3", "1","I","12",
                listaProfesor.get(2)));
        listaCurso.add(new Curso("PG1", "EIF", "Programación I", "3", "1","II","12",
                listaProfesor.get(2)));
        listaCurso.add(new Curso("PG2", "EIF", "Programación II", "4", "2","I","12",
                listaProfesor.get(1)));
        listaCurso.add(new Curso("PG3", "EIF", "Programación III", "5" ,"2","II","12",
                listaProfesor.get(1)));
        listaCurso.add(new Curso("PG4", "EIF", "Programación IV", "6", "3","I","12",
                listaProfesor.get(1)));
        listaCurso.add(new Curso("EDA", "EIF", "Estructuras de Datos", "4", "3","I","12",
                listaProfesor.get(3)));
        listaCurso.add(new Curso("EDI", "EIF", "Estructuras Discretas", "3", "1","II","12",
                listaProfesor.get(3)));
        listaCurso.add(new Curso("MV", "EIF", "Dispositivos Móviles", "5", "4","I","12",
                listaProfesor.get(3)));
        listaCurso.add(new Curso("AQ", "EIF", "Arquitectura", "5", "3","I","12",
                listaProfesor.get(1)));
        listaCurso.add(new Curso("PP", "EIF", "Paradigmas", "4", "3","II","12",
                listaProfesor.get(1)));
        listaCurso.add(new Curso("RD", "EIF", "Redes", "5", "3","II","12",
                listaProfesor.get(0)));
        //CURSOS CARRERA ADMIN
        listaCurso.add(new Curso("FAD", "ADM","Fundamentos de Administración", "3", "4","I","1",
                listaProfesor.get(2)));
        listaCurso.add(new Curso("C1", "ADM","Contabilidad I", "3", "4","II","2",
                listaProfesor.get(2)));
        //CURSOS CARRERA FISICA
        listaCurso.add(new Curso("FF", "FIS","Fundamentos de Física", "3", "4","I","15",
                listaProfesor.get(0)));
        listaCurso.add(new Curso("F1", "FIS","Física I","3", "4","II","10",
                listaProfesor.get(2)));

        //CURSOS CARRERA MATEMATICA
        listaCurso.add(new Curso("FM", "MAT","Fundamentos de Matemáticas", "3", "4","I","10",
                listaProfesor.get(1)));
        listaCurso.add(new Curso("HB1", "MAT","Historia Básica I", "3", "4","II","12",
                listaProfesor.get(3)));
    }

    public List<Usuario> getUsuariosList() {
        List<Usuario> users = new ArrayList<>();
        users.add(new Usuario("@diego", "diego", "administrador"));
        users.add(new Usuario("@allison", "allimv", "administrador"));
        users.add(new Usuario("@vane", "vanessa", "matriculador"));
        return users;
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
