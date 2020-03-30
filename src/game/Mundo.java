/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import gamebase.Sprite;
import gamebase.SpriteContainer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

/**
 *
 * @author educacion
 */
public class Mundo extends SpriteContainer {

    protected Person person;

    public Mundo(int x, int y, int height, int width) {
        super(x, y, height, width);
        person = new Person((width - Person.INITIAL_WIDTH) / 2,
                (height - Person.INITIAL_HEIGHT) / 2,
                Person.INITIAL_WIDTH, Person.INITIAL_HEIGHT);
        person.setGraphicContainer(this);
        this.setColor(Color.CYAN);
    }

    private void addPerson() {
        boolean state = true;
        while (state) {
            int random1 = (int) (Math.random() * this.width) + 1;
            int random2 = (int) (Math.random() * this.height) + 1;

            person = new Person(random1, random2, Person.INITIAL_WIDTH, Person.INITIAL_HEIGHT);

            if (!validateCreatePerson(person)) {
                person.setGraphicContainer(this);
                sprites.add(person);
                state = false;
            }
        }

    }

    private boolean validateCreatePerson(Person person) {
        for (Sprite sprite : sprites) {
            if (sprite.checkCollision(person)) {
                return true;
            }
        }
        if (person.getX() >= 0
                & person.getY() >= 0
                & person.getX() + person.getWidth() <= this.getWidth()
                & person.getY() + person.getHeight() <= this.getHeight()) {
            return false;
        }
        return true;
    }

    private void processMushroomsEaten() {
        //se proceso si se toca con otra persona que comience el juego
    }

    public void keyPressed(int code) {
        if (code == KeyEvent.VK_W
                | code == KeyEvent.VK_S
                | code == KeyEvent.VK_A
                | code == KeyEvent.VK_D) {
            if (person.move(code)) {
                processMushroomsEaten();
            }
        }

        if (code == KeyEvent.VK_G) {
            addPerson();
            refresh();
        }
    }

    @Override
    public void paint(Graphics g) {
        // Painting the floor        
        g.setColor(color);
        g.fillRect(x, y, width, height);

        for (Sprite sprite : sprites) {
            sprite.paint(g);
        }

        // Painting the person (player)
        person.paint(g);
    }

    @Override
    public void refresh() {
        if (gameContainer != null) {
            gameContainer.refresh();
        }
    }

    @Override
    public Rectangle getBoundaries() {
        return new Rectangle(x, y, width, height);
    }
}
