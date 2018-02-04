/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids;

import java.awt.Color;
import java.time.Clock;

/**
 *
 * @author Vikto
 */
public class GameMaster implements Logic{
    
    private int iterationCount; //Kolikrát hráč zničil všechny asteroidy.
    private static final int NEW_ITERATION_DELAY = 5000;
    private static final int RESPAWN_DELAY = 5000;
    private static final int ROUND_COMPLETION_BONUS = 1000;
    private static final int INITIAL_ASTEROIDS = 1;
    private static final int INITIAL_LIVES_COUNT = 3;
    private int lives = INITIAL_LIVES_COUNT;
    private final Platno rootReference;
    private int score;
    private TextElement scoreCounter;
    private TextElement livesCounter;
    private TextElement iterationCounter;
    private long timeOfAdvancement;
    private long timeOfDeath;
    private long gracePeriodStart;
    
    public GameMaster(Platno rootReference){
        iterationCount = 0;
        score = 0;
        timeOfAdvancement = 0;
        timeOfDeath = 0;
        this.rootReference = rootReference;
    }
    
    public void init(){
        Asteroid asteroid;
        Ship ship = new Ship(400,300,rootReference);
        rootReference.renderable.add(ship);
        rootReference.logic.add(ship);
        rootReference.addKeyListener(ship);
        scoreCounter = new TextElement("Skore: "+score,Color.WHITE,20,20);
        livesCounter = new TextElement("Životy: "+lives,Color.WHITE,20,40);
        iterationCounter = new TextElement("Level: "+iterationCount+1,Color.WHITE,20,60);
        rootReference.renderable.add(scoreCounter);
        rootReference.renderable.add(livesCounter);
        for(int i = 0;i<INITIAL_ASTEROIDS;i++){
            asteroid = new Asteroid((int)(Math.random()*800),(int)(Math.random()*600),0,rootReference);
            rootReference.renderable.add(asteroid);
            rootReference.logic.add(asteroid);
        }
    }
    
    public void addScore(int x){
        score += x;
    }
    
    @Override
    public void performLogic() {
        
        //Část starající se o asteroidy, skóre a extra životy.
        int previousScore = score;
        boolean advance = true;
        for(Logic o : rootReference.logic){
            if(o.getClass() == Asteroid.class){
                advance = false;
                break;
            }
        }
        if(advance && timeOfAdvancement == 0){
            timeOfAdvancement = Clock.systemUTC().millis();
        }
        if(advance && Clock.systemUTC().millis() - timeOfAdvancement > NEW_ITERATION_DELAY){
            iterationCount++;
            iterationCounter.setText("Level: "+iterationCount+1);
            score+=ROUND_COMPLETION_BONUS*iterationCount;
            for(int i = 0;i<INITIAL_ASTEROIDS+iterationCount;i++){
                rootReference.birthList.add(new Asteroid((int)(Math.random()*800),(int)(Math.random()*600),0,rootReference));
            }
            timeOfAdvancement = 0;
            for(Logic o : rootReference.logic){
                if(o.getClass() == Ship.class){
                    ((Ship)o).restartGrace();
                    break;
                }
            }
        }
        lives += score/10000-previousScore/10000;
        scoreCounter.setText("Skore: "+score);
        
        //Část starající se o hráče.
        boolean dead = true;
        for(Logic o : rootReference.logic){
            if(o.getClass() == Ship.class){
                dead = false;
                break;
            }
        }
        if(dead && timeOfDeath == 0){
            timeOfDeath = Clock.systemUTC().millis();
        }
        if(lives > 0 && dead && Clock.systemUTC().millis() - timeOfDeath > RESPAWN_DELAY){
            Ship ship = new Ship(400,300,rootReference);
            rootReference.addKeyListener(ship);
            rootReference.birthList.add(ship);
            timeOfDeath = 0;
            lives--;
            livesCounter.setText("Životy:"+lives);
            if(lives == 1)
                livesCounter.setColor(Color.YELLOW);
            if(lives == 0)
                livesCounter.setColor(Color.RED);
        }
        
    }
    
}
