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
public class Asteroid extends LineObject{
    
    static final int SCORE_VALUE = 100;
    static final int SPLITS = 8;
    static final double SPLIT_ANGLE = (Math.PI*2) / SPLITS;
    static final int MAX_SPEED = 12;
    static final int MAX_ITERATIONS = 2;
    static final int DEBRIS_COUNT = 3;
    static final int DEBRIS_SCATTER_MAX_DISTANCE = 10;
    static final int MAX_POINT_DISTANCE = 50;
    static final int MIN_POINT_DISTANCE = 15;
    static final int ITERATION_QUOTIENT = 2;
    private final int iterationCount;
    private final Platno rootReference;
    public Asteroid(int x,int y,int iterationCount,Platno rootReference){
        this.rootReference = rootReference;
        this.iterationCount = iterationCount;
        this.location.x = x;
        this.location.y = y;
        double[] angles = new double[SPLITS];
        for(int i=0;i<SPLITS;i++){
            angles[i] = Math.random() * ( (SPLIT_ANGLE  * (i+1)) - (SPLIT_ANGLE * i) ) + (SPLIT_ANGLE * i);
        }
        for(int i=0;i<SPLITS;i++){
            baseOffsets.add(new Point(
            (int) ((Math.random() * (MAX_POINT_DISTANCE - MIN_POINT_DISTANCE) + MIN_POINT_DISTANCE) * Math.cos(angles[i]))/(iterationCount == 0?1:iterationCount*ITERATION_QUOTIENT),
            (int) ((Math.random() * (MAX_POINT_DISTANCE - MIN_POINT_DISTANCE) + MIN_POINT_DISTANCE) * Math.sin(angles[i]))/(iterationCount == 0?1:iterationCount*ITERATION_QUOTIENT)        
            ));
        }
        for(int i = 0;i<8;i++){
            actualOffsets.add(new Point());
        }
        do{
            speedX = Math.random() * MAX_SPEED;
            speedY = Math.random() * MAX_SPEED;
        }while(Math.sqrt(speedX*speedX+speedY*speedY) > MAX_SPEED);
    }
    
    public void destroy(){
        if(iterationCount < MAX_ITERATIONS){
            for(int i=0;i<DEBRIS_COUNT;i++)
                rootReference.birthList.add(new Asteroid(
                                    (int)(location.x+(Math.random() * (2*DEBRIS_SCATTER_MAX_DISTANCE)-DEBRIS_SCATTER_MAX_DISTANCE)),
                                    (int)(location.y+(Math.random() * (2*DEBRIS_SCATTER_MAX_DISTANCE)-DEBRIS_SCATTER_MAX_DISTANCE)),
                                    iterationCount+1,
                                    rootReference));
        }
        rootReference.GM.addScore(SCORE_VALUE);
        rootReference.killList.add(this);
    }
    
}
