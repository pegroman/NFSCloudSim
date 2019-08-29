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

import org.cloudbus.cloudsim.core.Ior;
import org.cloudbus.cloudsim.core.Rpc;
import ar.edu.unaj.lab215.estructuras.ArbolGeneral;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.SimpleStorage;
import org.cloudbus.cloudsim.core.VirtualFileSystem;

/**
 * Represents a Virtual Machine (VM) that runs inside a Host, sharing a hostList
 * with other VMs. It processes cloudlets. This processing happens according to
 * a policy, defined by the CloudletScheduler. Each VM has a owner, which can
 * submit cloudlets to the VM to execute them.
 *
 * @author pegroman
 */
public class NfsClientVm extends Vm {

    public SimpleStorage fs;
    public VirtualFileSystem vfs;
    public Ior iorApp;

    public NfsClientVm(
            int id,
            int userId,
            double mips,
            int numberOfPes,
            int ram,
            long bw,
            long size,
            double storage,
            String vmm,
            CloudletScheduler cloudletScheduler,
            Rpc rpc_client
    ) {
        super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler, rpc_client);
        this.fs = new SimpleStorage("disk" + id, storage);
        this.vfs = new VirtualFileSystem("vfs" + id);  //en donde se monta server
        this.iorApp = new Ior("IOR_APP" + id, "NOTHING"); //Benchmark IOR
    }

    public SimpleStorage getStorage() {
        return fs;
    }

    public VirtualFileSystem getVfs() {
        return vfs;
    }

    public int getIdFs() {
        return getStorage().getFileSystem().getId();
    }

    public int getIdVfs() {
        return getVfs().getId();
    }

    public ArbolGeneral getLocalFileSystem() {
        return getStorage().getFileSystem().getFs();
    }

    public Ior getIorApp() {
        return iorApp;
    }

    public int getIdIor() {
        return getIorApp().getId();
    }
    
    public String getOperacion(){
        return getIorApp().getOperacion_actual();
    }

}
