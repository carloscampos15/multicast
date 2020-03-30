/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redes;

import cliente.Cliente;
import cliente.MensajeEntradaMC;
import cliente.MensajeEntradaTCP;
import cliente.Notificable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.JSONException;
import org.json.JSONObject;
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
    private Notificable notificable;

    //comunicacion tcp
    private DataInputStream entrada;
    private DataOutputStream salida;

    public RedCliente(String tcp_ip, String grupo, int puerto, Cliente cliente) throws UnknownHostException, IOException {
        this.grupo = InetAddress.getByName(grupo);
        this.puerto = puerto;
        this.socket = new MulticastSocket(puerto);
        this.cliente = cliente;

        socket.setBroadcast(false);
        socket.setLoopbackMode(false);
        socket.setTimeToLive(2);
        socket.joinGroup(this.grupo);

        Socket socket_tcp = new Socket(tcp_ip, this.puerto);
        this.entrada = new DataInputStream(socket_tcp.getInputStream());
        this.salida = new DataOutputStream(socket_tcp.getOutputStream());
    }

    public Notificable getNotificable() {
        return notificable;
    }

    public void setNotificable(Notificable notificable) {
        this.notificable = notificable;
    }

    public void ejecutar() {
        MensajeEntradaMC readMessageMC = new MensajeEntradaMC(notificable, socket);
        readMessageMC.start();
        
        MensajeEntradaTCP readMessageTCP = new MensajeEntradaTCP(notificable, entrada);
        readMessageTCP.start();

//        BufferedReader myinput = new BufferedReader(new InputStreamReader(System.in));
//        boolean state = true;
//        try {
//            while (state) {
//                byte[] data = new byte[Interaccion.MAX_BUFFER_SIZE];
//                String accion = myinput.readLine();
//                
//                JSONObject sendJson = new JSONObject();
//                
//                switch (accion) {
//                    case "INICIAR":
//                        sendJson.put("accion", Interaccion.NUEVO_CLIENTE);
//                        break;
//                    case "SALIR":
//                        sendJson.put("accion", Interaccion.SALIDA_CLIENTE);
//                        state = false;
//                        break;
//                    case "MOVER":
//                        sendJson.put("accion", Interaccion.MOVER_CLIENTE);
//                        sendJson.put("movimiento", new Point(1,2));
//                        break;
//                }
//                sendJson.put("nombre_usuario", this.cliente.getNombre());
//                
//                byte[] hoja = new String(sendJson.toString()).getBytes();
//                
//                DatagramPacket dp = new DatagramPacket(hoja, hoja.length, this.grupo, this.puerto);
//
//                socket.send(dp);
//
//            }
//
//            socket.leaveGroup(grupo);
//            socket.close();
//        } catch (IOException ex) {
//            System.out.println(">>OCURRIO UN ERROR AL ENVIAR LOS DATOS");
//        } catch (JSONException ex) {
//            System.out.println(">>OCURRIO UN ERROR AL TRANFORMAR LOS DATOS A JSON");
//        }
    }

    /**
     * Asigna el respectivo nombre al cliente
     *
     * @param name
     * @return
     * @throws IOException
     */
    public boolean updateNameUser(String name) throws IOException, JSONException {
        //data en formato json
        byte[] data = new byte[Interaccion.MAX_BUFFER_SIZE];
        JSONObject sendJson = new JSONObject();
        sendJson.put("accion", Interaccion.NUEVO_CLIENTE);
        sendJson.put("nombre_usuario", this.cliente.getNombre());

        //enviar al tcp servidor
        salida.writeUTF(sendJson.toString());
        salida.flush();

        //enviar al multicast
        byte[] hoja = new String(sendJson.toString()).getBytes();
        DatagramPacket dp = new DatagramPacket(hoja, hoja.length, this.grupo, this.puerto);
        socket.send(dp);

        return true;
    }
}
