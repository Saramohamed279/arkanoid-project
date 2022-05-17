package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Level {
    protected static int current_lvl = 0;
    private int gridStartX = 140;
    private int gridStartY = 82;
    private Random random = new Random();

//fallen enemies variable
Enemy enemy[] = new Enemy[100];
    private int createdEnemies;
    private boolean isEnemyAdded = false;

//Boss variables
    protected Enemy Boss;
    private boolean isBossAdded = false;
    protected static boolean isBallInverted = false;

//power up variables
PowerUp powerup[] = new PowerUp[50];
    int createdPowerups = 0;
Timer Pwrtimer = new Timer();
    int elapsedPowerupTime;
Image pwrupImages[] = new Image[5];

//laser variables
PowerUp laser[] = new PowerUp[100];
private int createdLaser;
private boolean isLaserActivated;
private Timer laserTimer = new Timer();
private int elapsedLaserTime;
private Image laserImage;

//level blocks variable
Block currentLvlBlock[][]= new Block[8][8];
    protected int drawnBlocks = 0;
    private levels selectedlevel;


    Level(levels selectedlevel) {
        this.selectedlevel = selectedlevel;

        elapsedLaserTime = 0;
        elapsedPowerupTime = 0;
        isLaserActivated = false;

        createdEnemies = 0;
        createdPowerups = 0;
        createdLaser = 0;

        // load power up images
        pwrupImages[0] = new ImageIcon("paddleplus.png").getImage();
        pwrupImages[1] = new ImageIcon("laser pwrup.png").getImage();
        pwrupImages[2] = new ImageIcon("speedUP.png").getImage();
        pwrupImages[3] = new ImageIcon("enemy.png").getImage();
        pwrupImages[4] = new ImageIcon("Health.png").getImage();
        laserImage = new ImageIcon("laiser.png").getImage();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //set blocks' color ,health and position
                currentLvlBlock[i][j] = new Block();
                if(selectedlevel.lvl[i][j]>4 || selectedlevel.lvl[i][j]==1){
                    currentLvlBlock[i][j].setHealth(1);
                }
                else if(selectedlevel.lvl[i][j]<=4 && selectedlevel.lvl[i][j]!=0){
                    currentLvlBlock[i][j].setHealth(2);
                }
                currentLvlBlock[i][j].setBlockShape(selectedlevel.lvl[i][j]);
                currentLvlBlock[i][j].setBlockPosition(gridStartX + (currentLvlBlock[i][j].getBlockWidth() * j), gridStartY + (currentLvlBlock[i][j].getBlockHeight() * i));

                //add power up and stick it to the block
                if(selectedlevel.lvl[i][j] > 0){
                    drawnBlocks++;
                    if (drawnBlocks % 3 == 0){
                        if(random.nextBoolean()){
                            addPowerup(random.nextInt(5),currentLvlBlock[i][j]);
                            currentLvlBlock[i][j].carriedPwrupIndex = createdPowerups;
                            createdPowerups++;
                        }
                    }
                }
                gridStartX += 7;
            }
                gridStartX = 140;
                gridStartY += 10;
        }
        current_lvl++;

        }

public void addEnemy(int score){
        if(score%40 == 0 && createdEnemies<10 && score!=0 && !isEnemyAdded) {
            enemy[createdEnemies] = new Enemy();
            enemy[createdEnemies].setEnemyInitialPos();
            createdEnemies++;
            isEnemyAdded =true;
        }
        if(score%40!=0)
            isEnemyAdded = false;
        }
public void addBoss(){
        Boss = new Enemy();
        Boss.setHealth(25);
        Boss.setEnemyInitialPos(70);
        isBossAdded = true;
}

// add power ups when hit the boss
public void addPowerup(int pwrupIndex,Enemy Boss){
    powerup[createdPowerups] = new PowerUp(pwrupImages[pwrupIndex],pwrupIndex, Boss.px+35-11,Boss.py+70);
    powerup[createdPowerups].setGravity(1);
    createdPowerups++;
}

public void addPowerup(int pwrupIndex,Block block){
        powerup[createdPowerups] = new PowerUp(pwrupImages[pwrupIndex],pwrupIndex, block.px,block.py);
        }

public void setPwrupGravity(int carriedPwrupIndex){
         if(carriedPwrupIndex != -1)
            powerup[carriedPwrupIndex].setGravity(1);
    }

public void applyLaser(Paddle paddle){
            laserTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (isLaserActivated) {
                        elapsedLaserTime++;
                        laser[createdLaser] = new PowerUp(laserImage, createdLaser, paddle.paddle_xp, paddle.paddle_yp-laserImage.getHeight(null)-5);
                        laser[createdLaser].setGravity(-3);

                        laser[createdLaser + 1] = new PowerUp(laserImage, createdLaser, paddle.paddle_xp + paddle.getImage().getWidth(null) - 15, paddle.paddle_yp-laserImage.getHeight(null)-5);
                        laser[createdLaser + 1].setGravity(-3);
                        createdLaser += 2;
                    }
                }
            }, 500, 1000);
        }

public int applyPowerupEffect(Paddle paddle,Ball ball,PowerUp powerup,int lives){
    switch (powerup.pwrupIndex){
        case 0 :
            //paddle width power up
            elapsedPowerupTime =0;
            Pwrtimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    elapsedPowerupTime++;
            //System.out.println("seconds "+elapsedPowerupTime);
                }
            }, 1000, 1000);
            paddle.setFixedPaddleColor(true);
            //done
            break;
        case 1 :
            //laser power up
            isLaserActivated = true;
            applyLaser(paddle);
            //System.out.println("laser");
            break;
        case 2 :
            //speedy ball power up
            ball.ball_speed+=1;
            //System.out.println("speed up");
            break;
        case 3 :
            //new enemy
            enemy[createdEnemies] = new Enemy();
            enemy[createdEnemies].setEnemyInitialPos();
            createdEnemies++;
            break;
        case 4 :
             //new life
             lives++;
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + powerup.pwrupIndex);
    }

    return lives;
}

public void move(Paddle paddle){
            // make enemies keep falling
            for(int i=0;i<createdEnemies;i++)
                enemy[i].move(paddle);
            // make power ups keep falling
            for(int i=0;i<createdPowerups;i++)
                powerup[i].move();
            // make laser ho up
            for(int i=0;i<createdLaser;i++){
                if(laser[i].getGravity() != 0)
                laser[i].move();
            }
            // make boss moves
            if(isBossAdded)
                Boss.move();
    }

public int checkCollision(Paddle paddle,Ball ball,int lives) {
Rectangle paddleCollider = new Rectangle(paddle.paddle_xp, paddle.paddle_yp, paddle.getImage().getWidth(null), paddle.getImage().getHeight(null));
Rectangle ballCollider = new Rectangle(ball.ball_xp, ball.ball_yp, ball.ballImage[0].getWidth(null), ball.ballImage[0].getHeight(null));

    for (int i = 0; i < createdEnemies; i++) {
        //detect paddle with enemy collision
Rectangle enemyCollider = new Rectangle(enemy[i].px,enemy[i].py,enemy[i].enemyShape.getWidth(null),enemy[i].enemyShape.getHeight(null));
           if (enemyCollider.intersects(paddleCollider)){
                    lives--;
                    enemy[i].py = 1000;
           }
           //detect ball with enemy collision
        if (enemyCollider.intersects(ballCollider)){
            enemy[i].py = 1000;
            enemy[i].vx = 0;
            enemy[i].vy = 0;
        }
        }
        // power up with paddle
        for(int i =0;i<createdPowerups;i++){
Rectangle powerupCollider = new Rectangle(powerup[i].px,powerup[i].py,powerup[i].powerupImage.getWidth(null),powerup[i].powerupImage.getHeight(null));
          if(powerupCollider.intersects(paddleCollider)){
              lives = applyPowerupEffect(paddle,ball,powerup[i],lives);
              powerup[i].py = 1000;
              powerup[i].setGravity(0);
          }
        }

        for(int m = 0;m<createdLaser;m++){
Rectangle laserCollider = new Rectangle(laser[m].px,laser[m].py,laser[m].powerupImage.getWidth(null),laser[m].powerupImage.getHeight(null));
       //laser with blocks
        for(int i = 0;i<currentLvlBlock.length;i++){
           for (int j =0;j<currentLvlBlock[i].length;j++) {
Rectangle blockCollider = new Rectangle(currentLvlBlock[i][j].px,currentLvlBlock[i][j].py,currentLvlBlock[i][j].getBlockWidth(),currentLvlBlock[i][j].getBlockHeight());
if(blockCollider.intersects(laserCollider)){
    if(currentLvlBlock[i][j].getHealth()>0) {
        currentLvlBlock[i][j].setHealth(currentLvlBlock[i][j].getHealth() - 1);
        currentLvlBlock[i][j].setBlockShape(currentLvlBlock[i][j].getHealth());
        setPwrupGravity(currentLvlBlock[i][j].carriedPwrupIndex);
        selectedlevel.lvl[i][j] -= 1;
        laser[m].py = -20;
        laser[m].setGravity(0);
    }
    if(currentLvlBlock[i][j].getHealth() == 0) {
        drawnBlocks--;
    }
}
           }
       }
        //laser with enemy check
       for (int i = 0; i < createdEnemies; i++) {
            Rectangle enemyCollider = new Rectangle(enemy[i].px,enemy[i].py,enemy[i].enemyShape.getWidth(null),enemy[i].enemyShape.getHeight(null));
            if (enemyCollider.intersects(laserCollider)){
                enemy[i].py = 1000;
                enemy[i].vy = 0;
                enemy[i].vx = 0;
                laser[m].py = -20;
                laser[m].setGravity(0);
            }
            }
        }
if(isBossAdded) {
    Rectangle BossCollider = new Rectangle(Boss.px, Boss.py, 70, 70);
    //Rectangle ballCollider = new Rectangle(ball.ball_xp,ball.ball_yp,ball.ballImage[0].getWidth(null),ball.ballImage[0].getHeight(null));

    if (ballCollider.intersects(BossCollider) && !isBallInverted) {
        //System.out.println(Boss.getHealth());
        if (Boss.getHealth() > 0) {
            Boss.setHealth(Boss.getHealth() - 1);

            //System.out.println(Boss.getHealth());
            if (BossCollider.getX() >= ball.ball_xp - 11 + ball.ballImage[0].getWidth(null) ||
                    ball.ball_xp >= BossCollider.getX() + 70 - 8) {
                if (ball.ball_xv == 0) {
                    ball.ball_yv = 1;
                    Boss.vy = -1;
                } else {
                    ball.ball_xv *= -1;
                    Boss.vx *= -1;
                }
            } else if (ballCollider.getX() >= ball.ball_xp - 8 + ball.ballImage[0].getHeight(null)
                    || BossCollider.getX() <= ball.ball_xp + 70 + 30) {
                ball.ball_yv *= -1;
                Boss.vy *= -1;
            }
            if (Boss.getHealth() % 1 == 0) {
                enemy[createdEnemies] = new Enemy();
                enemy[createdEnemies].setEnemyInitialPos(Boss.px + 35, Boss.py + 70);
                createdEnemies++;
                addPowerup(random.nextInt(5), Boss);
            }
            isBallInverted = true;
        }
    }
    for (int m = 0; m < createdLaser; m++) {
        Rectangle laserCollider = new Rectangle(laser[m].px, laser[m].py, laser[m].powerupImage.getWidth(null), laser[m].powerupImage.getHeight(null));

        if (BossCollider.intersects(laserCollider)) {
            laser[m].py = -20;
            laser[m].setGravity(0);
            if (Boss.getHealth() > 0) {
                Boss.setHealth(Boss.getHealth() - 1);
            }
        }

    }
}
    if(elapsedPowerupTime>6){
        paddle.setFixedPaddleColor(false);
        elapsedPowerupTime =0;
    }
    if(elapsedLaserTime>8){
        isLaserActivated = false;
        elapsedLaserTime = 0;
    }
            return lives;
        }

public void draw(Graphics2D g) {
        //Block draw
        for (int i = 0; i < currentLvlBlock.length; i++) {
            for (int j = 0; j < currentLvlBlock.length; j++) {
                if(selectedlevel.lvl[i][j]!=0)
                g.drawImage(currentLvlBlock[i][j].blockColor, currentLvlBlock[i][j].px, currentLvlBlock[i][j].py, null);
            }
        }
        //Enemy draw
        for(int i=0;i<createdEnemies;i++)
            g.drawImage(enemy[i].enemyShape,enemy[i].px,enemy[i].py,null);
        //power up draw
        for(int i = 0;i<createdPowerups;i++) {
            if(powerup[i].getGravity()!=0)
            g.drawImage(powerup[i].powerupImage, powerup[i].px, powerup[i].py, 22, 22, null);
        }
        for(int i=0;i<createdLaser;i++){
            if(laser[i].getGravity() != 0)
            g.drawImage(laser[i].powerupImage,laser[i].px,laser[i].py,null);
        }
        if(isBossAdded) {
            // display boss's health
            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Boss's health: " + Boss.getHealth(), 320, 40);
            //boss draw
            g.drawImage(Boss.enemyShape, Boss.px, Boss.py, 70, 70, null);

        }
    }

}
class levels{
    int lvl[][];
    levels(){
        lvl = new int[8][8];
    }
}

