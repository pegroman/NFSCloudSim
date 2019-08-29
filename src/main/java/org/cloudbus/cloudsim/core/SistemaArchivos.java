/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.core;

import ar.edu.unaj.lab215.estructuras.ArbolGeneral;
import static ar.edu.unaj.lab215.estructuras.ArbolGeneral.crearArbolArchivo;
import static ar.edu.unaj.lab215.estructuras.ArbolGeneral.crearArbolArchivoContent;
import static ar.edu.unaj.lab215.estructuras.ArbolGeneral.crearArbolDirectorio;
import ar.edu.unaj.lab215.syntethic.SyntethicTags;
import java.util.ArrayList;
import org.cloudbus.cloudsim.Log;

/**
 *
 * @author pegroman
 */
public class SistemaArchivos extends SimEntity {

    public static final short CREAR_FSBASICO = SyntethicTags.SYNTETHIC_BASE + 7;
    public static final short LISTAR_FSBASICO = SyntethicTags.SYNTETHIC_BASE + 8;
    public static final short CREAR_ARCHIVO = SyntethicTags.SYNTETHIC_BASE + 9;
    public static final short EXPORT = SyntethicTags.SYNTETHIC_BASE + 11;
    public static final short WRITE = SyntethicTags.SYNTETHIC_BASE + 20;
    public static final short READ = SyntethicTags.SYNTETHIC_BASE + 21;
    public static final short WRITE_CLIENT = SyntethicTags.SYNTETHIC_BASE + 23;
    public static final short CREATE_DIR = SyntethicTags.SYNTETHIC_BASE + 24;

    private ArbolGeneral fs;
    private Lock lock;

    public SistemaArchivos(String name) {
        super(name);
        this.lock = new Lock("Lock" + name);
    }

    @Override
    public void startEntity() {
        Log.printLine("Init Local File System ... " + this.getId());
    }

    @Override
    public void processEvent(SimEvent ev) {
        switch (ev.getTag()) {
            case SimEvent.SEND:
                CloudSim.resumeSimulation();
                break;
            case SimEvent.CREATE:
                CloudSim.resumeSimulation();
                break;
            case SimEvent.ENULL:
                CloudSim.resumeSimulation();
                break;
            case SimEvent.HOLD_DONE:
                CloudSim.resumeSimulation();
                break;
            case CREATE_DIR:
                this.getFs().agregarHijo((ArbolGeneral) ev.getData());
                CloudSim.resumeSimulation();
                break;
            case CREAR_FSBASICO:
                fs = crearArbolDirectorio("/", 0.3, 0.15, 0.11);
                ArbolGeneral etc = crearArbolDirectorio("etc", 0.5, 0.25, 0.21);
                ArbolGeneral home = crearArbolDirectorio("home", 0.2, 0.1, 0.12);
                ArbolGeneral config = crearArbolArchivo(".config", 0.15, 0.125, 0.0625);
                fs.agregarHijo(etc);
                home.agregarHijo(config);
                fs.agregarHijo(home);
                CloudSim.resumeSimulation();
                break;
            case LISTAR_FSBASICO:
                System.out.println(ev.eventTime() + ": Listando Sistema de Archivos");
                fs.preOrden();
                CloudSim.resumeSimulation();
                break;
            case CREAR_ARCHIVO:
                System.out.println(ev.eventTime() + ": Creando archivo");
                ArbolGeneral newArchive = crearArbolArchivo("sudoers", 0.1, 0.135, 0.096);
                fs.agregarHijo(newArchive);
                SimEvent resp1 = new SimEvent(
                        SimEvent.SEND,
                        0.5,
                        ev.getDestination(),
                        ev.getSource(),
                        SyntethicTags.SYNTETHIC_BASE,
                        this.fs
                );
                CloudSim.resumeSimulation();
                break;
            case EXPORT:
                this.getFs().agregarHijo(crearArbolDirectorio((String) ev.getData(), 0.5, 0.25, 0.21));
                Log.printLine("Export shared dir: " + ev.getData());
                //this.send(ev.getSource(), ev.eventTime(), LISTAR_FSBASICO, null);
                CloudSim.resumeSimulation();
                break;
            case WRITE:
                Log.printLine("Writing file...");
                double tamanio;
                switch (((ArrayList<Object>) ev.getData()).get(3).toString()) {
                    case "b":
                        tamanio = (double) ((int) ((ArrayList<Object>) ev.getData()).get(2)) / 1000;
                        break;
                    case "k":
                        tamanio = (double) ((int) ((ArrayList<Object>) ev.getData()).get(2));
                        break;
                    default:
                        tamanio = (double) ((int) ((ArrayList<Object>) ev.getData()).get(2)) * 1000.00;
                        break;
                }

                StringBuilder contenido = new StringBuilder("");
                for (int i = 5; i < ((ArrayList<Object>) ev.getData()).size(); i++) {
                    contenido.append(((ArrayList<Object>) ev.getData()).get(i));
                }
                if (this.getLock().isLocked()) {
                    this.send(this.getLock().getId(), ev.eventTime(), Lock.UNLOCK, ((ArrayList<Object>) ev.getData()).get(1).toString());
                }
                ArbolGeneral toWrite = crearArbolArchivoContent(((ArrayList<Object>) ev.getData()).get(1).toString(), tamanio, 0.123, ev.eventTime(), contenido.toString()); //el double fijo se cambia por el que se levante de los tiempos que saquemos en aws
                if (!this.getLock().isLocked()) {
                    this.send(this.getLock().getId(), ev.eventTime(), Lock.LOCK, ((ArrayList<Object>) ev.getData()).get(1).toString());
                    this.getFs().agregarHijo(toWrite);
                    this.send(this.getLock().getId(), ev.eventTime(), Lock.UNLOCK, ((ArrayList<Object>) ev.getData()).get(1).toString());
                }
                this.send(ev.getDestination(), ev.eventTime(), SistemaArchivos.LISTAR_FSBASICO, null);
                Object[] results = {tamanio, ev.eventTime()};
                this.send(((Rpc) CloudSim.getEntity((int) ((ArrayList<Object>) ev.getData()).get(0))).getIor_reference(), ev.eventTime(), Ior.RESULTS, results);
                CloudSim.resumeSimulation();
                break;
            case READ:
                if (this.getLock().isLocked()) {
                    this.send(this.getLock().getId(), ev.eventTime(), Lock.UNLOCK, ((ArrayList<Object>) ev.getData()).get(1).toString());
                }
                Log.printLine("Reading file " + (String) ((ArrayList) ev.getData()).get(1) + "...");
                if (!this.getLock().isLocked()) {
                    this.send(this.getLock().getId(), ev.eventTime(), Lock.LOCK, ((ArrayList<Object>) ev.getData()).get(1).toString());
                    for (int i = 5; i < (int) ((ArrayList) ev.getData()).size(); i++) {
                        Log.printLine("Reading " + ((ArrayList) ev.getData()).get(i) + " " + ((ArrayList) ev.getData()).get(3) + " ...");
                        for (int j = 0; j < (int) ((ArrayList) ev.getData()).get(i); j++) {
                            Log.print("@");
                        }
                        Log.printLine("\n");
                    }
                    this.send(this.getLock().getId(), ev.eventTime(), Lock.UNLOCK, ((ArrayList<Object>) ev.getData()).get(1).toString());
                }
                double tamanio_read;
                switch (((ArrayList<Object>) ev.getData()).get(3).toString()) {
                    case "b":
                        tamanio_read = (double) ((int) ((ArrayList<Object>) ev.getData()).get(2)) / 1000;
                        break;
                    case "k":
                        tamanio_read = (double) ((int) ((ArrayList<Object>) ev.getData()).get(2));
                        break;
                    default:
                        tamanio_read = (double) ((int) ((ArrayList<Object>) ev.getData()).get(2));
                        break;
                }
                Object[] results_read = {tamanio_read, ev.eventTime()};
                this.send(((Rpc) CloudSim.getEntity((int) ((ArrayList<Object>) ev.getData()).get(0))).getIor_reference(), ev.eventTime(), Ior.RESULTS, results_read);
                CloudSim.resumeSimulation();
                break;
            case WRITE_CLIENT:
                Log.printLine("Writing file...");
                double tamanio_;
                switch (((ArrayList<Object>) ev.getData()).get(3).toString()) {
                    case "b":
                        tamanio_ = (double) ((int) ((ArrayList<Object>) ev.getData()).get(2)) / 1000;
                        break;
                    case "k":
                        tamanio_ = (double) ((int) ((ArrayList<Object>) ev.getData()).get(2));
                        break;
                    default:
                        tamanio_ = (double) ((int) ((ArrayList<Object>) ev.getData()).get(2));
                        break;
                }

                StringBuilder contenido_ = new StringBuilder("");
                for (int i = 5; i < ((ArrayList<Object>) ev.getData()).size(); i++) {
                    contenido_.append(((ArrayList<Object>) ev.getData()).get(i));
                }

                ArbolGeneral toWrite_ = crearArbolArchivoContent(((ArrayList<Object>) ev.getData()).get(1).toString(), tamanio_, 0.123, ev.eventTime(), contenido_.toString()); //el double fijo se cambia por el que se levante de los tiempos que saquemos en aws
                this.getFs().agregarHijo(toWrite_);
                this.send(ev.getDestination(), ev.eventTime(), SistemaArchivos.LISTAR_FSBASICO, null);
                CloudSim.resumeSimulation();
                break;
            default:
                Log.printLine("Error");
                break;
        }
    }

    @Override
    public void shutdownEntity() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Log.printLine("Shutdown Local File system");
    }

    public ArbolGeneral getFs() {
        return fs;
    }

    public Lock getLock() {
        return lock;
    }
}
