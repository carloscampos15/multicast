/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.util.ArrayList;

/**
 *
 * @author carlo
 */
public class Juego {

    private ArrayList<Jugador> jugadores;

    public void addPlayer(Jugador jugador) {
        jugadores.add(jugador);
    }
}
