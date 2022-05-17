package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Enemy {
    private Random random = new Random();
    protected int px =0 ,py;
    protected int vx = 2,vy = 1;
    private int health = 1;
    private boolean isInverted =false;
    private boolean isCircled =false;
    private boolean isBossLaunched = false;

    Image enemyShape;
    Enemy(){
        enemyShape = new ImageIcon("Rock.png").getImage();
    }

    //set enemy position random in regular levels
    public void setEnemyInitialPos() {
        px = random.nextInt(700);
        py = 0 - enemyShape.getHeight(null);
    }
    //set only boss position
    public void setEnemyInitialPos(int py) {
        px = random.nextInt(700);
        this.py = 0 - py - enemyShape.getHeight(null);
    }
    //set position to enemies fallen from boss
    public void setEnemyInitialPos(int px,int py) {
        this.px = px;
        this.py = py;
    }
    public void setHealth(int health){
        this.health = health;
    }
    public int getHealth(){return health;}

   //only boss move(doesn't go after paddle just stay on his range)
    public void move(){
        if(py>350) {
            vy *= -1;
        }
        if(py<=3&&isBossLaunched){
            vy *= -1;
        }
        if(px<=0 || px>=800-70) {
            vx *= -1;
        }
        if(py>20){
            isBossLaunched = true;
        }

        px += vx;
        py += vy;
    }

    //move of the regular enemies to catch the paddle
    public void move(Paddle paddle) {
        //seek for paddle and go after it
            if (paddle.paddle_xp + paddle.getImage().getWidth(null) / 2 < px && !isCircled) {
                vx = -1;
            } else if (paddle.paddle_xp + paddle.getImage().getWidth(null) / 2 > px && !isCircled) {
                vx = 1;
            } else {
                vx = 0;
            }
            //when enemy get to specific point start the circled moves
            if (py > 400 && !isInverted) {
                isCircled = true;
                vy = -1;
            }
            //when enemy get to the upper limit in while circle shape movement
            else if (py < 300 && vy < 0) {
                isInverted = true;
                vy = 1;
            }
            //when enemy get to specific but it already did the circle shape movement
            if (py > 400 && isInverted) {
                isCircled = false;
            }
            //circle shape in movement
            if (isCircled) {
                if (py >= 350) {
                    vx = 1;
                } else {
                    vx = -1;
                }
            }
            px += vx;
            py += vy;

            if(py>560){
                vy = 0;
                vx = 0;
            }
        }
}
