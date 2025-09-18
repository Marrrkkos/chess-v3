package com.chess.c;

import javafx.scene.control.ListView;
import player.Player;

public class MovesView {
    String moveView = "";

    public void manageMoveView(String a, int count, boolean playerColour, ListView<String> movesView){
        if(playerColour){
            movesView.getItems().add(count/2, (count + 2) / 2 + ".  " + a);
            moveView = (a);
        }else{
            movesView.getItems().add(count/2,(count + 2) / 2 + ".  " + moveView + " | " + a);
            movesView.getItems().removeLast();
        }
    }
    public void resetMoveView(ListView<String> movesView){
        movesView.getItems().clear();
    }
}
