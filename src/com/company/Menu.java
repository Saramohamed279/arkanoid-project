package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Menu implements MouseListener, MouseMotionListener, KeyListener {
    Music music = new Music("Arkanoid Music.wav");

    private final int homeScreenIndex = 1;
    private final int nameScreenIndex = 2;
    private final int scoreBoardScreenIndex = 3;
    private final int settingsScreenIndex = 4;

    ScoreBoard scoreBoard = new ScoreBoard();

    Player livePlayer = new Player();

    protected  int live_index;
    public int inMenuIndex = 1;
    private int btn_positions[][] = {
            {366,451},   //0.start button
            {666, 113},  //1.setting button
            {666, 240},  //2.score button
            {666, 490},  //3.exit button
            {345,390},   //4.next button
            {10,10},     //5.back button
            {666,370}    //6.sound button
    };
    private int[][] settingsBtns_positions = {
            {180, 280}, // prv button 1 postion for ball
            {180, 480}, // prv button 2 postion  for paddle
            {530, 280},  // nxt button 1 postion for ball
            {530,480}   // nxt button 2 postion for paddle
    };
    private Image Start_img;
    private Image Setting_btn;
    private Image BackGround , settingBG;
    private Image Score_btn;
    private Image sound_btn[] = new Image[2];
    private int soundStatus = 0;
    private Image Exit_btn;
    private Image Next_btn;
    private Image Back_btn;
    private Image prv_btns[] = new Image[2];
    private Image nxt_btns[] = new Image[2] ;
    private Image ballImage[] = new Image[4];
    private Image paddleImage[] = new Image[4];
    protected int selectedBall = 0;
    protected int selectedPaddle = 0;

    private  Ball ball;
    private Paddle paddle;

    protected String liveName = "";
    protected StringBuffer s = new StringBuffer(liveName);
    public Menu(int live_index,Ball ball,Paddle paddle){
        this.live_index = live_index;
        this.ball = ball;
        this.paddle = paddle;
        Start_img   = new ImageIcon("Start_button.png").getImage();
        Setting_btn = new ImageIcon("s_button.png").getImage();
        BackGround  = new ImageIcon("layer_1__semi_final.png").getImage();
        settingBG   = new ImageIcon("settings_BG.jpg").getImage();
        Score_btn   = new ImageIcon("Score_button2.png").getImage();
        Exit_btn    = new ImageIcon("exit_Button.png").getImage();
        Next_btn    = new ImageIcon("next_button.png").getImage();
        Back_btn    = new ImageIcon("back_Button.png").getImage();
        for(int i=0;i<2;i++){
            prv_btns[i] = new ImageIcon("back_Button.png").getImage();
            nxt_btns[i] = new ImageIcon("nxt button.png").getImage();
        }
        ballImage[0] = new ImageIcon("BlueBall.png").getImage();
        ballImage[1] = new ImageIcon("GreenBall.png").getImage();
        ballImage[2] = new ImageIcon("PinkBall.png").getImage();
        ballImage[3] = new ImageIcon("OrangeBall.png").getImage();

        paddleImage[0] = new ImageIcon("bluePaddle.png").getImage();
        paddleImage[1] = new ImageIcon("greenPaddle.png").getImage();
        paddleImage[2] = new ImageIcon("orangePaddle.png").getImage();
        paddleImage[3] = new ImageIcon("redPaddle.png").getImage();

        sound_btn[0] = new ImageIcon("music_Button.png").getImage();
        sound_btn[1] = new ImageIcon("mute_Button.png").getImage();
    }

    public void draw(Graphics2D g){
        //Main menu home
        if(inMenuIndex == homeScreenIndex) {
            g.drawImage(BackGround, 0, 0, null);
            g.drawImage(Start_img, btn_positions[0][0], btn_positions[0][1], null);
            g.drawImage(Setting_btn, btn_positions[1][0], btn_positions[1][1], null);
            g.drawImage(Score_btn, btn_positions[2][0], btn_positions[2][1], null);
            g.drawImage(sound_btn[soundStatus],btn_positions[6][0], btn_positions[6][1],null);
            g.drawImage(Exit_btn, btn_positions[3][0], btn_positions[3][1], null);
        }
        //Name screen
        else if(inMenuIndex == nameScreenIndex){
            g.drawImage(settingBG,0,0,null);
            Font font = new Font("SansSerif", Font.PLAIN, 36);
            g.setFont(font);
            g.setColor(Color.white);
            g.drawString("Enter Your Name: ",260,250);
            g.setColor(Color.black);
            g.drawRoundRect(210, 290, 400, 50, 20, 20);
            g.setColor(Color.white);
            g.drawString(liveName,230,330);
            g.drawImage(Back_btn,btn_positions[5][0], btn_positions[5][1],null);
            g.drawImage(Next_btn,btn_positions[4][0], btn_positions[4][1],null);
        }
        //Score board
        else if(inMenuIndex == scoreBoardScreenIndex){
            scoreBoard.draw(g);
            g.drawImage(Back_btn,btn_positions[5][0], btn_positions[5][1],null);
        }
        //Settings
        else if(inMenuIndex == settingsScreenIndex){
            g.drawImage(settingBG,0,0,null);
            g.setColor(Color.white);
            g.setFont(new Font("MV Poli", Font.BOLD, 60));
            g.drawString("Settings",283,210);
            g.drawImage(Back_btn,btn_positions[5][0], btn_positions[5][1],null);
            g.drawImage(prv_btns[0],settingsBtns_positions[0][0], settingsBtns_positions[0][1],null);
            g.drawImage(prv_btns[1],settingsBtns_positions[1][0], settingsBtns_positions[1][1],null);
            g.drawImage(nxt_btns[0],settingsBtns_positions[2][0], settingsBtns_positions[2][1],null);
            g.drawImage(nxt_btns[1],settingsBtns_positions[3][0], settingsBtns_positions[3][1],null);
            g.drawImage(ballImage[selectedBall],380,280,50,50,null);
            g.drawImage(paddleImage[selectedPaddle],340,480,null);
        }
    }
    public void setLivePlayerScore(int score){
        livePlayer.score = score;
        scoreBoard.resetBoard(livePlayer);
    }

    public void setBallImage(int ballImageIndex){
        ball.setBallColor(ballImageIndex);
    }

    public void setPaddleImage(int paddleImageIndex){paddle.setPaddleColor(paddleImageIndex);}

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
//Home screen
if(inMenuIndex == homeScreenIndex) {
            if (mouseEvent.getX() < Start_img.getWidth(null) + 366 && mouseEvent.getX() > 366) {
                if (mouseEvent.getY() < Start_img.getHeight(null) + 481 && mouseEvent.getY() > 451) {
                    inMenuIndex = 2;
                }
            }
            if (mouseEvent.getX() < Score_btn.getWidth(null) + 666 && mouseEvent.getX() > 666) {
                if (mouseEvent.getY() < Score_btn.getHeight(null) + 270 && mouseEvent.getY() > 240) {
                    inMenuIndex = 3;
                }
            }
            if (mouseEvent.getX() < Setting_btn.getWidth(null) + 666 && mouseEvent.getX() > 666) {
                if (mouseEvent.getY() < Setting_btn.getHeight(null) + 143 && mouseEvent.getY() > 113) {
                    inMenuIndex = settingsScreenIndex;
                }

            }
            if (mouseEvent.getX() < sound_btn[0].getWidth(null) + 666 && mouseEvent.getX() > 666) {
                if (mouseEvent.getY() < sound_btn[0].getHeight(null) + 390 && mouseEvent.getY() > 370) {
                    if(soundStatus == 0) {
                        soundStatus = 1;
                        music.pause();
                    }
                    else if(soundStatus == 1) {
                        soundStatus = 0;
                        music.resume();
                    }
                }
            }
           if (mouseEvent.getX() < Exit_btn.getWidth(null) + 666 && mouseEvent.getX() > 666) {
              if (mouseEvent.getY() < Exit_btn.getHeight(null) + 510 && mouseEvent.getY() > 490) {
              System.exit(0);
              }
          }
        }
//Name screen
else if(inMenuIndex == nameScreenIndex){
         if(mouseEvent.getX()>=345 && mouseEvent.getX()<=345+Next_btn.getWidth(null)) {
            if (mouseEvent.getY() >= 390 && mouseEvent.getY() <= 390 + Next_btn.getHeight(null)) {
              if(liveName.length()>0) {
            livePlayer.name = liveName;
            setBallImage(selectedBall);
            setPaddleImage(selectedPaddle);
            live_index = 1;
              }
            }
         }
    if (mouseEvent.getX() >= 10 && mouseEvent.getX() <= 30 + Back_btn.getWidth(null) &&
            mouseEvent.getY() >= 10 && mouseEvent.getY() <= 30 + Back_btn.getHeight(null))
           {
               inMenuIndex = 1;
           }
}
//Score Board
else if(inMenuIndex == scoreBoardScreenIndex){
     if (mouseEvent.getX() >= 10 && mouseEvent.getX() <= 30 + Back_btn.getWidth(null) &&
          mouseEvent.getY() >= 10 && mouseEvent.getY() <= 30 + Back_btn.getHeight(null)) {
                inMenuIndex = 1;
     }
}
//Settings
else if(inMenuIndex == settingsScreenIndex){
            if (mouseEvent.getX() >= 10 && mouseEvent.getX() <= 30 + Back_btn.getWidth(null) &&
                    mouseEvent.getY() >= 10 && mouseEvent.getY() <= 30 + Back_btn.getHeight(null)) {
                inMenuIndex = 1;
            }
            if (mouseEvent.getX() >= 180 && mouseEvent.getX() <= 200 + prv_btns[0].getWidth(null) &&
                    mouseEvent.getY() >= 280 && mouseEvent.getY() <= 300 + prv_btns[0].getHeight(null)){
                if(selectedBall>0){
                    selectedBall--;
                }
            }
            if (mouseEvent.getX() >= 530 && mouseEvent.getX() <= 550 + nxt_btns[0].getWidth(null) &&
                    mouseEvent.getY() >= 280 && mouseEvent.getY() <= 300 + nxt_btns[0].getHeight(null)) {
               if(selectedBall<3){
                selectedBall++;
               }
            }
            if (mouseEvent.getX() >= 180 && mouseEvent.getX() <= 200 + prv_btns[1].getWidth(null) &&
                    mouseEvent.getY() >= 480 && mouseEvent.getY() <= 500 + prv_btns[1].getHeight(null)){
                if(selectedPaddle>0){
                    selectedPaddle--;
                }
            }
            if (mouseEvent.getX() >= 530 && mouseEvent.getX() <= 550 + nxt_btns[1].getWidth(null) &&
                    mouseEvent.getY() >= 480 && mouseEvent.getY() <= 500 + nxt_btns[1].getHeight(null)) {
                if(selectedPaddle<3){
                    selectedPaddle++;
                }
            }
        }
    }
    //Animation
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        //Home screen buttons' animation
        if (inMenuIndex == homeScreenIndex) {
            if (mouseEvent.getX() < Start_img.getWidth(null) + 366 && mouseEvent.getX() > 366) {
                if (mouseEvent.getY() < Start_img.getHeight(null) + 481 && mouseEvent.getY() > 451) {
                    btn_positions[0][0] = 376;
                    btn_positions[0][1] = 461;
                }
            } else {
                btn_positions[0][0] = 376 - 10;
                btn_positions[0][1] = 461 - 10;
            }
            if (mouseEvent.getX() < Score_btn.getWidth(null) + 666 && mouseEvent.getX() > 666 &&
                    mouseEvent.getY() < Score_btn.getHeight(null) + 143 && mouseEvent.getY() > 123) {
                btn_positions[1][0] = 676;
                btn_positions[1][1] = 123;

            } else {
                btn_positions[1][0] = 676 - 10;
                btn_positions[1][1] = 123 - 10;
            }
            if (mouseEvent.getX() < Setting_btn.getWidth(null) + 666 && mouseEvent.getX() > 666 &&
                    mouseEvent.getY() < Setting_btn.getHeight(null) + 260 && mouseEvent.getY() > 240) {
                btn_positions[2][0] = 676;
                btn_positions[2][1] = 250;

            } else {
                btn_positions[2][0] = 676 - 10;
                btn_positions[2][1] = 250 - 10;
            }
            if (mouseEvent.getX() < sound_btn[soundStatus].getWidth(null) + 666 && mouseEvent.getX() > 666 &&
                    mouseEvent.getY() < sound_btn[soundStatus].getHeight(null) + 390 && mouseEvent.getY() > 370) {
                btn_positions[6][0] = 676;
                btn_positions[6][1] = 380;
            } else {
                btn_positions[6][0] = 676 - 10;
                btn_positions[6][1] = 380 - 10;
            }
            if (mouseEvent.getX() < Exit_btn.getWidth(null) + 666 && mouseEvent.getX() > 666 &&
                    mouseEvent.getY() < Exit_btn.getHeight(null) + 510 && mouseEvent.getY() > 490) {
                btn_positions[3][0] = 676;
                btn_positions[3][1] = 500;
            } else {
                btn_positions[3][0] = 676 - 10;
                btn_positions[3][1] = 500 - 10;
            }

        }
        //name screen buttons' animation
        else if (inMenuIndex == nameScreenIndex) {
            if (mouseEvent.getX() >= 345 && mouseEvent.getX() <= 365 + Next_btn.getWidth(null) &&
                mouseEvent.getY() >= 390 && mouseEvent.getY() <= 410 + Next_btn.getHeight(null)) {
                    btn_positions[4][0] = 355;
                    btn_positions[4][1] = 400;

            }
            else{
                btn_positions[4][0] = 355-10;
                btn_positions[4][1] = 400-10;
            }
            if (mouseEvent.getX() >= 10 && mouseEvent.getX() <= 30 + Back_btn.getWidth(null) &&
                    mouseEvent.getY() >= 10 && mouseEvent.getY() <= 30 + Back_btn.getHeight(null)) {
                btn_positions[5][0] = 15;
                btn_positions[5][1] = 15;

            }
            else{
                btn_positions[5][0] = 10;
                btn_positions[5][1] = 10;
            }
        }
        //Score Board buttons' animation
        else if(inMenuIndex == scoreBoardScreenIndex){
            if (mouseEvent.getX() >= 10 && mouseEvent.getX() <= 30 + Back_btn.getWidth(null) &&
                    mouseEvent.getY() >= 10 && mouseEvent.getY() <= 30 + Back_btn.getHeight(null)) {
                btn_positions[5][0] = 15;
                btn_positions[5][1] = 15;

            }
            else{
                btn_positions[5][0] = 10;
                btn_positions[5][1] = 10;
            }
        }
        //Settings buttons' animation
        else if(inMenuIndex == settingsScreenIndex){
            if (mouseEvent.getX() >= 10 && mouseEvent.getX() <= 30 + Back_btn.getWidth(null) &&
                    mouseEvent.getY() >= 10 && mouseEvent.getY() <= 30 + Back_btn.getHeight(null)) {
                btn_positions[5][0] = 15;
                btn_positions[5][1] = 15;

            }
            else{
                btn_positions[5][0] = 10;
                btn_positions[5][1] = 10;
            }
            if (mouseEvent.getX() >= 180 && mouseEvent.getX() <= 200 + prv_btns[0].getWidth(null) &&
                    mouseEvent.getY() >= 280 && mouseEvent.getY() <= 300 + prv_btns[0].getHeight(null)) {
                settingsBtns_positions[0][0] = 185;
                settingsBtns_positions[0][1] = 285;

            }
            else{
                settingsBtns_positions[0][0] = 180;
                settingsBtns_positions[0][1] = 280;
            }
            if (mouseEvent.getX() >= 180 && mouseEvent.getX() <= 200 + prv_btns[1].getWidth(null) &&
                    mouseEvent.getY() >= 480 && mouseEvent.getY() <= 500 + prv_btns[1].getHeight(null)) {
                settingsBtns_positions[1][0] = 185;
                settingsBtns_positions[1][1] = 485;

            }
            else{
                settingsBtns_positions[1][0] = 180;
                settingsBtns_positions[1][1] = 480;
            }
            if (mouseEvent.getX() >= 530 && mouseEvent.getX() <= 550 + nxt_btns[0].getWidth(null) &&
                    mouseEvent.getY() >= 280 && mouseEvent.getY() <= 300 + nxt_btns[0].getHeight(null)) {
                settingsBtns_positions[2][0] = 535;
                settingsBtns_positions[2][1] = 285;

            }
            else{
                settingsBtns_positions[2][0] = 530;
                settingsBtns_positions[2][1] = 280;
            }
            if (mouseEvent.getX() >= 530 && mouseEvent.getX() <= 550 + prv_btns[1].getWidth(null) &&
                    mouseEvent.getY() >= 480 && mouseEvent.getY() <= 500 + prv_btns[1].getHeight(null)) {
                settingsBtns_positions[3][0] = 535;
                settingsBtns_positions[3][1] = 485;

            }
            else{
                settingsBtns_positions[3][0] = 530;
                settingsBtns_positions[3][1] = 480;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        //backspace detect , '\u0008' is back space char
        if(keyEvent.getKeyChar()=='\u0008') {
            if (liveName.length() > 0) {
                s.delete(0, s.length());
                s.append(liveName);
                s.deleteCharAt(s.length() - 1);
                liveName = s.toString();
            }
        }
        else
            if(liveName.length()<=10)
                 liveName += keyEvent.getKeyChar();

    }
    //unused over rides
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {}
    @Override
    public void keyPressed(KeyEvent keyEvent) {}
    @Override
    public void keyReleased(KeyEvent keyEvent) {}
}