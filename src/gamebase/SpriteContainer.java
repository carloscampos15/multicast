/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebase;

import java.util.ArrayList;

/**
 *
 * @author educacion
 */
public abstract class SpriteContainer extends Sprite implements GraphicContainer
{
    protected ArrayList<Sprite> sprites;   
    
    public SpriteContainer(int x, int y, int height, int width) {
        super(x, y, height, width);
        
        sprites = new ArrayList<Sprite>();
    }   
    
    public boolean add(Sprite sprite)
    {
        return sprites.add(sprite);
    }
    
    public void remove(int index)
    {
        sprites.remove(index);
    }

    public void remove(Sprite sprite)
    {
        sprites.remove(sprite);
    }
    
    public int size()
    {
        return sprites.size();
    }
}
