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
import java.util.ArrayList;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.SimpleStorage;
import org.cloudbus.cloudsim.core.Rpc;

/**
 * Represents a Virtual Machine (VM) that runs inside a Host, sharing a hostList
 * with other VMs. It processes cloudlets. This processing happens according to
 * a policy, defined by the CloudletScheduler. Each VM has a owner, which can
 * submit cloudlets to the VM to execute them.
 *
 * @author pegroman
 */
public class NfsServerVm extends Vm {

    public SimpleStorage nfs;
    private String sharedDir;
    private ArrayList<NfsClientVm> client_list;

    public NfsServerVm(
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
            Rpc rpc_server
    ) {
        super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler, rpc_server);
        this.nfs = new SimpleStorage("SERVER_STORAGE", storage);
        this.sharedDir = "";
        this.client_list = new ArrayList<>();
    }

    public SimpleStorage getStorage() {
        return nfs;
    }

    public int getIdFs() {
        return getStorage().getFileSystem().getId();
    }

    public Object getLocalFileSystem() {
        return getStorage().getFileSystem().getFs();
    }

    public String getSharedDir() {
        return sharedDir;
    }

    public void setSharedDir(String sharedDir) {
        this.sharedDir = sharedDir;
    }

    public ArrayList<NfsClientVm> getClient_list() {
        return client_list;
    }

    public void setClient_list(ArrayList<NfsClientVm> client_list) {
        this.client_list = client_list;
    }
}
