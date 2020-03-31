/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebase;

/**
 * Es el encargado de que el cuadrado tenga no necesario para que se pueda mover
 *
 * @author Karen Castaño Orjuela Castaño
 * @author Carlos Alberto Campos Armero
 */
public abstract class SpriteMobile extends Sprite
{
    protected int step;
    
    public SpriteMobile(int x, int y, int height, int width) {
        super(x, y, height, width);
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
