/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.unaj.lab215.estructuras;

import java.util.ArrayList;

/**
 *
 * @author pegroman
 */
public class NodoGeneral {

    private Object dato;
    private ArrayList<ArbolGeneral> hijos;

    public Object getDato() {
        return dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }

    public ArrayList<ArbolGeneral> getHijos() {
        return hijos;
    }

    public void setHijos(ArrayList<ArbolGeneral> hijos) {
        this.hijos = hijos;
    }

    public NodoGeneral(Object dato) {
        this.dato = dato;
        this.hijos = new ArrayList<>();
    }

    @Override
    public String toString() {
        return  this.dato.toString() ;
    }
    
    

}
