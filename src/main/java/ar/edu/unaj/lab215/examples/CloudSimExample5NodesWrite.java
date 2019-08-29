/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.unaj.lab215.examples;

/*
 * Title: Ejemplo que corre ejecuta write!      
 */
import org.cloudbus.cloudsim.core.Ior;
import ar.edu.unaj.lab215.syntethic.NfsClientVm;
import ar.edu.unaj.lab215.syntethic.NfsServerVm;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.core.SistemaArchivos;
import org.cloudbus.cloudsim.core.VirtualFileSystem;
import org.cloudbus.cloudsim.core.Rpc;

/**
 * A simple example showing how to create a data center with one host and run
 * one cloudlet on it.
 */
public class CloudSimExample5NodesWrite {

    /**
     * The cloudlet list.
     */
    private static List<Cloudlet> cloudletList;
    /**
     * The vmlist.
     */
    //private static List<NfsServerVm> vmlist;
    private static List<Vm> vmlist;

    /**
     * Creates main() to run this example.
     *
     * @param args the args
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Log.printLine("Starting BenchmarkingCloudSimExample1...");
        try {
            // First step: Initialize the CloudSim package. It should be called before creating any entities.
            int num_user = 1; // number of cloud users
            Calendar calendar = Calendar.getInstance(); // Calendar whose fields have been initialized with the current date and time.
            boolean trace_flag = true; // trace events

            CloudSim.init(num_user, calendar, trace_flag);

            // Second step: Create Datacenters
            // Datacenters are the resource providers in CloudSim. We need at
            // list one of them to run a CloudSim simulation
            Datacenter datacenter0 = createDatacenter("Datacenter_0");

            // Third step: Create Broker
            DatacenterBroker broker = createBroker();
            int brokerId = broker.getId();

            // Fourth step: Create one virtual machine
            vmlist = new ArrayList<>();

            // VM description
            int vmid = 0;
            int mips = 500;
            long size = 10000; // image size (MB)
            int ram = 512; // vm memory (MB)
            long bw = 1000;
            int pesNumber = 1; // number of cpus
            double storage = 500000.00; //storage with local file system
            String vmm = "Xen"; // VMM name

            // create VM's
            NfsServerVm vm = new NfsServerVm(vmid, brokerId, mips, pesNumber, ram, bw, size, storage, vmm, new CloudletSchedulerTimeShared(), new Rpc("RPC_SERVER"));

            NfsClientVm vm1 = new NfsClientVm(vmid + 1, brokerId, mips, pesNumber, ram, bw, size, storage, vmm, new CloudletSchedulerTimeShared(), new Rpc("RPC_CLIENT" + vmid));

            NfsClientVm vm2 = new NfsClientVm(vmid + 2, brokerId, mips, pesNumber, ram, bw, size, storage, vmm, new CloudletSchedulerTimeShared(), new Rpc("RPC_CLIENT" + vmid));

            NfsClientVm vm3 = new NfsClientVm(vmid + 3, brokerId, mips, pesNumber, ram, bw, size, storage, vmm, new CloudletSchedulerTimeShared(), new Rpc("RPC_CLIENT" + vmid));

            NfsClientVm vm4 = new NfsClientVm(vmid + 4, brokerId, mips, pesNumber, ram, bw, size, storage, vmm, new CloudletSchedulerTimeShared(), new Rpc("RPC_CLIENT" + vmid));

            //nombre del directorio a compartir
            vm.setSharedDir("sharedNFS");

            //set list of clients
            vm.getClient_list().add(vm1);
            vm.getClient_list().add(vm2);
            vm.getClient_list().add(vm3);
            vm.getClient_list().add(vm4);

            // add the VM to the vmList
            vmlist.add(vm);
            vmlist.add(vm1);
            vmlist.add(vm2);
            vmlist.add(vm3);
            vmlist.add(vm4);

            // submit vm list to the broker
            broker.submitVmList(vmlist);

            // Fifth step: Create one Cloudlet
            cloudletList = new ArrayList<>();

            // Cloudlet properties
            int id = 0;
            long length = 400000;
            long fileSize = 300;
            long outputSize = 300;
            UtilizationModel utilizationModel = new UtilizationModelFull();
            Cloudlet cloudlet = new Cloudlet(id, length, pesNumber, fileSize,
                    outputSize, utilizationModel, utilizationModel,
                    utilizationModel);
            cloudlet.setUserId(brokerId);
            cloudlet.setVmId(vmid);

            Cloudlet cloudlet1 = new Cloudlet(id + 1, length, pesNumber, fileSize,
                    outputSize, utilizationModel, utilizationModel,
                    utilizationModel);
            cloudlet1.setUserId(brokerId);
            cloudlet1.setVmId(vmid + 1);

            Cloudlet cloudlet2 = new Cloudlet(id + 2, length, pesNumber, fileSize,
                    outputSize, utilizationModel, utilizationModel,
                    utilizationModel);
            cloudlet2.setUserId(brokerId);
            cloudlet2.setVmId(vmid + 2);

            Cloudlet cloudlet3 = new Cloudlet(id + 3, length, pesNumber, fileSize,
                    outputSize, utilizationModel, utilizationModel,
                    utilizationModel);
            cloudlet3.setUserId(brokerId);
            cloudlet3.setVmId(vmid + 3);

            Cloudlet cloudlet4 = new Cloudlet(id + 4, length, pesNumber, fileSize,
                    outputSize, utilizationModel, utilizationModel,
                    utilizationModel);
            cloudlet4.setUserId(brokerId);
            cloudlet4.setVmId(vmid + 4);

            // add the cloudlet to the list
            cloudletList.add(cloudlet);
            cloudletList.add(cloudlet1);
            cloudletList.add(cloudlet2);
            cloudletList.add(cloudlet3);
            cloudletList.add(cloudlet4);

            // submit cloudlet list to the broker
            broker.submitCloudletList(cloudletList);

            //crea y monta local file system server
            CloudSim.send(vm.getIdFs(), vm.getIdFs(), 80, SistemaArchivos.CREAR_FSBASICO, null);

            //crea y monta local file system client 
            CloudSim.send(vm1.getIdFs(), vm1.getIdFs(), 84, SistemaArchivos.CREAR_FSBASICO, null);
            CloudSim.send(vm2.getIdFs(), vm2.getIdFs(), 84, SistemaArchivos.CREAR_FSBASICO, null);
            CloudSim.send(vm3.getIdFs(), vm3.getIdFs(), 84, SistemaArchivos.CREAR_FSBASICO, null);
            CloudSim.send(vm4.getIdFs(), vm4.getIdFs(), 84, SistemaArchivos.CREAR_FSBASICO, null);

            //iniciar nfsd. Export
            CloudSim.send(vm.getIdFs(), vm.getIdFs(), 86, SistemaArchivos.EXPORT, vm.getSharedDir());

            //testing mounst
            CloudSim.send(vm1.getIdFs(), vm.getClient_list().get(0).getIdVfs(), 87, VirtualFileSystem.MOUNT, vm.getSharedDir());
            CloudSim.send(vm2.getIdFs(), vm.getClient_list().get(1).getIdVfs(), 87 + 0.25, VirtualFileSystem.MOUNT, vm.getSharedDir());
            CloudSim.send(vm3.getIdFs(), vm.getClient_list().get(2).getIdVfs(), 87, VirtualFileSystem.MOUNT, vm.getSharedDir());
            CloudSim.send(vm4.getIdFs(), vm.getClient_list().get(3).getIdVfs(), 87 + 0.25, VirtualFileSystem.MOUNT, vm.getSharedDir());

            //send client list to rpc server
            CloudSim.send(vm.getRpc().getId(), vm.getRpc().getId(), 87.5, Rpc.CLIENT_LIST, vm.getClient_list());

            //Ejecuto IOR, en cliente. El tipo de operacion es write
            //Object[] parametros = {"WRITE", 256.00, 256.00, 4, "LINEAL"}; // 0->operación; 1->bsize; 2->tsize; 3->scount; 4->regresion
            //Object[] parametros = {"WRITE", 256.00, 256.00, 4, "EXPONENCIAL"}; // 0->operación; 1->bsize; 2->tsize; 3->scount; 4->regresion
            //Object[] parametros = {"WRITE", 512.00, 512.00, 4, "EXPONENCIAL"};
            //Object[] parametros = {"WRITE", 512.00, 512.00, 4, "LINEAL"};
            //Object[] parametros = {"WRITE", 512.00, 512.00, 6, "EXPONENCIAL"};
            //Object[] parametros = {"WRITE", 512.00, 512.00, 6, "LINEAL"};
            //Object[] parametros = {"WRITE", 512.00, 512.00, 8, "EXPONENCIAL"};
            //Object[] parametros = {"WRITE", 512.00, 512.00, 8, "LINEAL"};
            //Object[] parametros = {"WRITE", 256.00, 256.00, 6, "LINEAL"}; //1.5GB
            //Object[] parametros = {"WRITE", 512.00, 512.00, 5, "LINEAL"}; //2.5GB
            Object[] parametros = {"WRITE", 512.00, 512.00, 7, "LINEAL"}; //3.5GB
            CloudSim.send(vm1.getIdFs(), vm1.getIdIor(), 88, Ior.OPERACION, parametros);

            CloudSim.send(vm1.getIdIor(), vm1.getRpc().getId(), 89, Rpc.SET_OP, vm1.getIdIor());

            // Sixth step: Starts the simulation
            CloudSim.startSimulation();

            CloudSim.stopSimulation();

            //Final step: Print results when simulation is over
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            printCloudletList(newList);

            Log.printLine("Benchmarking CloudSim Example1 finished!");
        } catch (NullPointerException e) {
            Log.printLine("Unwanted errors happen");
        }
    }

    /**
     * Creates the datacenter.
     *
     * @param name the name
     *
     * @return the datacenter
     */
    private static Datacenter createDatacenter(String name) {

        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store
        // our machine
        List<Host> hostList = new ArrayList<>();

        // 2. A Machine contains one or more PEs or CPUs/Cores.
        // In this example, it will have only one core.
        List<Pe> peList = new ArrayList<>();

        int mips = 1000000;

        // 3. Create PEs and add these into a list.
        peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

        // 4. Create Host with its id and list of PEs and add them to the list
        // of machines
        int hostId = 0;
        int ram = 8192; // host memory (MB)
        long storage = 1000000; // host storage
        int bw = 10000;

        hostList.add(
                new Host(
                        hostId,
                        new RamProvisionerSimple(ram),
                        new BwProvisionerSimple(bw),
                        storage,
                        peList,
                        new VmSchedulerTimeShared(peList)
                )
        ); // This is our machine

        // 5. Create a DatacenterCharacteristics object that stores the
        // properties of a data center: architecture, OS, list of
        // Machines, allocation policy: time- or space-shared, time zone
        // and its price (G$/Pe time unit).
        String arch = "x86"; // system architecture
        String os = "Linux"; // operating system
        String vmm = "Xen";
        double time_zone = 10.0; // time zone this resource located
        double cost = 3.0; // the cost of using processing in this resource
        double costPerMem = 0.05; // the cost of using memory in this resource
        double costPerStorage = 0.001; // the cost of using storage in this
        // resource
        double costPerBw = 0.0; // the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<>(); // we are not adding SAN
        // devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem,
                costPerStorage, costPerBw);

        // 6. Finally, we need to create a PowerDatacenter object.
        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
        }

        return datacenter;
    }

    // We strongly encourage users to develop their own broker policies, to
    // submit vms and cloudlets according
    // to the specific rules of the simulated scenario
    /**
     * Creates the broker.
     *
     * @return the datacenter broker
     */
    private static DatacenterBroker createBroker() {
        DatacenterBroker broker;
        try {
            broker = new DatacenterBroker("Broker");
        } catch (Exception e) {
            return null;
        }
        return broker;
    }

    /**
     * Prints the Cloudlet objects.
     *
     * @param list list of Cloudlets
     */
    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
                + "Data center ID" + indent + "VM ID" + indent + "Time" + indent
                + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");

                Log.printLine(indent + indent + cloudlet.getResourceId()
                        + indent + indent + indent + cloudlet.getVmId()
                        + indent + indent
                        + dft.format(cloudlet.getActualCPUTime()) + indent
                        + indent + dft.format(cloudlet.getExecStartTime())
                        + indent + indent
                        + dft.format(cloudlet.getFinishTime()));
            }
        }
    }
}
