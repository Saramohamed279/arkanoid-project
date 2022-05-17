package com.company;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public abstract class Audio {
    Clip clip;
    //upload audio
    Audio(String path){
        try {
            File musicpath = new File(path);

                AudioInputStream audioinput = AudioSystem.getAudioInputStream(musicpath);
                clip = AudioSystem.getClip();
                clip.open(audioinput);
                clip.start();
                clip.loop(clip.LOOP_CONTINUOUSLY);

        }

        catch (Exception ex) {
        System.out.println ("cant find file ");
            ex.printStackTrace();

        }
    }
    public abstract void pause();
    public abstract void resume();
}

class Music extends Audio{
    Music(String path) {
        super(path);
    }
    public void pause(){clip.stop();}
    public void resume(){
        clip.start();
        clip.loop(clip.LOOP_CONTINUOUSLY);
    }
}
