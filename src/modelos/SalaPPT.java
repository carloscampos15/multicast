/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import org.json.JSONException;
import org.json.JSONObject;
import servidor.Interaccion;

/**
 *
 * @author carlo
 */
public class SalaPPT {

    private Jugador[] jugadores;
    private int identificador_sala;

    public SalaPPT(Jugador[] jugadores) {
        this.jugadores = jugadores;
    }

    public Jugador[] getJugadores() {
        return jugadores;
    }

    public int getIdentificador_sala() {
        return identificador_sala;
    }

    public void setIdentificador_sala(int identificador_sala) {
        this.identificador_sala = identificador_sala;
    }

    public JSONObject calcularGanador(int identificador, String accion) throws JSONException {
        for (Jugador jugador : jugadores) {
            if (jugador.getIdentificador() == identificador) {
                jugador.setAccion(accion);
            }
        }

        int con = 0;

        for (Jugador jugador : jugadores) {
            if (!jugador.getAccion().equals("")) {
                con++;
            }
        }

        if (con == 2) {
            return this.obtenerGanador();
        }
        return null;
    }

    private JSONObject obtenerGanador() throws JSONException {
        JSONObject response = new JSONObject();

        Jugador jugador1 = jugadores[0];
        Jugador jugador2 = jugadores[1];
        response.put("accion", Interaccion.GANADOR_PPT);
        response.put("jugada1", jugador1.getAccion());
        response.put("identificador1", jugador1.getIdentificador());
        response.put("jugada2", jugador2.getAccion());
        response.put("identificador2", jugador2.getIdentificador());
        if ((jugador1.getAccion().equalsIgnoreCase(Jugador.PIEDRA) && jugador2.getAccion().equalsIgnoreCase(Jugador.TIJERAS))
                || (jugador1.getAccion().equalsIgnoreCase(Jugador.PAPEL) && jugador2.getAccion().equalsIgnoreCase(Jugador.PIEDRA))
                || (jugador1.getAccion().equalsIgnoreCase(Jugador.TIJERAS) && jugador2.getAccion().equalsIgnoreCase(Jugador.PAPEL))) {

            response.put("ganador", jugador1.getIdentificador());
            return response;

        } else if ((jugador2.getAccion().equalsIgnoreCase(Jugador.PIEDRA) && jugador1.getAccion().equalsIgnoreCase(Jugador.TIJERAS))
                || (jugador2.getAccion().equalsIgnoreCase(Jugador.PAPEL) && jugador1.getAccion().equalsIgnoreCase(Jugador.PIEDRA))
                || (jugador2.getAccion().equalsIgnoreCase(Jugador.TIJERAS) && jugador1.getAccion().equalsIgnoreCase(Jugador.PAPEL))) {

            response.put("ganador", jugador2.getIdentificador());
            return response;

        }
        
        for(Jugador jugador : this.jugadores){
            jugador.setAccion("");
        }
        
        response.put("ganador", 0);
        return response;
    }
}
