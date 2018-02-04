/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids;

import java.awt.Point;

/**
 *
 * @author Vikto
 */
public class Projectile extends LineObject{
    static final double SPEED = 10;
    private final Platno rootReference;
    public Projectile(int x,int y,double rotation,Platno rootReference){
        this.rootReference = rootReference;
        this.location.x = x;
        this.location.y = y;
        this.rotation = rotation;
        baseOffsets.add(new Point(0,0));
        baseOffsets.add(new Point(0,20));
        for(Point p : baseOffsets)
            actualOffsets.add((Point)p.clone());
        this.speedX = SPEED * Math.sin(rotation);
        this.speedY = SPEED * Math.cos(rotation)*(-1);
    }
    
    @Override
    public void performLogic(){
        location.translate((int)speedX, (int)speedY);
        
        if(location.x < 0 || location.x > 800 || location.y < 0 || location.y > 600){ //Zobecnit později.
            rootReference.killList.add(this);
        }
        
        location.translate((int)speedX, (int)speedY);
        //Kolize
        Asteroid asteroid;
        for(Logic o : rootReference.logic){
            if (o.getClass() == Asteroid.class){
                asteroid = (Asteroid)o;
                if(colides(asteroid)){
                    asteroid.destroy();
                    rootReference.killList.add(this);
                }
            }
        }
    }
}
