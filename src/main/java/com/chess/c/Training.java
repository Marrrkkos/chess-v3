package com.chess.c;

import javafx.scene.control.Button;

public class Training {


    public void setTrainingOnVisual(Button[] trainingButtons){
        for(Button button1 : trainingButtons){
            button1.setDisable(true);
        }
    }
    public void setTrainingOffVisual(Button[] trainingButtons){
        for(Button button1 : trainingButtons){
            button1.setDisable(false);
        }
    }
}
