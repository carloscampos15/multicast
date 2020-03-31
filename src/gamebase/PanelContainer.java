/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebase;

import game.Mundo;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

/**
 * 
 *
 * @author Karen Castaño Orjuela Castaño
 * @author Carlos Alberto Campos Armero
 */
public class PanelContainer extends JPanel implements GraphicContainer {

    protected Mundo mundo;
    protected GraphicContainer containe;
    protected JPanel panel;

    public PanelContainer(GraphicContainer containe, JPanel panel) {
        this.containe = containe;
        this.panel = panel;
    }

    public Mundo getMundo() {
        return mundo;
    }

    public void setMundo(Mundo mundo) {
        this.mundo = mundo;
    }

    public void keyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_Q) {
            System.exit(0);
        }

        if (evt.getKeyCode() == KeyEvent.VK_W
                | evt.getKeyCode() == KeyEvent.VK_S
                | evt.getKeyCode() == KeyEvent.VK_A
                | evt.getKeyCode() == KeyEvent.VK_D) {
            mundo.keyPressed(evt.getKeyCode());
        }

        if (evt.getKeyCode() == KeyEvent.VK_G) {
            mundo.keyPressed(evt.getKeyCode());
        }
    }

    @Override
    public void paint(Graphics g) {
        if (mundo != null) {
            mundo.paint(g);
        }
    }

    @Override
    public void refresh() {
        this.repaint();
        this.containe.refresh();
        this.panel.repaint();
    }

    @Override
    public Rectangle getBoundaries() {
        return this.getBounds();
    }
}
