package ar.edu.unaj.lab215.syntethic;

/**
 *
 * @author pegroman
 */
public class Archivo extends Elemento {

    protected String identificador;
    private String contenido;

    public Archivo(String nombre, Double tamanio, Double tiempo_proc, Double tiempo_creacion) {
        super(nombre, 0.01 + tamanio, tiempo_proc, tiempo_creacion);
        this.identificador = "Archivo";
        this.contenido = "";
    }
    
    public Archivo(String nombre, Double tamanio, Double tiempo_proc, Double tiempo_creacion, String contenido) {
        super(nombre, 0.01 + tamanio, tiempo_proc, tiempo_creacion);
        this.identificador = "Archivo";
        this.contenido = contenido;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    @Override
    public String toString() {
        return identificador + ": " + super.toString();
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    

}
