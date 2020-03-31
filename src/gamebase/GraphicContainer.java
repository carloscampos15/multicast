/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebase;

import java.awt.Rectangle;

/**
 * Se encarga de tener lo necesario para graficar
 *
 * @author Karen Castaño Orjuela Castaño
 * @author Carlos Alberto Campos Armero
 */
public interface GraphicContainer {
    public void refresh();
    public Rectangle getBoundaries();
}
