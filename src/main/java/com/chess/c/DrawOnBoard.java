package com.chess.c;

import board.Board;
import board.Field;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class DrawOnBoard {
    public void drawPieces(Board board, Button[][] buttonArray, int rotation) {
        Field[][] field = board.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (field[i][j].piece != null) {
                    ImageView pieceImage = new ImageView(field[i][j].piece.getImage());
                    pieceImage.setFitHeight(100);
                    pieceImage.setFitWidth(100);

                    buttonArray[i][j].setGraphic(pieceImage);
                    buttonArray[i][j].getGraphic().setRotate(rotation);
                } else {
                    buttonArray[i][j].setGraphic(null);
                }
            }
        }
    }
    public void removeAllCircles(GridPane gridPane){
        gridPane.getChildren().removeIf(node -> node instanceof Circle);
    }
    private Circle drawPoint(double i, double j){
        Circle circle = new Circle();
        circle.setCenterX(j*100+50);
        circle.setCenterY(i*100+50);
        circle.setManaged(false);
        circle.setRadius(12);
        circle.setOpacity(0.8);
        circle.setFill(Color.GRAY);
        circle.setMouseTransparent(true);

        return circle;
    }
    public void drawAllPossibleMoves(Board board, GridPane gridPane) {
        Field[][] b = board.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (b[i][j].isPossible) {
                    int[] arr = {i, j};
                    gridPane.getChildren().add(drawPoint(i,j));
                }
            }
        }
    }




     
}
