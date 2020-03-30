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

/**
 *
 * @author carlo
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
                System.out.println(receivedJson);
            } catch (IOException e) {
                System.out.println(">>ERROR AL RECIBIR DATOS");
            } catch (JSONException ex) {
                System.out.println(">>ERROR TRANSFORMANDO DATOS");
            }
        }
    }

}
