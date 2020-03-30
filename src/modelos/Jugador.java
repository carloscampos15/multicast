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
import org.json.JSONException;
import org.json.JSONObject;
import servidor.Interaccion;

/**
 *
 * @author carlo
 */
public class Jugador implements Runnable {

    private String nombre;
    private Point posicion;
    private Socket clientSocket;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private ControladorJuego controladorJuego;

    public Jugador(Socket clientSocket, ControladorJuego controladorJuego) throws IOException {
        this.clientSocket = clientSocket;
        this.entrada = new DataInputStream(clientSocket.getInputStream());
        this.salida = new DataOutputStream(clientSocket.getOutputStream());
        this.controladorJuego = controladorJuego;
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

    private boolean iniciarSesion(String name) throws IOException, JSONException {
        this.setNombre(nombre);
        //calcular tambien la posicion
        JSONObject sendJson = new JSONObject();
        sendJson.put("code", 200);
        sendJson.put("nombre_usuario", this.getNombre());
        sendJson.put("posicion", "{"+"x:"+this.getPosicion().getX()+",y:"+this.getPosicion().getY()+"}");
        this.salida.writeUTF(sendJson.toString());
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
