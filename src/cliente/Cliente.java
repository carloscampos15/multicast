/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.awt.Point;
import java.io.IOException;
import org.json.JSONException;
import redes.RedCliente;
import servidor.Interaccion;

/**
 *
 * @author carlo
 */
public class Cliente {

    private RedCliente redCliente;
    private String nombre;

    public Cliente(String nombre) throws IOException {
        this.nombre = nombre;
        this.redCliente = new RedCliente("127.0.0.1", "229.2.2.2", Interaccion.PUERTO, this);
    }

    public void ejecutarCliente() {
        this.redCliente.ejecutar();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNotificableRed(Notificable notificable) {
        this.redCliente.setNotificable(notificable);
    }

    public boolean updateName(String name) throws IOException, JSONException {
        this.redCliente.updateNameUser(name);
        return true;
    }
    
    public boolean enviarMensaje(String mensaje) throws IOException, JSONException {
        this.redCliente.enviarMensaje(mensaje);
        return true;
    }

//    public static void main(String[] args) {
//        try {
//            Cliente cliente = new Cliente("NOMBRE USUARIO");
//            cliente.ejecutarCliente();
//        } catch (IOException ex) {
//            System.out.println(">>ERROR INCIIANDO CLIENTE");
//        }
//    }
}
