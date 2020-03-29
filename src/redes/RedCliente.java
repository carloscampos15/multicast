/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redes;

import cliente.Cliente;
import cliente.MensajeEntradaMC;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import servidor.Interaccion;

/**
 *
 * @author carlo
 */
public class RedCliente {

    private InetAddress grupo;
    private int puerto;
    private MulticastSocket socket;
    private Cliente cliente;

    public RedCliente(String grupo, int puerto, Cliente cliente) throws UnknownHostException, IOException {
        this.grupo = InetAddress.getByName(grupo);
        this.puerto = puerto;
        this.socket = new MulticastSocket(puerto);
        this.cliente = cliente;

        socket.setBroadcast(false);
        socket.setLoopbackMode(false);
        socket.setTimeToLive(2);
        socket.joinGroup(this.grupo);
    }

    public void ejecutar() {
        MensajeEntradaMC readMessage = new MensajeEntradaMC(socket);
        readMessage.start();

        BufferedReader myinput = new BufferedReader(new InputStreamReader(System.in));
        boolean state = true;
        try {
            while (state) {
                byte[] data = new byte[Interaccion.MAX_BUFFER_SIZE];
                String accion = myinput.readLine();
                String line = "";
                //CASOS PARA ACCIONES
                switch (accion) {
                    case "INICIAR":
                        data[0] = Interaccion.NUEVO_CLIENTE;
                        break;
                    case "SALIR":
                        data[0] = Interaccion.SALIDA_CLIENTE;
                        state = false;
                        break;
                }
                byte[] nombre = this.cliente.getNombre().getBytes();
                data[1] = (byte) nombre.length;
                System.arraycopy(nombre, 0, data, 2, nombre.length);

                DatagramPacket dp = new DatagramPacket(data, Interaccion.MAX_BUFFER_SIZE, this.grupo, this.puerto);

                socket.send(dp);

            }

            socket.leaveGroup(grupo);
            socket.close();
        } catch (IOException ex) {
            System.out.println(">>OCURRIO UN ERROR AL ENVIAR LOS DATOS");
        }
    }
}
