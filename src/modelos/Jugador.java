/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import controladores.ControladorJuego;
import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import redes.RedServidor;
import servidor.Interaccion;

/**
 * Tiene lo necesario para que un jugador pueda ingresar a jugar
 *
 * @author Karen Castaño Orjuela Castaño
 * @author Carlos Alberto Campos Armero
 */
public class Jugador implements Runnable {

    private String nombre;
    private Point posicion;
    private Socket clientSocket;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private ControladorJuego controladorJuego;
    private int identificador;
    private String accion;

    public static final String PIEDRA = "PIEDRA";
    public static final String PAPEL = "PAPEL";
    public static final String TIJERAS = "TIJERAS";

    public Jugador(Socket clientSocket, ControladorJuego controladorJuego) throws IOException {
        this.clientSocket = clientSocket;
        this.entrada = new DataInputStream(clientSocket.getInputStream());
        this.salida = new DataOutputStream(clientSocket.getOutputStream());
        this.controladorJuego = controladorJuego;
        this.accion = "";
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public ControladorJuego getControladorJuego() {
        return controladorJuego;
    }

    public Point getPosicion() {
        return posicion;
    }

    public void setPosicion(Point posicion) {
        this.posicion = posicion;
    }

    public boolean verificarColision(Jugador jugador) {
        boolean collisionX = this.getPosicion().getX() + Interaccion.SIZE_BOX >= jugador.getPosicion().getX()
                && this.getPosicion().getX() < jugador.getPosicion().getX() + Interaccion.SIZE_BOX;
        boolean collisionY = this.getPosicion().getY() + Interaccion.SIZE_BOX >= jugador.getPosicion().getY()
                && this.getPosicion().getY() < jugador.getPosicion().getY() + Interaccion.SIZE_BOX;
        return collisionX && collisionY;
    }

    private boolean iniciarSesion(String nombre) throws IOException, JSONException {
        this.setNombre(nombre);
        JSONObject sendJson = new JSONObject();
        sendJson.put("accion", Interaccion.NUEVO_CLIENTE);
        sendJson.put("identificador", this.getIdentificador());
        sendJson.put("nombre_usuario", this.getNombre());
        sendJson.put("posicion", "{" + "x:" + this.getPosicion().getX() + ",y:" + this.getPosicion().getY() + "}");
        this.salida.writeUTF(sendJson.toString());
        return true;
    }

    private boolean notificarJugadores() throws JSONException, IOException {
        JSONObject sendJson = new JSONObject();
        sendJson.put("accion", Interaccion.SESION_NUEVO_CLIENTE);
        sendJson.put("nombre_usuario", this.getNombre());
        sendJson.put("identificador", this.getIdentificador());
        sendJson.put("posicion", "{" + "x:" + this.getPosicion().getX() + ",y:" + this.getPosicion().getY() + "}");

        JSONObject jsonUsers = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        //se envia a los usuario ya en sesion el nuevo jugagor
        for (Jugador jugadore : RedServidor.jugadores) {
            JSONObject jsonSesion = new JSONObject();
            if (jugadore != this) {
                jugadore.salida.writeUTF(sendJson.toString());
                jsonSesion.put("nombre_usuario", jugadore.getNombre());
                jsonSesion.put("identificador", jugadore.getIdentificador());
                jsonSesion.put("x", jugadore.getPosicion().getX());
                jsonSesion.put("y", jugadore.getPosicion().getY());
                jsonArray.put(jsonSesion);
            }
        }

        jsonUsers.put("accion", Interaccion.SESION_NUEVO_CLIENTE);
        jsonUsers.put("usuarios", jsonArray);
        //se enviar al nuevo jugador los jugadores en sersion
        if (RedServidor.jugadores.size() > 1) {
            this.salida.writeUTF(jsonUsers.toString());
        }

        return true;
    }

    private boolean notificarJugadoresSala(SalaPPT sala) throws JSONException, IOException {
        Jugador[] players = sala.getJugadores();
        JSONObject sendJson = new JSONObject();
        sendJson.put("accion", Interaccion.NUEVO_PPT);
        sendJson.put("id_jugador1", players[0].getIdentificador());
        sendJson.put("nombre_jugador1", players[0].getNombre());
        sendJson.put("id_jugador2", players[1].getIdentificador());
        sendJson.put("nombre_jugador2", players[1].getNombre());
        sendJson.put("id_sala", sala.getIdentificador_sala());
        for (Jugador jugador : players) {
            jugador.salida.writeUTF(sendJson.toString());
        }
        return true;
    }

    private boolean notificarGanadorBySala(SalaPPT sala, JSONObject response) throws IOException {
        Jugador[] players = sala.getJugadores();
        for (Jugador jugador : players) {
            jugador.salida.writeUTF(response.toString());
        }
        return true;
    }

    @Override
    public void run() {
        while (true) {
            String received;
            while (true) {
                try {
                    received = entrada.readUTF();
                    JSONObject receivedJson = new JSONObject(received);

                    switch (receivedJson.getInt("accion")) {
                        case Interaccion.NUEVO_CLIENTE:
                            this.iniciarSesion(receivedJson.getString("nombre_usuario"));
                            this.notificarJugadores();
                            break;
                        case Interaccion.MOVER_CLIENTE:
                            this.controladorJuego.setPosicionJugador(receivedJson.getInt("identificador"), receivedJson.getInt("x"), receivedJson.getInt("y"));
                            break;
                        case Interaccion.NUEVO_PPT:
                            SalaPPT sala = this.controladorJuego.crearSalaPPT(receivedJson.getInt("jugador1"), receivedJson.getInt("jugador2"));
                            this.notificarJugadoresSala(sala);
                            break;
                        case Interaccion.JUEGO_PPT:
                            JSONObject response = this.controladorJuego.calcularGanador(receivedJson.getInt("identificador"), receivedJson.getInt("identificador_sala"), receivedJson.getString("juego_accion"));
                            SalaPPT misala = this.controladorJuego.getSalaById(receivedJson.getInt("identificador_sala"));
                            if (response != null) {
                                notificarGanadorBySala(misala, response);
                            }
                            break;

                    }
                } catch (JSONException ex) {
                    System.out.println("<<Error tranformando datos a json");
                } catch (IOException ex) {
                    System.out.println("<<Error de lectura");
                }
            }
        }
    }
}
