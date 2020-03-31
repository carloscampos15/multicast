/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.awt.Point;
import java.util.ArrayList;
import modelos.Jugador;
import modelos.SalaPPT;
import org.json.JSONException;
import org.json.JSONObject;
import servidor.Interaccion;

/**
 * Es el encargado de agregar y validar la posicion del nuevo jugador
 *
 * @author Karen Castaño Orjuela Castaño
 * @author Carlos Alberto Campos Armero
 */
public class ControladorJuego {

    private ArrayList<Jugador> jugadores;
    private ArrayList<SalaPPT> salas;

    public ControladorJuego(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
        this.salas = new ArrayList<>();
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
    
    public Jugador getJugadorById(int identificador){
        for(Jugador jugador : jugadores){
            if(jugador.getIdentificador() == identificador){
                return jugador;
            }
        }
        return null;
    }
    
    public SalaPPT crearSalaPPT(int id_jugador1, int id_jugador2){
        Jugador[] players = new Jugador[2];
        players[0] = getJugadorById(id_jugador1);
        players[1] = getJugadorById(id_jugador2);
        SalaPPT sala = new SalaPPT(players);
        sala.setIdentificador_sala(salas.size()+1);
        salas.add(sala);
        return sala;
    }
    
    public SalaPPT getSalaById(int identificador_sala){
        for(SalaPPT sala : salas){
            if(sala.getIdentificador_sala() == identificador_sala){
                return sala;
            }
        }
        return null;
    }
    
    public JSONObject calcularGanador(int identificador, int identificador_sala, String accion) throws JSONException{
        SalaPPT sala = getSalaById(identificador_sala);
        return sala.calcularGanador(identificador, accion);
    }
}
