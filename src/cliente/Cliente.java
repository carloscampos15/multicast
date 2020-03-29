/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.awt.Point;
import java.io.IOException;
import redes.RedCliente;
import servidor.Interaccion;

/**
 *
 * @author carlo
 */
public class Cliente {

    private RedCliente redCliente;
    private String nombre;
    private Point posicion;

    public Cliente(String nombre) throws IOException {
        this.nombre = nombre;
        this.redCliente = new RedCliente("229.2.2.2", Interaccion.PUERTO, this);
    }

    public Point getPosicion() {
        return posicion;
    }

    public void setPosicion(Point posicion) {
        this.posicion = posicion;
    }

    public void ejecutarCliente() {
        this.redCliente.ejecutar();
    }

    public String getNombre() {
        return nombre;
    }

    public static void main(String[] args) {
        try {
            Cliente cliente = new Cliente("NOMBRE USUARIO");
            cliente.ejecutarCliente();
        } catch (IOException ex) {
            System.out.println(">>ERROR INCIIANDO CLIENTE");
        }
    }
}
