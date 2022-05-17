package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class ScoreBoard {
    private ArrayList<Player> players = new ArrayList<Player>();
    private Image settingBG = new ImageIcon("settings_BG.jpg").getImage();
ScoreBoard(){
    try{
    File myObj = new File("gameData.txt");
    Scanner myReader = new Scanner(myObj);
    while (myReader.hasNextLine()) {
        Player player = new Player();
        String nam = myReader.nextLine();
        player.name = nam;
        String data = myReader.nextLine();
        player.score=Integer.parseInt(data);
players.add(player);
    }
        myReader.close();

    Collections.sort(players, Player.playerComparator);

} catch (FileNotFoundException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }

}
public void resetBoard(Player player){
    players.add(player);
    Collections.sort(players, Player.playerComparator);
    savingScore(player);
}
    public void savingScore(Player player){
        //Creating text file if it's not created
        try {
            File myObj = new File("gameData.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //saving the name and score in the file
        try{
            FileWriter scoreSaving = new FileWriter("gameData.txt",true);
            player.name = player.name.concat("\n");
            scoreSaving.write(player.name);
            scoreSaving.write(String.valueOf(player.score).concat("\n"));
            scoreSaving.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g) {
        g.drawImage(settingBG,0,0,null);
        Font font = new Font("SansSerif", Font.PLAIN, 36);
        g.setFont(font);
        g.setColor(Color.gray);
        g.drawString("Name",180,190);
        g.drawString("Score",500,190);
        g.setColor(Color.white);
        for(int i=0;i<5;i++){
            g.drawString(players.get(i).name,200,250+50*i);
            g.drawString(String.valueOf(players.get(i).score),520,250+50*i);
        }
    }
}
class Player {
    protected int score;
    protected String name;
    public static Comparator<Player> playerComparator = new Comparator<Player>() {

        @Override
        public int compare(Player p1, Player p2) {
            return p2.score-p1.score;
        }

        };
}