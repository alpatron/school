/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author Vikto
 */
public class TextElement implements Renderable{
    private String text;
    private Point location;
    private Color color;
    public TextElement(String text,Color color,int x,int y){
        this.text = text;
        this.color = color;
        this.location = new Point(x,y);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    @Override
    public void render(Graphics g) {
        g.setColor(color);
        g.drawString(text, location.x, location.y);
    }
    
}
