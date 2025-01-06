package com.chess.c;

import java.net.URL;
import javax.sound.sampled.*;
public class Sound {
    Clip clip;
    URL soundURL[] = new URL[255];
    public Sound(){
        soundURL[0] = getClass().getResource("/sounds/capture.wav");
        soundURL[1] = getClass().getResource("/sounds/castle.wav");
        soundURL[2] = getClass().getResource("/sounds/check.wav");
        soundURL[3] = getClass().getResource("/sounds/correct.wav");
        soundURL[4] = getClass().getResource("/sounds/illegal.wav");
        soundURL[5] = getClass().getResource("/sounds/move.wav");
        soundURL[6] = getClass().getResource("/sounds/select.wav");
    }
    public void setFile(int i){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }catch (Exception e){

        }
    }
    public void play(){
        clip.start();
    }
}
