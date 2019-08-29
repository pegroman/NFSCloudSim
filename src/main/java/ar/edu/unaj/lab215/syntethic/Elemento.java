/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.unaj.lab215.syntethic;

/**
 *
 * @author pegroman
 */
public class Elemento {

    protected String nombre;
    protected Double tamanio;
    protected Double tiempo_proc;
    protected Double tiempo_creacion;

    public Double getTamanio() {
        return tamanio;
    }

    public void setTamanio(Double tamanio) {
        this.tamanio = tamanio;
    }

    public Double getTiempo_proc() {
        return tiempo_proc;
    }

    public void setTiempo_proc(Double tiempo_proc) {
        this.tiempo_proc = tiempo_proc;
    }

    public Double getTiempo_creacion() {
        return tiempo_creacion;
    }

    public void setTiempo_creacion(Double tiempo_creacion) {
        this.tiempo_creacion = tiempo_creacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Elemento(String nombre, Double tamanio, Double tiempo_proc, Double tiempo_creacion) {
        this.nombre = nombre;
        this.tamanio = tamanio;
        this.tiempo_proc = tiempo_proc;
        this.tiempo_creacion = tiempo_creacion;

    }

    @Override
    public String toString() {
        return nombre + ", " + tamanio + "Kb ";
    }

}
