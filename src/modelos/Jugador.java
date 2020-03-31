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

    public Jugador(Socket clientSocket, ControladorJuego controladorJuego) throws IOException {
        this.clientSocket = clientSocket;
        this.entrada = new DataInputStream(clientSocket.getInputStream());
        this.salida = new DataOutputStream(clientSocket.getOutputStream());
        this.controladorJuego = controladorJuego;
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

    @Override
    public void run() {
        while (true) {
            String received;
            while (true) {
                try {
                    received = entrada.readUTF();
                    JSONObject receivedJson = new JSONObject(received);

                    switch (receivedJson.getInt("accion")) {
                        case 1:
                            this.iniciarSesion(receivedJson.getString("nombre_usuario"));
                            this.notificarJugadores();
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
