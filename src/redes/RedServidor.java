/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import servidor.Interaccion;

/**
 *
 * @author carlo
 */
public class RedServidor {

    private int puerto;
    private InetAddress grupo;
    private MulticastSocket socket;

    public RedServidor(String grupo, int puerto) throws IOException {
        this.grupo = InetAddress.getByName(grupo);
        this.puerto = puerto;
        socket = new MulticastSocket(puerto);

        socket.setBroadcast(false);
        socket.setLoopbackMode(false);
        socket.setTimeToLive(2);
        socket.joinGroup(this.grupo);
    }

    public void activar() throws IOException {
        this.ejecutarServicios();
    }

    public void ejecutarServicios() {
        while (true) {
            try {
                DatagramPacket paquete = new DatagramPacket(new byte[Interaccion.MAX_BUFFER_SIZE], Interaccion.MAX_BUFFER_SIZE);
                socket.receive(paquete);
                byte[] data = paquete.getData();
                socket.send(paquete);
            } catch (IOException ex) {
                System.out.println("<<ERROR RECIBIENDO DATOS");
            }
        }
    }
}
