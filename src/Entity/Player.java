package Entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Player extends Entity {


    public BufferedImage[] idleLeft=new BufferedImage[15];//64x64
    public BufferedImage[] idleRight=new BufferedImage[15];
    public BufferedImage[] attackLeft=new BufferedImage[23];//144x64
    public BufferedImage[] attackRight=new BufferedImage[23];
    public BufferedImage[] runRight=new BufferedImage[8];//96x64
    public BufferedImage[] runLeft=new BufferedImage[8];
    public BufferedImage[] JumpLeft=new BufferedImage[14];//144x64
    public BufferedImage[] JumpRight=new BufferedImage[14];
    GamePanel gp;
    KeyHandler KeyH;
    double spriteCounter;
    int spriteNumIdle=15;//hardcoded
    int spriteNumAttack=22;//hardcoded
    int spriteNumRun=8;//hardcoded
    int spriteNumJump=14;//hardcoded
    public Player(GamePanel gp, KeyHandler KeyH)
    {
        this.gp=gp;
        this.KeyH=KeyH;
        setDefaultValues();
        getPlayerImage();
        spriteCounter=0;
        facing="right";
    }
    public void setDefaultValues()
    {
        x=100;
        y=100;
        speed=2;
        direction="idle";
    }
    public void update()
    {
        KeyH.idle=!(KeyH.up||KeyH.down||KeyH.left||KeyH.right);
        if(KeyH.up)
        {
            direction="up";
            y-=speed;
        }
        if(KeyH.down)
        {
            direction="down";
            y+=speed;
        }
        if(KeyH.left)
        {
            direction="left";
            facing="left";
            x-=speed;
        } else
        if(KeyH.right)
        {
            direction="right";
            facing="right";
            x+=speed;
        }
        if(KeyH.idle)
        {
            direction="idle";
        }

        spriteCounter+= 0.15;
        if(spriteCounter>600)
            spriteCounter=0;

    }
    public  void  getPlayerImage()
    {
        try{
            for(int i=0;i<15;++i)//idle
            {
                idleLeft[i]= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/idle/left/tile"
                        + String.format("%03d", i) + ".png")));
                idleRight[i]= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/idle/right/tile"
                        + String.format("%03d", i) + ".png")));
            }
            for(int i=0;i<22;++i)
            {
                attackLeft[i]= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/attack/left/tile"
                    + String.format("%03d", i) + ".png")));
                attackRight[i]= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/attack/right/tile"
                        + String.format("%03d", i) + ".png")));
            }
            for(int i=0;i<8;++i)
            {
                runLeft[i]= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/run/left/tile"
                        + String.format("%03d", i) + ".png")));
                runRight[i]= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/run/right/tile"
                        + String.format("%03d", i) + ".png")));
            }
            for(int i=0;i<14;++i)
            {
                JumpLeft[i]= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/jump/left/tile"
                        + String.format("%03d", i) + ".png")));
                JumpRight[i]= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/jump/right/tile"
                        + String.format("%03d", i) + ".png")));
            }
        }
        catch(Exception e)
        {
           e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2)
    {
        BufferedImage image = null;
        int height=64,width=0;
        switch (direction){
            case"up":
                if(facing.equals("left"))
                    image=JumpLeft[(int)spriteCounter%spriteNumJump];
                else
                    image=JumpRight[(int)spriteCounter%spriteNumJump];
                width=144;
                break;
            case"down":
                image=attackRight[(int)spriteCounter%spriteNumAttack];
                width=144;
                break;
            case"left":
                image=runLeft[(int)spriteCounter%spriteNumRun];
                width=96;
                break;
            case"right":
                image=runRight[(int)spriteCounter%spriteNumRun];
                width=96;
                break;
            case"idle":
                if(facing.equals("left"))
                    image=idleLeft[(int)spriteCounter%spriteNumIdle];
                else
                    image=idleRight[(int)spriteCounter%spriteNumIdle];
                width=64;
                break;
        }
        g2.drawImage(image,x,y,width*3,height*3,null);
    }
}
