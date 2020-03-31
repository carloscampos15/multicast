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
 * Es donde se van a generar la creacion de las nuevos clientes que desean ingresar
 *
 * @author Karen Castaño Orjuela Castaño
 * @author Carlos Alberto Campos Armero
 */
public class Mundo extends SpriteContainer {

    protected Person person;

    public Mundo(int x, int y, int height, int width) {
        super(x, y, height, width);
        this.setColor(Color.CYAN);
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void createPerson(int x, int y, int identificador, String nombre) {
        this.person = new Person(x, y, Person.INITIAL_WIDTH, Person.INITIAL_HEIGHT, identificador, nombre);
        sprites.add(person);
        person.setGraphicContainer(this);
        refresh();
    }

    public void createPersonSesion(int x, int y, int identificador, String nombre) {
        Person person = new Person(x, y, Person.INITIAL_WIDTH, Person.INITIAL_HEIGHT, identificador, nombre);
        sprites.add(person);
        person.setGraphicContainer(this);
        refresh();
    }
    
    public Person findPersonById(int identificador, int x, int y){
        for(Sprite sprite : sprites){
            Person person = (Person) sprite;
            if(person.getIdentificador() == identificador){
                person.setX(x);
                person.setY(y);
                refresh();
                return person;
            }
        }
        return null;
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
