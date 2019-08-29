/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.unaj.lab215.estructuras;

import java.util.ArrayList;
import java.util.Iterator;
import ar.edu.unaj.lab215.syntethic.Archivo;
import ar.edu.unaj.lab215.syntethic.Directorio;
import ar.edu.unaj.lab215.syntethic.Elemento;

/**
 *
 * @author pegroman
 */
public class ArbolGeneral {

    private NodoGeneral raiz;

    public NodoGeneral getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoGeneral raiz) {
        this.raiz = raiz;
    }

    public Object getDatoRaiz() {
        return raiz.getDato();
    }

    public ArbolGeneral(Object dato) {
        this.raiz = new NodoGeneral(dato);
    }

    public ArbolGeneral() {
    }

    public ArrayList<ArbolGeneral> getHijos() {
        Iterator<ArbolGeneral> rec_hijos = this.getRaiz().getHijos().iterator();
        ArrayList<ArbolGeneral> lista = new ArrayList<>();
        while (rec_hijos.hasNext()) {
            ArbolGeneral arbol = new ArbolGeneral(rec_hijos.next());
            lista.add(lista.size(), arbol);
        }
        return lista;
    }

    public void agregarHijo(ArbolGeneral hijo) {
        ArrayList<ArbolGeneral> lista = this.getRaiz().getHijos();
        lista.add(lista.size(), hijo);
    }

    public void preOrden() {
        System.out.println(this.getDatoRaiz());
        ArrayList<ArbolGeneral> hijos = this.getRaiz().getHijos();
        Iterator<ArbolGeneral> rec_hijos = hijos.iterator();
        while (rec_hijos.hasNext()) {
            rec_hijos.next().preOrden();
        }
    }

    public static ArbolGeneral crearArbolArchivo(String nombre, Double tamanio, Double tiempo_proc, Double tiempo_crear) {
        ArbolGeneral arbol_aux;
        Archivo archivo_aux = new Archivo(nombre, tamanio, tiempo_proc, tiempo_crear);
        return arbol_aux = new ArbolGeneral(archivo_aux);
    }

    public static ArbolGeneral crearArbolDirectorio(String nombre, Double tamanio, Double tiempo_proc, Double tiempo_crear) {
        ArbolGeneral arbol_aux;
        Directorio dir_aux = new Directorio(nombre, tamanio, tiempo_proc, tiempo_crear);
        return arbol_aux = new ArbolGeneral(dir_aux);
    }

    public static ArbolGeneral crearArbolArchivoContent(String nombre, Double tamanio, Double tiempo_proc, Double tiempo_crear, String content) {
        ArbolGeneral arbol_aux;
        Archivo archivo_aux = new Archivo(nombre, tamanio, tiempo_proc, tiempo_crear, content);
        return arbol_aux = new ArbolGeneral(archivo_aux);
    }

    @Override
    public String toString() {
        return "ArbolGeneral{" + "raiz=" + raiz + '}';
    }

    public ArbolGeneral getHijo(String nombre) {
        ArbolGeneral aux = null;
        ArrayList<ArbolGeneral> hijos = this.getRaiz().getHijos();
        
        for (int i = 0; i < hijos.size(); i++) {
            if (nombre.equals(((Elemento)hijos.get(i).getDatoRaiz()).getNombre())) {
                aux = hijos.get(i);
                break;
            }
        }
        return aux;
    }

//    public static void main(String[] args) {
//        ArbolGeneral fs = crearArbolDirectorio("/", 0.3, 0.15, 0.11);
//        ArbolGeneral etc = crearArbolDirectorio("etc", 0.5, 0.25, 0.21);
//        ArbolGeneral home = crearArbolDirectorio("home", 0.2, 0.1, 0.12);
//        ArbolGeneral config = crearArbolArchivo(".config", 0.15, 0.125, 0.0625);
//        fs.agregarHijo(etc);
//        home.agregarHijo(config);
//        fs.agregarHijo(home);
//        fs.getHijo("etc");
//        System.out.println(fs.getHijo("etc").getDatoRaiz());
//        
//    }

}
