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
        try {
            this.redServidor.activar();
        } catch (IOException ex) {
            System.out.println("<<NO PUDE INICIAR MIS SERVICIOS");
        }
    }

    public static void main(String[] args) {
        RedServidor red = new RedServidor(Interaccion.PUERTO);
        Servidor servidor = new Servidor(red);
        servidor.ejecutarServidor();

    }
}
