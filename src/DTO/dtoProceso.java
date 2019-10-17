
package DTO;

import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author Juan
 */
public class dtoProceso {

    private Integer Identificador;
    private Integer EstadoCPU;
    private Integer Procesador;
    private Pair<Integer,Integer> Memoria;
    private String EstadoProceso;
    private ArrayList<String> Recursos;
    private String Planificador;
    private Integer Prioridad;
    private Integer Contabilizacion;
    private Integer Ancestro;
    private ArrayList<Integer> Descendientes;

    public Integer getIdentificador() {
        return Identificador;
    }

    public void setIdentificador(Integer Identificador) {
        this.Identificador = Identificador;
    }

    public Integer getEstadoCPU() {
        return EstadoCPU;
    }

    public void setEstadoCPU(Integer EstadoCPU) {
        this.EstadoCPU = EstadoCPU;
    }

    public Integer getProcesador() {
        return Procesador;
    }

    public void setProcesador(Integer Procesador) {
        this.Procesador = Procesador;
    }

    public Pair<Integer, Integer> getMemoria() {
        return Memoria;
    }

    public void setMemoria(Pair<Integer, Integer> Memoria) {
        this.Memoria = Memoria;
    }

    public String getEstadoProceso() {
        return EstadoProceso;
    }

    public void setEstadoProceso(String EstadoProceso) {
        this.EstadoProceso = EstadoProceso;
    }

    public ArrayList<String> getRecursos() {
        return Recursos;
    }

    public void setRecursos(ArrayList<String> Recursos) {
        this.Recursos = Recursos;
    }

    public String getPlanificador() {
        return Planificador;
    }

    public void setPlanificador(String Planificador) {
        this.Planificador = Planificador;
    }

    public Integer getPrioridad() {
        return Prioridad;
    }

    public void setPrioridad(Integer Prioridad) {
        this.Prioridad = Prioridad;
    }

    public Integer getContabilizacion() {
        return Contabilizacion;
    }

    public void setContabilizacion(Integer Contabilizacion) {
        this.Contabilizacion = Contabilizacion;
    }

    public Integer getAncestro() {
        return Ancestro;
    }

    public void setAncestro(Integer Ancestro) {
        this.Ancestro = Ancestro;
    }

    public ArrayList<Integer> getDescendientes() {
        return Descendientes;
    }

    public void setDescendientes(ArrayList<Integer> Descendientes) {
        this.Descendientes = Descendientes;
    }
    
    public dtoProceso() {
        this.Identificador = Identificador;
        this.EstadoCPU = EstadoCPU;
        this.Procesador = Procesador;
        this.Memoria = Memoria;
        this.EstadoProceso = EstadoProceso;
        this.Recursos = Recursos;
        this.Planificador = Planificador;
        this.Prioridad = Prioridad;
        this.Contabilizacion = Contabilizacion;
        this.Ancestro = Ancestro;
        this.Descendientes = Descendientes;
    }
    
    public dtoProceso(Integer Identificador, Integer EstadoCPU, Integer Procesador, Pair<Integer, Integer> Memoria, String EstadoProceso, ArrayList<String> Recursos, String Planificador, Integer Prioridad, Integer Contabilizacion, Integer Ancestro, ArrayList<Integer> Descendientes) {
        this.Identificador = Identificador;
        this.EstadoCPU = EstadoCPU;
        this.Procesador = Procesador;
        this.Memoria = Memoria;
        this.EstadoProceso = EstadoProceso;
        this.Recursos = Recursos;
        this.Planificador = Planificador;
        this.Prioridad = Prioridad;
        this.Contabilizacion = Contabilizacion;
        this.Ancestro = Ancestro;
        this.Descendientes = Descendientes;
    }

}
