/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.core;

import ar.edu.unaj.lab215.syntethic.NfsClientVm;
import ar.edu.unaj.lab215.syntethic.SyntethicTags;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import org.cloudbus.cloudsim.Log;

/**
 *
 * @author pegroman
 */
public class Rpc extends SimEntity {
    
    private Queue<Object> sendBuffer;
    private Queue<Object> recvBuffer;
    private String operacion;
    private double bufferSize;
    private ArrayList<Object> memory; //Representa memoria ram o en disco. Para rearmar el paquete final y ordenarlo.
    private int fragment_counter;
    private int ior_reference;
    private ArrayList<NfsClientVm> clients;
    
    public static final short SEND_CLIENT = SyntethicTags.SYNTETHIC_BASE + 14;
    public static final short RECV_SERVER = SyntethicTags.SYNTETHIC_BASE + 15;
    public static final short SET_OP = SyntethicTags.SYNTETHIC_BASE + 16;
    public static final short PACKAGE = SyntethicTags.SYNTETHIC_BASE + 17;
    public static final short XDR_CODE = SyntethicTags.SYNTETHIC_BASE + 18;
    public static final short XDR_DECODE = SyntethicTags.SYNTETHIC_BASE + 19;
    public static final short CLIENT_LIST = SyntethicTags.SYNTETHIC_BASE + 22;
    
    public Rpc(String name) {
        super(name);
        this.fragment_counter = 0;
        this.recvBuffer = new LinkedList();
        this.sendBuffer = new LinkedList();
        this.bufferSize = 4.00; //en bytes. El original es con kbytes y es por default. se pude cambiar en la configuración de NFS- VEr wsize y rsize
        this.memory = new ArrayList();
        this.ior_reference = 0;
        this.clients = new ArrayList<>();
    }
    
    @Override
    public void startEntity() {
        Log.printLine("Waiting for request ... " + this.getId());
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
            case CLIENT_LIST:
                this.setClients((ArrayList<NfsClientVm>) ev.getData());
                CloudSim.resumeSimulation();
                break;
            case SEND_CLIENT:
                Log.printLine("To send buffer... ");
                Log.printLine(ev.getData());
                this.getSendBuffer().add((ArrayList) ev.getData());
                CloudSim.send(ev.getSource(), ((Rpc) CloudSim.getEntity("RPC_SERVER")).getId(), ev.eventTime(), Rpc.RECV_SERVER, this.getSendBuffer().remove());
                CloudSim.resumeSimulation();
                break;
            case RECV_SERVER:
                Log.printLine("Decoding data... ");
                this.send(ev.getDestination(), ev.eventTime(), Rpc.XDR_DECODE, ev.getData());
                CloudSim.resumeSimulation();
                break;
            case SET_OP:
                Log.printLine("Recibiendo operación... ");
                this.setOperacion(((Ior) CloudSim.getEntity((int) ev.getData())).getOperacion_actual());
                this.setIor_reference((int) ev.getData());
                Log.printLine("Operación " + this.getOperacion() + " a codificar");
                this.send(ev.getDestination(), ev.eventTime(), Rpc.PACKAGE, this.getOperacion());
                Log.printLine("Entra a 1");
                CloudSim.resumeSimulation();
                break;
            case PACKAGE:
                Log.printLine("Creating package ... ");
                Object[] data = new Object[4];
                Double bsize = ((Ior) CloudSim.getEntity(this.getIor_reference())).getBlockSize();
                int scount = ((Ior) CloudSim.getEntity(this.getIor_reference())).getSegmentCount();
                this.setBufferSize(((Ior) CloudSim.getEntity(this.getIor_reference())).getTransferSize());
                //8 bytes a escribir. Dos paquetes de 4
                data[0] = ev.getData(); //ior reference
                //data[1] = 8; //size
                data[1] = (int) (bsize * scount);
                data[2] = "test.txt";
                data[3] = "m"; //unit
                this.send(ev.getSource(), ev.eventTime(), Rpc.XDR_CODE, data);
                CloudSim.resumeSimulation();
                break;
            case XDR_CODE:
                Log.printLine("Representing data... ");
                ArrayList<Object> xdr_data = new ArrayList<>();
                for (int i = 0; i < ((Object[]) ev.getData()).length; i++) {
                    xdr_data.add(((Object[]) ev.getData())[i]);
                }
                ArrayList<Object> frame = new ArrayList<>();
                int fragments = (int) Math.ceil((int) xdr_data.get(1) / bufferSize);
                int contador = 0;
                for (int k = 0; k < fragments; k++) {
                    frame.removeAll(frame);
                    frame.add(0, ev.getSource()); //ior reference
                    frame.add(1, k); //parte paquete
                    frame.add(2, fragments); //cantidad de fragementos total
                    frame.add(3, xdr_data.get(2)); // filename
                    frame.add(4, xdr_data.get(1)); //size
                    frame.add(5, xdr_data.get(3)); // unit
                    frame.add(6, xdr_data.get(0).toString().charAt(0)); //Operacion
                    frame.add(7, 0); //offset-> posicion inicial y final de lectura
                    for (int l = 0; l < (int) bufferSize; l++) {
                        if ("W".equals(frame.get(6))) {
                            frame.add("@");
                        } else {
                            frame.set(7, contador + 1);
                        }
                        contador = contador + 1;
                        if (contador == (int) xdr_data.get(1)) {
                            break;
                        }
                    }
                    Log.printLine(frame);
                    this.send(ev.getSource(), ev.eventTime(), SEND_CLIENT, this.copy_to(frame));
                }
                CloudSim.resumeSimulation();
                break;
            case XDR_DECODE:
                int cant_fragments = (int) ((ArrayList<Object>) ev.getData()).get(2);
                this.getMemory().add(ev.getData());
                this.fragment_counter++;
                if (this.getFragment_counter() == cant_fragments) {
                    Log.printLine("Decoding... ");
                    ArrayList<Object> decode_data = new ArrayList<>();
                    decode_data.add(((ArrayList) this.getMemory().get(0)).get(0));
                    decode_data.add(((ArrayList) this.getMemory().get(0)).get(3));
                    decode_data.add(((ArrayList) this.getMemory().get(0)).get(4));
                    decode_data.add(((ArrayList) this.getMemory().get(0)).get(5));
                    decode_data.add(((ArrayList) this.getMemory().get(0)).get(6));
                    this.getMemory().forEach((Object i) -> {
                        for (int j = 7; j < ((ArrayList) i).size(); j++) {
                            decode_data.add(((ArrayList) i).get(j));
                        }
                    });
                    Log.printLine(decode_data);
                    Log.printLine("Executing NFS procedure " + decode_data.get(4));
                    if ("W".equals(decode_data.get(4).toString())) {
                        this.send(((SistemaArchivos) CloudSim.getEntity("FileSystemSERVER_STORAGE")).getId(), ev.eventTime(), SistemaArchivos.WRITE, decode_data);
                        this.getClients().forEach((NfsClientVm vm) -> {
                            this.send(vm.getIdFs(), ev.eventTime(), SistemaArchivos.WRITE_CLIENT, decode_data);
                        });
                        CloudSim.resumeSimulation();
                        break;
                    } else if ("R".equals(decode_data.get(4).toString())) {
                        this.send(((SistemaArchivos) CloudSim.getEntity("FileSystemSERVER_STORAGE")).getId(), ev.eventTime(), SistemaArchivos.READ, decode_data);
                        CloudSim.resumeSimulation();
                        break;
                    }
                }
                CloudSim.resumeSimulation();
                break;
            default:
                Log.printLine("Error");
                break;
        }
    }
    
    @Override
    public void shutdownEntity() {
        Log.printLine("No more operations. Cleaning Buffers. Shutdown ... " + this.getId());
    }
    
    public Queue<Object> getSendBuffer() {
        return sendBuffer;
    }
    
    public void setSendBuffer(Queue<Object> sendBuffer) {
        this.sendBuffer = sendBuffer;
    }
    
    public Queue<Object> getRecvBuffer() {
        return recvBuffer;
    }
    
    public void setRecvBuffer(Queue<Object> recvBuffer) {
        this.recvBuffer = recvBuffer;
    }
    
    public Object getOperacion() {
        return operacion;
    }
    
    public void setOperacion(String Operacion) {
        this.operacion = Operacion;
    }
    
    public ArrayList<Object> getMemory() {
        return memory;
    }
    
    public void setMemory(ArrayList<Object> memory) {
        this.memory = memory;
    }
    
    public ArrayList<Object> copy_to(ArrayList<Object> list) {
        ArrayList<Object> aux = new ArrayList<>();
        aux.addAll(list);
        return aux;
    }
    
    public int getFragment_counter() {
        return fragment_counter;
    }
    
    public void setFragment_counter(int fragment_counter) {
        this.fragment_counter = fragment_counter;
    }
    
    public int getIor_reference() {
        return ior_reference;
    }
    
    public void setIor_reference(int ior_reference) {
        this.ior_reference = ior_reference;
    }
    
    public ArrayList<NfsClientVm> getClients() {
        return clients;
    }
    
    public void setClients(ArrayList<NfsClientVm> clients) {
        this.clients = clients;
    }
    
    public double getBufferSize() {
        return bufferSize;
    }
    
    public void setBufferSize(double bufferSize) {
        this.bufferSize = bufferSize;
    }
}
