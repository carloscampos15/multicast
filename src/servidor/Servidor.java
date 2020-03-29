/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import redes.RedServidor;

/**
 *
 * @author carlo
 */
public class Servidor {

    private RedServidor redServidor;

    public Servidor(RedServidor redServidor) {
        this.redServidor = redServidor;
    }

    public void ejecutarServidor() {
        this.redServidor.ejecutarServicios();
    }

    public static void main(String[] args) {
        try {
            RedServidor red = new RedServidor("229.2.2.2", Interaccion.PUERTO);
            
            Servidor servidor = new Servidor(red);
            servidor.ejecutarServidor();
        } catch (IOException ex) {
            System.out.println("<<ERROR INICIANDO MIS SERVICIOS");
        }
    }
}
