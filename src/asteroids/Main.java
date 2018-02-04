package asteroids;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;

public class Main{
    private JFrame window;
    private Platno platno;
    
    final int width = 800;
    final int height = 600;
    
    public Main(){
        window = new JFrame("Asteroids");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(width, height);
        platno = new Platno();
        platno.init();
        Container pane = window.getContentPane();
        pane.add(platno,BorderLayout.CENTER);
        window.setVisible(true);
    }
    
    public static void main(String[] args) {
    new Main();
    }

}