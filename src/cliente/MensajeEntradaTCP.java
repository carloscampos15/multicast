/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.DataInputStream;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import servidor.Interaccion;

/**
 * Es el hilo encargado de estar siempre pendiente de recibir un mensaje (TCP)
 *
 * @author Karen Castaño Orjuela Castaño
 * @author Carlos Alberto Campos Armero
 */
public class MensajeEntradaTCP extends Thread {

    private boolean isAlive;
    private DataInputStream entrada;
    private Notificable notificable;

    public MensajeEntradaTCP(Notificable notificable, DataInputStream entrada) {
        this.notificable = notificable;
        this.entrada = entrada;
        this.isAlive = true;
    }

    @Override
    public void run() {
        while (isAlive) {
            try {
                String msg = entrada.readUTF();
                JSONObject receivedJson = new JSONObject(msg);
                switch (receivedJson.getInt("accion")) {
                    case Interaccion.NUEVO_CLIENTE:
                        this.notificable.login(receivedJson.toString(), 2);
                        break;
                    case Interaccion.SESION_NUEVO_CLIENTE:
                        this.notificable.nuevoUsuario(receivedJson.toString(), 2);
                        break;
                    case Interaccion.SALIDA_CLIENTE:
                        isAlive = false;
                        break;
                    case Interaccion.NUEVO_PPT:
                        this.notificable.nuevoPPT(receivedJson.toString(), 2);
                        break;
                }
            } catch (IOException e) {
                System.out.println(">>ERROR AL RECIBIR DATOS");
            } catch (JSONException ex) {
                System.out.println(">>ERROR TRANSFORMANDO DATOS");
            }
        }
    }

}
