/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import gamebase.Sprite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 *
 * @author educacion
 */
public class Person extends Sprite {

    public static final int INITIAL_WIDTH = 50;
    public static final int INITIAL_HEIGHT = 50;
    public static final int GROW_SIZE = 4;

    protected int step = 5;

    public Person(int x, int y, int height, int width) {
        super(x, y, height, width);
        setColor(Color.MAGENTA);
    }

    public boolean move(int direction) {
        int nx = x;
        int ny = y;

        switch (direction) {
            case KeyEvent.VK_W:
                ny -= step;
                break;

            case KeyEvent.VK_S:
                ny += step;
                break;

            case KeyEvent.VK_A:
                nx -= step;
                break;

            case KeyEvent.VK_D:
                nx += step;
                break;
        }
        if (!isOutOfGraphicContainer(nx, ny, width, height)) {
            x = nx;
            y = ny;
            if (gameContainer != null) {
                gameContainer.refresh();
            }

            return true;
        }

        return false;
    }

    //crecer
    public void grow() {
        x -= GROW_SIZE / 2;
        y -= GROW_SIZE / 2;
        width += GROW_SIZE;
        height += GROW_SIZE;

        gameContainer.refresh();
    }

    //disminuir
    public void shrink() {
        x += GROW_SIZE / 2;
        y += GROW_SIZE / 2;
        width -= GROW_SIZE;
        height -= GROW_SIZE;

        gameContainer.refresh();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

}
