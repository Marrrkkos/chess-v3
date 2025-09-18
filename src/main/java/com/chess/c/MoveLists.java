package com.chess.c;

import board.Board;
import board.NotationHandler;
import javafx.scene.control.ListView;
import player.MoveTree;
import player.Turn;

import java.util.ArrayList;

public class MoveLists {
    Board board;
    NotationHandler notationHandler;

    public void resetMoveOptions(ListView<String> openingsView, MoveTree moveTree, ArrayList<Turn> Zuege){
        ArrayList<Turn> nextMoves = moveTree.getCurrentChildren(Zuege);

        while(!openingsView.getItems().isEmpty()) {
            openingsView.getItems().removeLast();
        }
        for(Turn turn: nextMoves) {
            notationHandler.setAll(turn.a1, turn.b1, board.getPiece(turn.a1), board.getPiece(turn.b1));
            openingsView.getItems().add(notationHandler.handleNotation());
        }
    }
}
