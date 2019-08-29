/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.core;


/**
 *
 * @author pegroman
 */
public class SimpleStorage {

  private String name;
  private double currentSize;
  private double capacity;
  private double latency;
  private double maxTransferRate;
  private SistemaArchivos fileSystem;

    public SimpleStorage(String name,  double capacity) {
        this.name = name;
        this.capacity = capacity;
        init(name);
    }
    
    /**
     * The initialization of the hard drive is done in this method. The most common parameters, such
     * as latency and maximum transfer rate are set. The default values are set
     * to simulate the "Maxtor DiamonMax 10 ATA" hard disk.
     */
    private void init(String aName) {
	currentSize = 0;
	latency = 0.00417;     // 4.17 ms in seconds
	maxTransferRate = 133; // in MB/sec
        fileSystem = new SistemaArchivos("FileSystem" +aName);
}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(double currentSize) {
        this.currentSize = currentSize;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getLatency() {
        return latency;
    }

    public void setLatency(double latency) {
        this.latency = latency;
    }

    public double getMaxTransferRate() {
        return maxTransferRate;
    }

    public void setMaxTransferRate(double maxTransferRate) {
        this.maxTransferRate = maxTransferRate;
    }

    public SistemaArchivos getFileSystem() {
        return fileSystem;
    }
    
    public int getFSystemId(){
        return this.getFileSystem().getId();
    }

    public void setFileSystem(SistemaArchivos fileSystem) {
        this.fileSystem = fileSystem;
    }
  
  
    
}
