package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class gamePanel extends JPanel implements Runnable, MouseListener, MouseMotionListener, KeyListener {
    private final int GameWidth=800;
    private final int GameHeight=600;
    private final int menuindex = 0;
    private final int gameindex = 1;
    private final int pauseScreenIndex = 2;
    private final int lvlCompletedScreen = 3;
    private final int gameOverIndex = 4;
    private final int youWonScreenIndex = 5;
    private int addedLevels = 0;
    public int live_index = 0;
    private int lives = 3;
    private int liveScore=0;

    Menu gameMenu;
    private Image backg[] =  new Image[7];
    private Image healthImage[];

    //screens images
    private Image pauseBackg;
    private Image home_btn;
    private Image resume_btn;
    private Image gameOver;
    private Image levelCompleted;
    private Image youWonScreen;

    //buttons
    private Image pause_btn[] = new Image[2];
    private int animatedPause_btn = 0;
    private Image next_btn;
    private int pauseMenuBtnsPos[][] = {
            {310,376}, //home btn
            {415,376} //resume btn
    };
    private Paddle paddle;
    protected Ball ball;
    private Thread GameThread;
    private boolean isInversed = false;
    private Dimension gameSize;

    private Level lvl1;
    private levels selectedLvl[];
    private boolean isBossAdded = false;

    gamePanel(){
        //set ball nad paddle
        paddle = new Paddle(GameWidth,GameHeight);
        ball = new Ball(0,paddle,live_index);

        gameMenu = new Menu(live_index,ball,paddle);
    //set panel size
        gameSize = new Dimension(GameWidth,GameHeight);
        this.setPreferredSize(gameSize);

     //Buttons
     pause_btn[0] = new ImageIcon("pause_Button.png").getImage();
     pause_btn[1] = new ImageIcon("pause_Button_fullOP.png").getImage();
     home_btn = new ImageIcon("home_Button.png").getImage();
     resume_btn = new ImageIcon("cont_Button.png").getImage();
     next_btn = new ImageIcon("next_button.png").getImage();

        //screeens
     pauseBackg = new ImageIcon("overBackground.png").getImage();
     gameOver = new ImageIcon("gameOver.png").getImage();
     levelCompleted = new ImageIcon("levelCompleted.png").getImage();
     youWonScreen = new ImageIcon("youwon.png").getImage();

     healthImage = new Image[7];
     for(int i=0;i<7;i++)
         healthImage[i]= new ImageIcon("Health.png").getImage();

     //Backgrouns
        backg[0] = new ImageIcon("background5.jpg").getImage();
        backg[1] = new ImageIcon("background4.jpg").getImage();
        backg[2] = new ImageIcon("background2.jpg").getImage();
        backg[3] = new ImageIcon("background6.jpg").getImage();
        backg[4] = new ImageIcon("background3.jpg").getImage();
        backg[5] = new ImageIcon("background1.jpg").getImage();
        backg[6] = new ImageIcon("background6.jpg").getImage();

    //create new levels
     selectedLvl = new levels[7];

       setInitialLevelsSeq();
       //set start level
       lvl1 = new Level(selectedLvl[Level.current_lvl]);

        GameThread=new Thread(this);
        GameThread.start();
    }

//Game draw
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        //Main menu draw
        if (live_index == menuindex) {
            gameMenu.draw(g2d);
            live_index = gameMenu.live_index;
            ball.setIndex(live_index);
        }
        //in-game draw
        else if(live_index == gameindex){
            g2d.drawImage(backg[Level.current_lvl-1], 0, 0, null);
            g2d.drawImage(pause_btn[animatedPause_btn],10,10,null);
            for (int i = 0; i < lives; i++)
                g2d.drawImage(healthImage[i], 10 + (healthImage[i].getWidth(null) * i), 560, null);
            g2d.setColor(Color.white);
            g2d.setFont(new Font("serif", Font.BOLD, 25));
            g2d.drawString("Your Score: " + liveScore, 600, 580);
            paddle.draw(g2d);
            lvl1.draw(g2d);
            ball.draw(g2d);
        }
        //pause screen draw
        else if(live_index == pauseScreenIndex){
            g2d.drawImage(backg[Level.current_lvl-1], 0, 0, null);
            for (int i = 0; i < lives; i++)
            g2d.drawImage(healthImage[i], 10 + (healthImage[i].getWidth(null) * i), 560, null);
            g2d.setColor(Color.white);
            g2d.setFont(new Font("serif", Font.BOLD, 25));
            g2d.drawString("Your Score: " + liveScore, 600, 580);
            paddle.draw(g2d);
            lvl1.draw(g2d);
            ball.draw(g2d);
            g2d.drawImage(pauseBackg,0,0,null);
            g2d.drawImage(home_btn,pauseMenuBtnsPos[0][0],pauseMenuBtnsPos[0][1],null);
            g2d.drawImage(resume_btn,pauseMenuBtnsPos[1][0],pauseMenuBtnsPos[1][1],null);
            g2d.setFont(new Font("serif",Font.PLAIN,76));
            g2d.drawString("LEVEL " + Level.current_lvl,236,302);
        }
        //game over screen draw
        else if(live_index == gameOverIndex){
            g2d.drawImage(backg[Level.current_lvl-1], 0, 0, null);
            for (int i = 0; i < lives; i++)
                g2d.drawImage(healthImage[i], 10 + (healthImage[i].getWidth(null) * i), 560, null);
            g2d.setColor(Color.white);
            g2d.setFont(new Font("serif", Font.BOLD, 25));
            g2d.drawString("Your Score: " + liveScore, 600, 580);
            paddle.draw(g2d);
            lvl1.draw(g2d);
            ball.draw(g2d);
            g2d.drawImage(gameOver,0,0,null);
            g2d.drawImage(home_btn,pauseMenuBtnsPos[0][0],pauseMenuBtnsPos[0][1],null);
        }
        //level completed screen
        else if(live_index == lvlCompletedScreen){
            g2d.drawImage(backg[Level.current_lvl-1], 0, 0, null);
            for (int i = 0; i < lives; i++)
                g2d.drawImage(healthImage[i], 10 + (healthImage[i].getWidth(null) * i), 560, null);
            g2d.setColor(Color.white);
            g2d.setFont(new Font("serif", Font.BOLD, 25));
            g2d.drawString("Your Score: " + liveScore, 600, 580);
            paddle.draw(g2d);
            lvl1.draw(g2d);
            ball.draw(g2d);
            g2d.drawImage(levelCompleted,0,0,null);
            g2d.drawImage(next_btn,330,352,null);
        }
        //game last win screen
        else if(live_index == youWonScreenIndex){
            g2d.drawImage(backg[Level.current_lvl-1], 0, 0, null);
            for (int i = 0; i < lives; i++)
                g2d.drawImage(healthImage[i], 10 + (healthImage[i].getWidth(null) * i), 560, null);
            g2d.setColor(Color.white);
            g2d.setFont(new Font("serif", Font.BOLD, 25));
            g2d.drawString("Your Score: " + liveScore, 600, 580);
            paddle.draw(g2d);
            lvl1.draw(g2d);
            ball.draw(g2d);
            g2d.drawImage(youWonScreen,0,0,null);
            g2d.drawImage(home_btn,360,378,null);
        }
    }

    public void move(){
       isInversed = ball.move(isInversed);
        paddle.move();
        lvl1.move(paddle);
    }

    //this function reset the game to it's default
    public void gameSet(){
        liveScore = 0;
        lives = 3;
        Level.current_lvl = 0;
        setInitialLevelsSeq();
        lvl1 = new Level(selectedLvl[Level.current_lvl]);
        ball.setBallColor(gameMenu.selectedBall);
        gameMenu.liveName = "";
        paddle.setPaddleInitialPosition(GameWidth,GameHeight);
        ball.setBallInitialPos(paddle);
        paddle.setFixedPaddleColor(false);
        ball.ball_xv = -1;
        ball.ball_yv = -1;
        ball.setIndex(live_index);
        pauseMenuBtnsPos[0][0] = 315;
        pauseMenuBtnsPos[0][1] = 381;
        isBossAdded = false;
    }

    public void checkCollision() {
        //Death check
        if(lives <= 0){
            gameMenu.setLivePlayerScore(liveScore);
            pauseMenuBtnsPos[0][0] = 367;
            pauseMenuBtnsPos[0][1] = 381;
            live_index = gameOverIndex;
        }
        //level win check
        if(lvl1.drawnBlocks == 0){
            //check if it's the last level
            if(Level.current_lvl == addedLevels){
                //take you to boss fight
                if(!isBossAdded) {
                    lvl1.addBoss();
                    isBossAdded = true;
                }
                else{
                    //check if boss died
                    if(lvl1.Boss.getHealth()<=0){
                        live_index = youWonScreenIndex;
                    }
                }
            }
            //if you just won a regular level
            else {
                live_index = lvlCompletedScreen;
            }
        }

        //paddle with window borders
        if (paddle.paddle_xp <= 0) {
            paddle.paddle_xp = 0;
        }
        if (paddle.paddle_xp >= GameWidth - paddle.paddleImage[0].getWidth(null)) {
            paddle.paddle_xp = GameWidth - paddle.paddleImage[0].getWidth(null);
        }
        //ball Death check
        if (ball.ball_yp > GameHeight) {
            if (lives > 0) {
                lives--;
                ball.ball_speed = 0;
                ball.ball_xv = -1;
                ball.ball_yv = -1;
                paddle.setPaddleInitialPosition(GameWidth, GameHeight);
                ball.setBallInitialPos(paddle);
                ball.setIndex(live_index);
            }
        }
        //paddle with ball check
        Rectangle paddleCollider = new Rectangle(paddle.paddle_xp, paddle.paddle_yp, paddle.getImage().getWidth(null), paddle.getImage().getHeight(null));
        Rectangle ballCollider = new Rectangle(ball.ball_xp, ball.ball_yp, ball.ballImage[0].getWidth(null), ball.ballImage[0].getHeight(null));
        if (paddleCollider.intersects(ballCollider) && ballCollider.getY() <= 20+paddleCollider.getY()-ballCollider.getHeight()) {
            if(ballCollider.getX()<=paddleCollider.getX()+paddle.getImage().getWidth(null)/3) {
                if (ball.ball_xv >= 0 && !isInversed) {
                    ball.ball_xv = -1;
                    ball.ball_yv = -1;
                    isInversed = true;
                } else {
                    if(! isInversed) {
                        ball.ball_yv = -1;
                        isInversed = true;
                    }
                }
            }
            else if(ballCollider.getX()>paddleCollider.getX()+paddle.getImage().getWidth(null)/3 &&
                    ballCollider.getX()<paddleCollider.getX()+paddle.getImage().getWidth(null)*2/3 && ball.ball_speed != 0){
                if(!isInversed) {
                    ball.ball_yv = -1;
                    ball.ball_xv = 0;
                    isInversed = true;
                }
            }
            else if(ballCollider.getX()>=paddleCollider.getX()+paddle.getImage().getWidth(null)*2/3){
                if(ball.ball_xv <=0 && !isInversed) {
                    ball.ball_xv = 1;
                    ball.ball_yv = -1;
                    isInversed = true;
                }

                else {
                    if (!isInversed) {
                        ball.ball_yv = -1;
                        isInversed = true;
                    }
                }
            }
            lvl1.isBallInverted = false;
        }
    //ball with blocks
    ballCollider = new Rectangle(ball.ball_xp, ball.ball_yp, ball.ballImage[0].getWidth(null), ball.ballImage[0].getHeight(null));
    if(isBossAdded) {
        Rectangle BossCollider = new Rectangle(lvl1.Boss.px, lvl1.Boss.py, 70, 70);
    if(BossCollider.intersects(ballCollider)){
        isInversed = false;
    }
    }

    //ball with blocks
    int collisionDetection = 0;
    for(int i =0;i<lvl1.currentLvlBlock.length;i++) {
        for (int j =0;j<lvl1.currentLvlBlock[i].length;j++) {
            Rectangle blockCollider = new Rectangle(lvl1.currentLvlBlock[i][j].px,lvl1.currentLvlBlock[i][j].py,lvl1.currentLvlBlock[i][j].getBlockWidth(),lvl1.currentLvlBlock[i][j].getBlockHeight());
        if(ballCollider.intersects(blockCollider)) {
            if(lvl1.currentLvlBlock[i][j].getHealth()>0) {
                lvl1.currentLvlBlock[i][j].setHealth(lvl1.currentLvlBlock[i][j].getHealth() - 1);
                lvl1.currentLvlBlock[i][j].setBlockShape(lvl1.currentLvlBlock[i][j].getHealth());
                selectedLvl[Level.current_lvl-1].lvl[i][j] -= 1;
            }
            if(lvl1.currentLvlBlock[i][j].getHealth() == 0) {
                lvl1.drawnBlocks--;
                lvl1.setPwrupGravity(lvl1.currentLvlBlock[i][j].carriedPwrupIndex);
            }
            isInversed = false;
            if(blockCollider.getX()>=ball.ball_xp - 5 + ball.ballImage[0].getWidth(null)||
                    ball.ball_xp >= blockCollider.getX()+blockCollider.getWidth()-3){
                collisionDetection = 1;
                liveScore += 5;
            }
            else if(ballCollider.getX() >= ball.ball_xp - 11 + ball.ballImage[0].getHeight(null)
                || blockCollider.getX()<=ball.ball_xp+blockCollider.getHeight()+40){
                collisionDetection = 2;
                liveScore += 5;
            }
        }
        }
    }
    //check ball with block collision direction
    if(collisionDetection != 0){
        if(collisionDetection == 1){
         if(ball.ball_xv == 0){
              ball.ball_yv = 1;
         }
    else
        ball.ball_xv *= -1;
        }
    if(collisionDetection == 2){
        ball.ball_yv *= -1;
    }
}

    //check rest elements collision(laser,enemies and boss if created)
    lives = lvl1.checkCollision(paddle,ball,lives);

    }
    //This function have the levels sequences
    public void setInitialLevelsSeq() {
        //1 level
        int Tarr[][] ={
                {4, 0, 4, 0, 0, 4, 0, 4},
                {4, 0, 4, 0, 0, 4, 0, 4},
                {0, 5, 0, 0, 0, 0, 5, 0},
                {0, 5, 0, 0, 0, 0, 5, 0},
                {4, 0, 4, 0, 0, 4, 0, 4},
                {4, 0, 4, 0, 0, 4, 0, 4},
                {4, 0, 4, 0, 0, 4, 0, 4},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        selectedLvl[0] = new levels();
        selectedLvl[0].lvl = Tarr;
        addedLevels++;

        //level 2
        Tarr = new int[][]{
                {0,0,0,0,0,0,0,0},
                {0,3,0,0,0,0,3,0},
                {3,0,3,0,0,3,0,3},
                {0,0,3,0,0,0,0,3},
                {0,3,0,0,0,0,3,0},
                {3,0,0,0,0,3,0,0},
                {3,3,3,0,0,3,3,3},
                {0,0,0,0,0,0,0,0}

        };
        selectedLvl[1] = new levels();
        selectedLvl[1].lvl = Tarr;
        addedLevels++;

        //level 3
        Tarr = new int[][]{
                {0,0,0,0,0,0,0,0},
                {0,0,0,1,1,0,0,0},
                {0,0,1,4,4,1,0,0},
                {0,9,0,1,1,0,9,0} ,
                {9,4,9,0,0,9,4,9} ,
                {0,9,0,1,1,0,9,0} ,
                {0,0,1,4,4,1,0,0},
                {0,0,0,1,1,0,0,0}

        };
        selectedLvl[2] = new levels();
        selectedLvl[2].lvl = Tarr;
        addedLevels++;

        //level 4
        Tarr = new int[][]{
                {0,0,0,5,1,5,0,0},
                {0,0,1,5,5,5,1,0},
                {0,1,5,2,2,2,5,1},
                {0,1,5,2,2,2,5,1},
                {0,5,0,0,5,0,0,5},
                {0,0,0,0,5,0,0,0},
                {0,0,5,0,5,0,0,0},
                {0,0,0,5,0,0,0,0}

        };
        selectedLvl[3] = new levels();
        selectedLvl[3].lvl = Tarr;
        addedLevels++;

        //level 5
        Tarr = new int[][]{
                {4, 0, 0, 4, 0, 0, 0, 0},
                {4, 0, 0, 4, 0, 4, 4, 0},
                {4, 3, 3, 4, 4, 0, 0, 4},
                {4, 0, 0, 4, 4, 0, 0, 4},
                {4, 0, 0, 4, 4, 3, 3, 4},
                {0, 0, 0, 0, 4, 0, 0, 4},
                {2, 2, 2, 2, 4, 0, 0, 4},
                {0, 0, 0, 0, 4, 0, 0, 4}
        };
        selectedLvl[4] = new levels();
        selectedLvl[4].lvl = Tarr;
        addedLevels++;

        //level 6
        Tarr = new int[][] {
                {5,5,0,0,0,5,5,0},
                {0,5,5,5,5,5,0,0},
                {0,5,2,5,2,5,0,0},
                {5,5,5,5,5,5,5,0},
                {5,2,5,5,5,2,5,0},
                {5,0,2,2,2,0,5,0},
                {5,0,0,0,0,0,5,0},
                {0,5,0,0,0,5,0,0}
        };
        selectedLvl[5] = new levels();
        selectedLvl[5].lvl = Tarr;
        addedLevels++;

        //boss level
        Tarr = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        selectedLvl[6] = new levels();
        selectedLvl[6].lvl = Tarr;
        addedLevels++;
    }

    // game loop
    public void run(){
        //Game Loop
        long lastTime = System.nanoTime();
        double amountOfTicks= 60.0;
        double ns = 1000000000/amountOfTicks;
        double delta =0;
        while (true) {
            long now =System.nanoTime();
            delta +=(now-lastTime)/ns;
            lastTime=now;
            if(delta>=1){
                if(live_index == gameindex) {
                    move();
                    checkCollision();
                    lvl1.addEnemy(liveScore);
                }
                repaint();
                delta--;
            }
        }

    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(live_index == gameindex){
        if(mouseEvent.getX()>=10 && mouseEvent.getX()<=30+pause_btn[0].getWidth(null)) {
            if (mouseEvent.getY() >= 10 && mouseEvent.getY() <= 30 + pause_btn[0].getHeight(null)) {
                live_index = pauseScreenIndex;
            }
        }
        }
        else if(live_index == pauseScreenIndex){
            if (mouseEvent.getX() >= pauseMenuBtnsPos[0][0] && mouseEvent.getX() <= pauseMenuBtnsPos[0][0]+20 + home_btn.getWidth(null) &&
                    mouseEvent.getY() >= pauseMenuBtnsPos[0][1] && mouseEvent.getY() <= pauseMenuBtnsPos[0][1]+20 + home_btn.getHeight(null)) {
                live_index = menuindex;
                gameMenu.live_index = live_index;
                gameMenu.inMenuIndex = 1;
                gameSet();
            }
            if (mouseEvent.getX() >= pauseMenuBtnsPos[1][0] && mouseEvent.getX() <= pauseMenuBtnsPos[1][0]+20 + resume_btn.getWidth(null) &&
                    mouseEvent.getY() >= pauseMenuBtnsPos[1][1] && mouseEvent.getY() <= pauseMenuBtnsPos[1][1]+20 + resume_btn.getHeight(null)) {
            live_index = gameindex;
            }
        }
        else if(live_index == gameOverIndex){
            if (mouseEvent.getX() >= pauseMenuBtnsPos[0][0] && mouseEvent.getX() <= pauseMenuBtnsPos[0][0]+20 + home_btn.getWidth(null) &&
                    mouseEvent.getY() >= pauseMenuBtnsPos[0][1] && mouseEvent.getY() <= pauseMenuBtnsPos[0][1]+20 + home_btn.getHeight(null)) {
                live_index = menuindex;
                gameMenu.live_index = live_index;
                gameMenu.inMenuIndex = 1;
                gameSet();
            }
        }
        else if(live_index == lvlCompletedScreen){
            if (mouseEvent.getX() >= 330 && mouseEvent.getX() <= 350 + next_btn.getWidth(null) &&
                    mouseEvent.getY() >= 352 && mouseEvent.getY() <= 372 + next_btn.getHeight(null)) {
                paddle.setPaddleInitialPosition(GameWidth,GameHeight);
                ball.setBallInitialPos(paddle);
                ball.setIndex(gameindex);
                paddle.setFixedPaddleColor(false);
                ball.ball_xv = -1;
                ball.ball_yv = -1;
                lvl1 = new Level(selectedLvl[Level.current_lvl]);
                live_index = gameindex;
            }
            }
        else if(live_index == youWonScreenIndex){
            if (mouseEvent.getX() >= 360 && mouseEvent.getX() <= 380 + next_btn.getWidth(null) &&
                 mouseEvent.getY() >= 378 && mouseEvent.getY() <= 398 + next_btn.getHeight(null)) {
                live_index = menuindex;
                gameMenu.live_index = live_index;
                gameMenu.inMenuIndex = 1;
                gameSet();
            }
        }

    }
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        if(live_index == gameindex) {

            if (mouseEvent.getX() >= 10 && mouseEvent.getX() <= 30 + pause_btn[0].getWidth(null) &&
                    mouseEvent.getY() >= 10 && mouseEvent.getY() <= 30 + pause_btn[0].getHeight(null)) {
                animatedPause_btn = 1;
            } else {
                animatedPause_btn = 0;
            }
        }
        else if(live_index == pauseScreenIndex){
            if (mouseEvent.getX() >= pauseMenuBtnsPos[0][0] && mouseEvent.getX() <= pauseMenuBtnsPos[0][0]+20 + home_btn.getWidth(null) &&
                    mouseEvent.getY() >= pauseMenuBtnsPos[0][1] && mouseEvent.getY() <= pauseMenuBtnsPos[0][1]+20 + home_btn.getHeight(null)) {

                pauseMenuBtnsPos[0][0] = 315;
                pauseMenuBtnsPos[0][1] = 381;
            }
            else{
                pauseMenuBtnsPos[0][0] = 310;
                pauseMenuBtnsPos[0][1] = 376;
            }
            if (mouseEvent.getX() >= pauseMenuBtnsPos[1][0] && mouseEvent.getX() <= pauseMenuBtnsPos[1][0]+20 + resume_btn.getWidth(null) &&
                    mouseEvent.getY() >= pauseMenuBtnsPos[1][1] && mouseEvent.getY() <= pauseMenuBtnsPos[1][1]+20 + resume_btn.getHeight(null)) {

                pauseMenuBtnsPos[1][0] = 420;
                pauseMenuBtnsPos[1][1] = 381;
            }
            else{
                pauseMenuBtnsPos[1][0] = 415;
                pauseMenuBtnsPos[1][1] = 376;
            }
        }
        if(live_index == gameOverIndex) {
            if (mouseEvent.getX() >= pauseMenuBtnsPos[0][0] && mouseEvent.getX() <= pauseMenuBtnsPos[0][0]+20 + home_btn.getWidth(null) &&
                    mouseEvent.getY() >= pauseMenuBtnsPos[0][1] && mouseEvent.getY() <= pauseMenuBtnsPos[0][1]+20 + home_btn.getHeight(null)) {

                pauseMenuBtnsPos[0][0] = 367;
                pauseMenuBtnsPos[0][1] = 381;
            }
            else{
                pauseMenuBtnsPos[0][0] = 362;
                pauseMenuBtnsPos[0][1] = 376;
            }
        }
    }
    public class  ActionL extends KeyAdapter {
        public void keyPressed(KeyEvent e){
            paddle.setBallStatus(ball.isBallLaunched);
            paddle.keyPressed(e);
        }
        public void keyReleased(KeyEvent e){
            paddle.keyReleased(e);
        }
    }


    //unused overrides
    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void keyPressed(KeyEvent keyEvent) {}

    @Override
    public void keyReleased(KeyEvent keyEvent) {}

    @Override
    public void mousePressed(MouseEvent mouseEvent) {}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {}


}

