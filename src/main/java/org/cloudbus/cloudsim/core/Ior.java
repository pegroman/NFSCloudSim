/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.core;

import ar.edu.unaj.lab215.syntethic.SyntethicTags;
import org.cloudbus.cloudsim.Log;

/**
 *
 * @author pegroman
 */
public class Ior extends SimEntity {

    private String operacion_actual;
    private double blockSize;
    private double transferSize;
    private int segmentCount;
    private String tipo_regresión;
    private final static String API = "POSIX";
    public static final short OPERACION = SyntethicTags.SYNTETHIC_BASE + 12;
    public static final short RESULTS = SyntethicTags.SYNTETHIC_BASE + 22;

    public Ior(String name, String operacion) {
        super(name);
        this.operacion_actual = operacion;
        this.blockSize = 0.00;
        this.transferSize = 0.00;
        this.segmentCount = 0;
        this.tipo_regresión = "LINEAL";
    }

    @Override
    public void startEntity() {
        Log.printLine("Installing Ior Benchmark... " + this.getId());
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
            case Ior.OPERACION:
                setOperacion_actual((String) ((Object[]) ev.getData())[0]);
                this.setBlockSize((Double) ((Object[]) ev.getData())[1]);
                this.setTransferSize((Double) ((Object[]) ev.getData())[2]);
                this.setSegmentCount((int) ((Object[]) ev.getData())[3]);
                this.setTipo_regresión((String) ((Object[]) ev.getData())[4]);
                Log.printLine("Operacion: " + getOperacion_actual());
                CloudSim.resumeSimulation();
                break;
            case RESULTS:
                Double total_time = this.time();
                this.output(total_time);
                CloudSim.resumeSimulation();
                break;
            default:
                Log.printLine("Error");
                break;
        }
    }

    @Override
    public void shutdownEntity() {
        Log.printLine("Ior operation " + this.getOperacion_actual() + " complete " + this.getId());
    }

    public String getOperacion_actual() {
        return operacion_actual;
    }

    public void setOperacion_actual(String operacion_actual) {
        this.operacion_actual = operacion_actual;
    }

    public double getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(double blockSize) {
        this.blockSize = blockSize;
    }

    public double getTransferSize() {
        return transferSize;
    }

    public void setTransferSize(double transferSize) {
        this.transferSize = transferSize;
    }

    public int getSegmentCount() {
        return segmentCount;
    }

    public void setSegmentCount(int segmentCount) {
        this.segmentCount = segmentCount;
    }

    public String getTipo_regresión() {
        return tipo_regresión;
    }

    public void setTipo_regresión(String tipo_regresión) {
        this.tipo_regresión = tipo_regresión;
    }

    public double linealRegresion(double fileSize) {
        double time = 0.00;
        switch (this.getOperacion_actual()) {
            case "READ":
                time = (16.8056 * fileSize) + 1.63;
                break;
            case "WRITE":
                time = (23.7843 * fileSize) - 0.2575;
                break;
            default:
                Log.printLine("Error lineal regresion");
                break;
        }
        return time;
    }

    public double exponencialRegresion(double fileSize) {
        double time = 0.00;
        switch (this.getOperacion_actual()) {
            case "READ":
                time = (12.717 * Math.exp(0.4442 * fileSize));
                break;
            case "WRITE":
                time = (16.3524 * Math.exp(0.4638 * fileSize));
                break;
            default:
                Log.printLine("Error lineal regresion");
                break;
        }
        return time;
    }

    public double megaToGiga(double file_size) {
        return  (file_size / 1024);
    }

    public double time() {
        Double total_time = 0.00;
        switch (this.getTipo_regresión()) {
            case "LINEAL":
                total_time = this.linealRegresion(this.megaToGiga(this.getBlockSize() * this.getSegmentCount()));
                break;
            case "EXPONENCIAL":
                total_time = this.exponencialRegresion(this.megaToGiga(this.getBlockSize() * this.getSegmentCount()));
                break;
            default:
                Log.printLine("Error en RESULT IOR");
                break;
        }
        return total_time;
    }

    public void output(Double total_time) {
        double fileSize = this.megaToGiga(this.getBlockSize() * this.getSegmentCount());
        Log.printLine("Display results...");
        Log.printLine("######### IOR TEST #########");
        Log.printLine("operation: " + this.getOperacion_actual());
        Log.printLine("API: " + API);
        Log.printLine("access: single shared file");
        Log.printLine("ordering in a file: sequencial offsets");
        Log.printLine("xfersize: " + this.getTransferSize() + "MB");
        Log.printLine("blocksize: " + this.getBlockSize() + "MB");
        Log.printLine("aggregate filesize (GB): " + fileSize );
        Double random_time = total_time + (Math.random() * (2.572566603 + 2.572566603) - 2.572566603);
        if ("WRITE".equals(this.getOperacion_actual())) {
            random_time = total_time + (Math.random() * (2.354250088 + 2.354250088) - 2.354250088);
            Log.printConcatLine("total time (" + this.getTipo_regresión() + "): " + random_time + " seconds");
        } else {
            Log.printConcatLine("total time (" + this.getTipo_regresión() + "): " + random_time + " seconds");
        }
        Log.printLine("Max bandwith (MB/s): " + (this.getBlockSize() * this.getSegmentCount()) / random_time);
        Log.printLine("###### END IOR TEST #######");
    }
}
