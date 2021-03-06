/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.IOException;
import org.json.JSONException;
import redes.RedCliente;
import servidor.Interaccion;

/**
 * Es el encargado de tener todo los componentes de cliente
 *
 * @author Karen Castaño Orjuela Castaño
 * @author Carlos Alberto Campos Armero
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
    
    public void ejecutarSocketPPT(NotificablePPT notificable){
        this.redCliente.setNotificablePPT(notificable);
    }
    
    public void terminarJuegoPPT(){
        this.redCliente.terminarJuegoPPT();
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

    public boolean moverUsuario(int identificador, int x, int y) throws JSONException, IOException {
        this.redCliente.moverUsuario(this.getNombre(), identificador, x, y);
        return true;
    }
    
    public boolean setPosicion(int identificador, int x, int y) throws JSONException, IOException {
        this.redCliente.setPosicion(this.getNombre(), identificador, x, y);
        return true;
    }
    
    public boolean iniciarPPT(int identificador1, int identificador2) throws JSONException, IOException {
        this.redCliente.iniciarPPT(identificador1, identificador2);
        return true;
    }
    
    public boolean jugarPPT(int identificador, int identificadorSala, String juegoAccion) throws JSONException, IOException{
        this.redCliente.jugarPPT(identificador, identificadorSala, juegoAccion);
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
