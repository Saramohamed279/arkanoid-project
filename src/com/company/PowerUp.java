package com.company;

import java.awt.*;

public class PowerUp {
    protected Image powerupImage;
    protected int pwrupIndex;
    protected int px,py;
    private int gravity = 0;
    PowerUp(Image powerupImage,int pwrupIndex,int px,int py){
        this.powerupImage = powerupImage;
        this.pwrupIndex = pwrupIndex;
        setPowerupInitialPos(px,py);
    }
    public void setPowerupInitialPos(int px,int py){
        this.px = px + 28 - 11;
        this.py = py;
    }
    public void setGravity(int gravity){this.gravity = gravity;}
    public int getGravity(){return gravity;}
    public void move(){
        py += gravity;
    }
}
