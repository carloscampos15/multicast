/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebase;

/**
 *
 * @author educacion
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
