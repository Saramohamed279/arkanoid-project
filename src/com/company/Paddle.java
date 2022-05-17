package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Paddle{
    protected   Image paddleImage[] = new Image[4];
    private Image fixedPaddleImage[] = new Image[4];
    private boolean isFixedPaddleApllied = false;
    protected int paddle_xp;
    protected int paddle_yp;
    private int paddle_xv = 0;
    private int selectedPaddle = 0;
    private boolean isBallLaunched = false;

    Paddle(int GameWidth,int GameHeight){
        paddleImage[0] = new ImageIcon("bluePaddle.png").getImage();
        paddleImage[1] = new ImageIcon("greenPaddle.png").getImage();
        paddleImage[2] = new ImageIcon("orangePaddle.png").getImage();
        paddleImage[3] = new ImageIcon("redPaddle.png").getImage();
        setPaddleInitialPosition(GameWidth,GameHeight);
        fixedPaddleImage[0] = new ImageIcon("blueLargePaddle.png").getImage();
        fixedPaddleImage[1] = new ImageIcon("greenLargePaddle.png").getImage();
        fixedPaddleImage[2] = new ImageIcon("orangeLargePaddle.png").getImage();
        fixedPaddleImage[3] = new ImageIcon("redLargePaddle.png").getImage();

    }

    public void setPaddleInitialPosition(int GameWidth,int GameHeight){
        paddle_xp = GameWidth/2 - (paddleImage[0].getWidth(null)/2);
        paddle_yp = GameHeight - 50;
        paddle_xv = 0;
    }

public void setBallStatus(boolean isBallLaunched){
        this.isBallLaunched = isBallLaunched;
    }

public void setPaddleColor(int selectedPaddle){this.selectedPaddle = selectedPaddle;}

public void setFixedPaddleColor(boolean isFixedPaddleApllied){this.isFixedPaddleApllied = isFixedPaddleApllied;}

public Image getImage(){
        if(!isFixedPaddleApllied){
            return paddleImage[selectedPaddle];
        }
        else{
            return fixedPaddleImage[selectedPaddle];
        }
}

    public void keyPressed(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode()==KeyEvent.VK_RIGHT && isBallLaunched){
            paddle_xv = 15;
        }
        else if(keyEvent.getKeyCode()==KeyEvent.VK_LEFT && isBallLaunched){
            paddle_xv = -15;
        }
    }
    public void keyReleased(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode()==KeyEvent.VK_RIGHT || keyEvent.getKeyCode()==KeyEvent.VK_LEFT)
        {
            paddle_xv = 0;
        }
    }

    public void draw(Graphics2D g){
        if(!isFixedPaddleApllied)
            g.drawImage(paddleImage[selectedPaddle], paddle_xp, paddle_yp,null);
        else
            g.drawImage(fixedPaddleImage[selectedPaddle], paddle_xp, paddle_yp,null);
    }

    public void move(){
        paddle_xp += paddle_xv;
    }

}