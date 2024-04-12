package main;

import Entity.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize=64;
    final int scale=2;
    public final int tileSize=originalTileSize*scale;
    final int maxScreenCol=12;
    final int maxScreenRow=6;
    final int screenWidth=maxScreenCol*tileSize;
    final int screenHeight=maxScreenRow*tileSize;

    int FPS=60;
    Thread gameThread;


    KeyHandler KeyH=new KeyHandler();
    Player player=new Player(this,KeyH);

    //Set Player's default position
    int Xp=100;
    int Yp=100;
    int pSpeed=8;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(KeyH);
        this.setFocusable(true);

    }
public void startGameThread()
{
    gameThread=new Thread(this);
    gameThread.start();
}

    @Override
    public void run() {
        double drawInterval=1000000000/FPS;
        double delta=0;
        long lastTime=System.nanoTime();
        long currentTime;

        while(gameThread.isAlive())
        {
            currentTime =System.nanoTime();
            delta+=(currentTime-lastTime)/drawInterval;
            lastTime=currentTime;
            if(delta>=1) {
                delta--;
                update();
                repaint();
            }
        }
    }

    public void update(){
        player.update();
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 =(Graphics2D) g;
        player.draw(g2);

        g2.dispose();
    }

}
