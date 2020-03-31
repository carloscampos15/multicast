/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redes;

import controladores.ControladorJuego;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import modelos.Juego;
import modelos.Jugador;

/**
 *
 * @author carlo
 */
public class RedServidor {

    private int puerto;
    private Juego juego;
    public static ArrayList<Jugador> jugadores;
    private ServerSocket listenSocket;
    private ControladorJuego controladorJuego;

    public RedServidor(int puerto) {
        this.puerto = puerto;
        this.juego = new Juego();
        jugadores = new ArrayList<>();
        this.controladorJuego = new ControladorJuego(jugadores);
    }

    public void activar() throws IOException {
        System.out.println("<<binding port");
        this.listenSocket = new ServerSocket(this.puerto);
        this.ejecutarServicios();
    }

    private void ejecutarServicios() {
        while (true) {
            try {
                System.out.println("<<Esperando clientes");
                Socket clientSocket = listenSocket.accept();
                System.out.println("<<Cliente recibido");
                Jugador jugador = new Jugador(clientSocket, this.controladorJuego);
                jugador.setIdentificador(jugadores.size()+1);
                this.jugadores = controladorJuego.agregarJugadorMapa(jugador);
            } catch (IOException ex) {
                System.out.println("<<Error al conectar cliente");
            }
        }
    }
}
