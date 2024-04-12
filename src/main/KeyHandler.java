package main;

import javax.swing.plaf.PanelUI;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean up,down,left,right,idle;
    public String last_key_pressed="up";
    @Override
    public void keyTyped(KeyEvent e) {
        //nu fac nic aicea
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code=e.getKeyCode();
        if(code==KeyEvent.VK_W||code==KeyEvent.VK_UP)//SUS
        {
            up=true;last_key_pressed="up";
        }
        if(code==KeyEvent.VK_S||code==KeyEvent.VK_DOWN)//JOS
        {
            down=true;last_key_pressed="down";
        }
        if(code==KeyEvent.VK_A||code==KeyEvent.VK_LEFT)//STANGA
        {
            left=true;last_key_pressed="left";
        }
        if(code==KeyEvent.VK_D||code==KeyEvent.VK_RIGHT)//DREAPTA
        {
            right=true;last_key_pressed="right";
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code=e.getKeyCode();
        if(code==KeyEvent.VK_W||code==KeyEvent.VK_UP)//SUS
        {
            up=false;
        }
        if(code==KeyEvent.VK_S||code==KeyEvent.VK_DOWN)//JOS
        {
            down=false;
        }
        if(code==KeyEvent.VK_A||code==KeyEvent.VK_LEFT)//STANGA
        {
            left=false;
        }
        if(code==KeyEvent.VK_D||code==KeyEvent.VK_RIGHT)//DREAPTA
        {
            right=false;
        }
    }
}
