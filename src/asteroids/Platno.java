/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.Timer;

public class Platno extends JComponent implements ActionListener{
    
    public final ArrayList<Renderable> renderable;
    public final ArrayList<Logic> logic;
    public final ArrayList<Object> killList;
    public final ArrayList<Object> birthList;
    public final ArrayList<Renderable> birthRenderList;
    public final GameMaster GM;
    private Timer timer;
    
    public Platno() {       
        this.renderable = new ArrayList<>();
        this.logic = new ArrayList<>();
        this.killList = new ArrayList<>();
        this.birthList = new ArrayList<>();
        this.birthRenderList = new ArrayList<>();
        this.setFocusable(true);
        GM = new GameMaster(this);
        logic.add(GM);
        GM.init();
    }
    
    public void init() {
        timer = new Timer(50, (ActionListener) this);
        timer.start();
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, 800, 600);
        for (Renderable t : renderable) {
            t.render(g);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        for(Object o : killList)
            destroy(o);
        for(Object o : birthList)
            create(o);
        for(Renderable o : birthRenderList)
            renderable.add(o);
        killList.clear();
        birthList.clear();
        for(Logic o : logic)
            o.performLogic();
        repaint();
    }
    
    private void create(Object object){
        renderable.add((Renderable) object);
        logic.add((Logic) object);
    }
    
    private void destroy(Object object){
        Iterator<Renderable> i = renderable.iterator();
        while (i.hasNext())
            if(i.next().equals(object))
                i.remove();
        Iterator<Logic> j = logic.iterator();
        while (j.hasNext())
            if(j.next().equals(object))
                j.remove();
    }
}
