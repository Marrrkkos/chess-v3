package com.chess.c;

import board.Board;
import javafx.scene.image.ImageView;
import piece.Bishop;
import piece.Knight;
import piece.Queen;
import piece.Rook;

import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import piece.*;
import java.io.*;
import java.util.Objects;

public class Promotion {
    boolean promoted;
    public Promotion(boolean promoted){
        this.promoted = promoted;
    }
    public String writePromotion(int p){
        return switch (p) {
            case 1 -> "=Q";
            case 2 -> "=R";
            case 3 -> "=K";
            case 4 -> "=B";
            default -> "=Q";
        };
    }
    public void showPromotion(int xPosButton, int yPosButton, boolean colour, Button[][] buttonArray, int rotation){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttonArray[i][j].setOpacity(0.2);
            }
        }
        Queen queen = new Queen(colour);
        Rook rook = new Rook(colour, true);
        Bishop bishop = new Bishop(colour);
        Knight knight = new Knight(colour);

        ImageView queenView = new ImageView(queen.getImage());
        ImageView rookView = new ImageView(rook.getImage());
        ImageView bishopView = new ImageView(bishop.getImage());
        ImageView knightView = new ImageView(knight.getImage());

        if(colour) {
            buttonArray[yPosButton][xPosButton].setStyle("-fx-background-radius: 90");
            buttonArray[yPosButton + 1][xPosButton].setStyle("-fx-background-radius: 90");
            buttonArray[yPosButton + 2][xPosButton].setStyle("-fx-background-radius: 90");
            buttonArray[yPosButton + 3][xPosButton].setStyle("-fx-background-radius: 90");

            buttonArray[yPosButton][xPosButton].setOpacity(1);
            buttonArray[yPosButton + 1][xPosButton].setOpacity(1);
            buttonArray[yPosButton + 2][xPosButton].setOpacity(1);
            buttonArray[yPosButton + 3][xPosButton].setOpacity(1);

            buttonArray[yPosButton][xPosButton].setGraphic(queenView);
            buttonArray[yPosButton+1][xPosButton].setGraphic(rookView);
            buttonArray[yPosButton+2][xPosButton].setGraphic(bishopView);
            buttonArray[yPosButton+3][xPosButton].setGraphic(knightView);
            if(rotation == 180){
                buttonArray[yPosButton][xPosButton].getGraphic().setRotate(180);
                buttonArray[yPosButton + 1][xPosButton].getGraphic().setRotate(180);
                buttonArray[yPosButton + 2][xPosButton].getGraphic().setRotate(180);
                buttonArray[yPosButton + 3][xPosButton].getGraphic().setRotate(180);
            }else{
                buttonArray[yPosButton][xPosButton].getGraphic().setRotate(0);
                buttonArray[yPosButton + 1][xPosButton].getGraphic().setRotate(0);
                buttonArray[yPosButton + 2][xPosButton].getGraphic().setRotate(0);
                buttonArray[yPosButton + 3][xPosButton].getGraphic().setRotate(0);
            }
        }else{
            buttonArray[yPosButton][xPosButton].setStyle("-fx-background-radius: 90");
            buttonArray[yPosButton - 1][xPosButton].setStyle("-fx-background-radius: 90");
            buttonArray[yPosButton - 2][xPosButton].setStyle("-fx-background-radius: 90");
            buttonArray[yPosButton - 3][xPosButton].setStyle("-fx-background-radius: 90");

            buttonArray[yPosButton][xPosButton].setOpacity(1);
            buttonArray[yPosButton - 1][xPosButton].setOpacity(1);
            buttonArray[yPosButton - 2][xPosButton].setOpacity(1);
            buttonArray[yPosButton - 3][xPosButton].setOpacity(1);

            buttonArray[yPosButton][xPosButton].setGraphic(queenView);
            buttonArray[yPosButton-1][xPosButton].setGraphic(rookView);
            buttonArray[yPosButton-2][xPosButton].setGraphic(bishopView);
            buttonArray[yPosButton-3][xPosButton].setGraphic(knightView);
            if(rotation == 180){
                buttonArray[yPosButton][xPosButton].getGraphic().setRotate(180);
                buttonArray[yPosButton - 1][xPosButton].getGraphic().setRotate(180);
                buttonArray[yPosButton - 2][xPosButton].getGraphic().setRotate(180);
                buttonArray[yPosButton - 3][xPosButton].getGraphic().setRotate(180);
            }else{
                buttonArray[yPosButton][xPosButton].getGraphic().setRotate(0);
                buttonArray[yPosButton - 1][xPosButton].getGraphic().setRotate(0);
                buttonArray[yPosButton - 2][xPosButton].getGraphic().setRotate(0);
                buttonArray[yPosButton - 3][xPosButton].getGraphic().setRotate(0);
            }
        }


        promoted = true;
    }
    public int doPromotion(Button button, Button[][] buttonArray, int xPosButton, int yPosButton, boolean colour, Board board, String m1, String m2){
        int x = 0;
        if(colour) {
            if (Objects.equals(button, buttonArray[yPosButton][xPosButton])) {
                x = 1;
            }else if (Objects.equals(button, buttonArray[yPosButton + 1][xPosButton])) {
                x = 2;
            }else if (Objects.equals(button, buttonArray[yPosButton + 2][xPosButton])) {
                x = 4;
            }else if (Objects.equals(button, buttonArray[yPosButton + 3][xPosButton])) {
                x = 3;
            }else{
                promoted = false;
            }
            buttonArray[yPosButton][xPosButton].setStyle("-fx-background-radius: 0");
            buttonArray[yPosButton + 1][xPosButton].setStyle("-fx-background-radius: 0");
            buttonArray[yPosButton + 2][xPosButton].setStyle("-fx-background-radius: 0");
            buttonArray[yPosButton + 3][xPosButton].setStyle("-fx-background-radius: 0");


        }else {
            if (Objects.equals(button, buttonArray[yPosButton][xPosButton])) {
                x = 1;
            }else if (Objects.equals(button, buttonArray[yPosButton - 1][xPosButton])) {
                x = 2;
            }else if (Objects.equals(button, buttonArray[yPosButton - 2][xPosButton])) {
                x = 4;
            }else if (Objects.equals(button, buttonArray[yPosButton - 3][xPosButton])) {
                x = 3;
            }else{
                promoted = false;
            }
            buttonArray[yPosButton][xPosButton].setStyle("-fx-background-radius: 0");
            buttonArray[yPosButton - 1][xPosButton].setStyle("-fx-background-radius: 0");
            buttonArray[yPosButton - 2][xPosButton].setStyle("-fx-background-radius: 0");
            buttonArray[yPosButton - 3][xPosButton].setStyle("-fx-background-radius: 0");

        }
        if(promoted) {
            board.promotion(x, m1, m2);
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttonArray[i][j].setOpacity(1);
            }
        }

        return x;
    }
    public void setPromotion(boolean setPromotion) {
        this.promoted = setPromotion;

    }
}
