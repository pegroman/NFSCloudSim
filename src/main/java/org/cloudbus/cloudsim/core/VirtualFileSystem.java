/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.core;

import ar.edu.unaj.lab215.estructuras.ArbolGeneral;
import ar.edu.unaj.lab215.syntethic.SyntethicTags;
import org.cloudbus.cloudsim.Log;

/**
 *
 * @author pegroman
 */
public class VirtualFileSystem extends SimEntity {

    private ArbolGeneral localVfs;
    public static final short MOUNT = SyntethicTags.SYNTETHIC_BASE + 10;

    public VirtualFileSystem(String name) {
        super(name);
    }

    @Override
    public void startEntity() {
        Log.printLine("Trying to mount nfs in client... " + this.getId());
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
            case MOUNT:
                Log.printLine("TESTING MOUNT..." + (String) ev.getData());
                ArbolGeneral aux = ((SistemaArchivos) CloudSim.getEntity("FileSystemSERVER_STORAGE")).getFs().getHijo((String)ev.getData());
                this.localVfs = aux;
                Log.printLine("From vfs to LocalFS... ");
                this.send(ev.getSource(), ev.eventTime(), SistemaArchivos.CREATE_DIR, aux);
                CloudSim.resumeSimulation();
                break;
            default:
                Log.printLine("Error");
                break;
        }
    }

    @Override
    public void shutdownEntity() {
        Log.printLine("umount vfs...");
    }

    public ArbolGeneral getLocalVfs() {
        return localVfs;
    }

    public void setLocalVfs(ArbolGeneral lovalVfs) {
        this.localVfs = lovalVfs;
    }
}
