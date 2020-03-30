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
 *
 * @author carlo
 */
public class ControladorJuego {

    private ArrayList<Jugador> jugadores;

    public ControladorJuego(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

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
    
    private boolean validarPosicionNueva(Jugador jugador){
        for(Jugador jugadore:jugadores){
            if(jugadore.verificarColision(jugador)){
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
}
