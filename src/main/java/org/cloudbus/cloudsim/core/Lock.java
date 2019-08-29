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
public class Lock extends SimEntity {

    private boolean locked;

    public static final short LOCK = SyntethicTags.SYNTETHIC_BASE + 25;
    public static final short UNLOCK = SyntethicTags.SYNTETHIC_BASE + 26;

    public Lock(String name) {
        super(name);
        this.locked = false;
    }

    @Override
    public void startEntity() {
        Log.printLine("Init Lock protocol " + this.getId());
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
            case LOCK:
                Log.printLine("Lock file " + ev.getData());
                this.setLocked(true);
                CloudSim.resumeSimulation();
                break;
            case UNLOCK:
                Log.printLine("Unlock file " + ev.getData());
                this.setLocked(false);
                CloudSim.resumeSimulation();
                break;
            default:
                Log.printLine("Error");
                break;
        }
    }

    @Override
    public void shutdownEntity() {
        Log.printLine("Shutdown Lock protocol " + this.getId());
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
