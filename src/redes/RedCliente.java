/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redes;

import cliente.Cliente;
import cliente.MensajeEntradaMC;
import cliente.MensajeEntradaPPT;
import cliente.MensajeEntradaTCP;
import cliente.Notificable;
import cliente.NotificablePPT;
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
 * Es el encargado de procesar los mensajes que entran y salen del cliente
 *
 * @author Karen Dayanna Castaño Orjuela
 * @author Carlos Alberto Campos Armero
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

    private MensajeEntradaMC readMessageMC;
    private MensajeEntradaTCP readMessageTCP;
    private MensajeEntradaPPT readMessagePPT;

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

    public void setNotificablePPT(NotificablePPT notificable) {
        readMessagePPT = new MensajeEntradaPPT(notificable, entrada);
        readMessagePPT.start();
    }
    
    public void terminarJuegoPPT(){
        readMessagePPT.setIsAlive(false);
        readMessagePPT = null;
    }

    public void ejecutar() {
        readMessageMC = new MensajeEntradaMC(notificable, socket);
        readMessageMC.start();

        readMessageTCP = new MensajeEntradaTCP(notificable, entrada);
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

    /**
     * Asigna el respectivo nombre al cliente
     *
     * @param name
     * @return
     * @throws IOException
     */
    public boolean enviarMensaje(String mensaje) throws IOException, JSONException {
        //data en formato json
        byte[] data = new byte[Interaccion.MAX_BUFFER_SIZE];
        JSONObject sendJson = new JSONObject();
        sendJson.put("accion", Interaccion.NUEVO_MENSAJE);
        sendJson.put("nombre_usuario", this.cliente.getNombre());
        sendJson.put("mensaje", mensaje);

        //enviar al multicast
        byte[] hoja = new String(sendJson.toString()).getBytes();
        DatagramPacket dp = new DatagramPacket(hoja, hoja.length, this.grupo, this.puerto);
        socket.send(dp);

        return true;
    }

    public boolean moverUsuario(String nombre, int identificador, int x, int y) throws JSONException, IOException {
        byte[] data = new byte[Interaccion.MAX_BUFFER_SIZE];
        JSONObject sendJson = new JSONObject();
        sendJson.put("accion", Interaccion.MOVER_CLIENTE);
        sendJson.put("nombre_usuario", nombre);
        sendJson.put("identificador", identificador);
        sendJson.put("x", x);
        sendJson.put("y", y);

        byte[] hoja = new String(sendJson.toString()).getBytes();
        DatagramPacket dp = new DatagramPacket(hoja, hoja.length, this.grupo, this.puerto);
        socket.send(dp);

        return true;
    }

    public boolean setPosicion(String nombre, int identificador, int x, int y) throws JSONException, IOException {
        byte[] data = new byte[Interaccion.MAX_BUFFER_SIZE];
        JSONObject sendJson = new JSONObject();
        sendJson.put("accion", Interaccion.MOVER_CLIENTE);
        sendJson.put("nombre_usuario", nombre);
        sendJson.put("identificador", identificador);
        sendJson.put("x", x);
        sendJson.put("y", y);

        //enviar al tcp servidor
        salida.writeUTF(sendJson.toString());
        salida.flush();

        return true;
    }

    public boolean iniciarPPT(int identificador1, int identificador2) throws JSONException, IOException {

        byte[] data = new byte[Interaccion.MAX_BUFFER_SIZE];
        JSONObject sendJson = new JSONObject();

        sendJson.put("accion", Interaccion.NUEVO_PPT);
        sendJson.put("jugador1", identificador1);
        sendJson.put("jugador2", identificador2);

        //enviar al tcp servidor
        salida.writeUTF(sendJson.toString());
        salida.flush();

        return true;
    }

    public boolean jugarPPT(int identificador, int identificadorSala, String juegoAccion) throws JSONException, IOException {

        byte[] data = new byte[Interaccion.MAX_BUFFER_SIZE];
        JSONObject sendJson = new JSONObject();

        sendJson.put("accion", Interaccion.JUEGO_PPT);
        sendJson.put("identificador", identificador);
        sendJson.put("identificador_sala", identificadorSala);
        sendJson.put("juego_accion", juegoAccion);

        //enviar al tcp servidor
        salida.writeUTF(sendJson.toString());
        salida.flush();

        return true;
    }
}
