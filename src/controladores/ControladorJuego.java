/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import game.Person;
import gamebase.Sprite;
import java.awt.Point;
import java.util.ArrayList;
import modelos.Jugador;
import servidor.Interaccion;

/**
 * Es el encargado de agregar y validar la posicion del nuevo jugador
 *
 * @author Karen Castaño Orjuela Castaño
 * @author Carlos Alberto Campos Armero
 */
public class ControladorJuego {

    private ArrayList<Jugador> jugadores;

    public ControladorJuego(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    /**
     * Agrega un jugador en una posicion aleatoria teniendo en cuenta que no
     * aparezca tocando a otro jugador
     *
     * @param jugador
     * @return
     */
    public ArrayList<Jugador> agregarJugadorMapa(Jugador jugador) {
        boolean state = true;

        while (state) {
            int random1 = (int) (Math.random() * Interaccion.WIDTH_MAP) + 1;
            int random2 = (int) (Math.random() * Interaccion.HEIGHT_MAP) + 1;
            jugador.setPosicion(new Point(random1, random2));
            if (!validarPosicionNueva(jugador)) {
                jugadores.add(jugador);
                state = false;
            }
        }

        Thread t = new Thread(jugador);
        t.start();
        return this.jugadores;
    }

    /**
     * Valida que la posicion del nuevo jugador este vacia
     *
     * @param jugador
     * @return
     */
    private boolean validarPosicionNueva(Jugador jugador) {
        for (Jugador jugadore : jugadores) {
            if (jugadore.verificarColision(jugador)) {
                return true;
            }
        }
        if (jugador.getPosicion().getX() >= 0
                & jugador.getPosicion().getY() >= 0
                & jugador.getPosicion().getX() + Interaccion.SIZE_BOX <= Interaccion.WIDTH_MAP
                & jugador.getPosicion().getY() + Interaccion.SIZE_BOX <= Interaccion.HEIGHT_MAP) {
            return false;
        }
        return true;
    }

    public boolean setPosicionJugador(int identificador, int x, int y) {
        
        for (Jugador jugador : jugadores) {
            if(jugador.getIdentificador() == identificador){
                jugador.setPosicion(new Point(x, y));
                return true;
            }
        }
        return false;
    }
}
