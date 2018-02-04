/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.Clock;
import java.util.ArrayList;

/**
 *
 * @author student
 */
public class Ship extends LineObject implements KeyListener, Logic{
    private boolean thrust;
    private static final double FORCE = 1; //Síla způsobená motorem.
    private ArrayList<Point> thrustEffectPoints;
    private ArrayList<Point> thrustEffectPointsRotated;
    private static final double SPEED_LIMIT = 24;
    private final Platno rootReference;
    private long gracePeriodStart;
    private boolean gracePeriod;
    private static final int GRACE_PERIOD_DURATION = 2000;
    
    public Ship(int x,int y,Platno rootReference){
        this.rootReference = rootReference;
        this.color = Color.GRAY;
        this.gracePeriodStart = Clock.systemUTC().millis();
        this.gracePeriod = true;
        this.thrust = false;
        this.location = new Point(x,y);
        this.rotation = 0;
        this.speedX = 0;
        this.speedY = 0;
        baseOffsets.add(new Point(0,-15));
        baseOffsets.add(new Point(15,15));
        baseOffsets.add(new Point(12,9));
        baseOffsets.add(new Point(-12,9));
        baseOffsets.add(new Point(-15,15));
        for(Point p : baseOffsets)
            actualOffsets.add((Point)p.clone());
        thrustEffectPoints = new ArrayList<>();
        thrustEffectPoints.add(new Point(9,9));
        thrustEffectPoints.add(new Point(-9,9));
        thrustEffectPoints.add(new Point(-9,18));
        thrustEffectPoints.add(new Point(9,18));
        thrustEffectPointsRotated = new ArrayList<>();
        for(Point p : thrustEffectPoints)
            thrustEffectPointsRotated.add((Point)p.clone());
    }

    @Override
    public void performLogic() {
        //Pohyb
        if(thrust){
            speedX += FORCE * Math.sin(rotation);
            speedY -= FORCE * Math.cos(rotation);
            double size = Math.sqrt(speedX*speedX+speedY*speedY); 
            if(size > SPEED_LIMIT){
                speedX *= SPEED_LIMIT / size;
                speedY *= SPEED_LIMIT / size;
            }       
        }
        super.performLogic();
        
         //Kolize
        if(gracePeriod && Clock.systemUTC().millis() - gracePeriodStart > GRACE_PERIOD_DURATION){
            gracePeriod = false;
            this.color = Color.WHITE;    
        }
        if(!gracePeriod){
            Asteroid asteroid;
            for(Logic o : rootReference.logic){
                if (o.getClass() == Asteroid.class){
                    asteroid = (Asteroid)o;
                    if(colides(asteroid)){
                        rootReference.killList.add(this);
                        rootReference.birthRenderList.add(new TextElement("RIP",Color.RED,location.x,location.y));
                        rootReference.removeKeyListener(this);
                        asteroid.destroy();
                    }

                }
            }
        }
    }
    
    @Override
    public void render(Graphics g){
        super.render(g);
        if(thrust){
            int x,y;
            int xArray[] = new int [4];
            int yArray[] = new int [4];
            for(int i = 0;i<thrustEffectPoints.size();i++){
                x = thrustEffectPoints.get(i).x;
                y = thrustEffectPoints.get(i).y;
                thrustEffectPointsRotated.get(i).x = (int) (x*Math.cos(rotation) - y*Math.sin(rotation)); 
                thrustEffectPointsRotated.get(i).y = (int) (y*Math.cos(rotation) + x*Math.sin(rotation));
            }
            for(int i = 0;i<thrustEffectPointsRotated.size();i++){
                xArray[i] = location.x+thrustEffectPointsRotated.get(i).x;
                yArray[i] = location.y+thrustEffectPointsRotated.get(i).y;
            }
            g.setColor(Color.RED);
            g.drawPolygon(new Polygon(xArray,yArray,4));
        }
            
    }
    
    public void restartGrace(){
        gracePeriod = true;
        gracePeriodStart = Clock.systemUTC().millis();
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                rotationDirection = -1;
                break;
            case KeyEvent.VK_RIGHT:
                rotationDirection = 1;
                break;
            case KeyEvent.VK_UP:
                thrust = true;
                break;
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                rotationDirection = 0;
                break;
            case KeyEvent.VK_UP:
                thrust = false;
                break;
            case KeyEvent.VK_SPACE:
                rootReference.birthList.add(new Projectile(location.x,location.y,rotation,rootReference));
        }
    }
    
}
