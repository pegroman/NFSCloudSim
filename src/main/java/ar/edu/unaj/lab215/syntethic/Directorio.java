package ar.edu.unaj.lab215.syntethic;

/**
 *
 * @author pegroman
 */
public class Directorio extends Elemento {

    protected String identificador;

    public Directorio(String nombre, Double tamanio, Double tiempo_proc, Double tiempo_creacion) {
        super(nombre, 0.02 + tamanio, tiempo_proc, tiempo_creacion);
        this.identificador = "Directorio";
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

}
