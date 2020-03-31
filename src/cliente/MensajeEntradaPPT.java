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
 *
 * @author carlo
 */
public class MensajeEntradaPPT extends Thread{
    private boolean isAlive;
    private DataInputStream entrada;
    private NotificablePPT notificable;

    public MensajeEntradaPPT(NotificablePPT notificable, DataInputStream entrada) {
        this.notificable = notificable;
        this.entrada = entrada;
        this.isAlive = true;
    }

    public boolean isIsAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
    
    @Override
    public void run() {
        while (isAlive) {
            try {
                String msg = entrada.readUTF();
                JSONObject receivedJson = new JSONObject(msg);
                switch (receivedJson.getInt("accion")) {
                    case Interaccion.GANADOR_PPT:
                        this.notificable.ganadorPPT(receivedJson.toString(), 2);
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
