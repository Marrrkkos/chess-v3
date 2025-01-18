package com.chess.c;

import board.Board;
import board.Field;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class DrawOnBoard {
    public void drawPieces(Board board, Button[][] buttonArray, int rotation) {
        Field[][] field = board.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (field[i][j].piece != null) {
                    ImageView ik = new ImageView(field[i][j].piece.getImage());
                    ik.setFitHeight(100);
                    ik.setFitWidth(100);

                    buttonArray[i][j].setGraphic(ik);
                    buttonArray[i][j].getGraphic().setRotate(rotation);
                } else {
                    buttonArray[i][j].setGraphic(null);
                }
            }
        }
    }
    int arrowID = 0;
    public void drawArrow(String a, String b, int FIELDSIZE, Board board, GridPane gridPane) {
        double x1, y1;
        double x2, y2;

        int[] firstCoordinate = board.NameToCoordinate(a);
        int[] secondCoordinate = board.NameToCoordinate(b);

        x1 = firstCoordinate[1] * FIELDSIZE + 50;
        y1 = firstCoordinate[0] * FIELDSIZE + 50;
        x2 = secondCoordinate[1] * FIELDSIZE + 50;
        y2 = secondCoordinate[0] * FIELDSIZE + 50;

        Line line = new Line();


        double dx = x2 - x1;
        double dy = y2 - y1;
        double angle = Math.atan2(dy, dx);

        double arrowLength = 45;
        double arrowWidth = 65;

        double adjustedEndX = x2 - arrowLength * Math.cos(angle)*1.22;
        double adjustedEndY = y2 - arrowLength * Math.sin(angle)*1.22;

        double m1 = x2 - arrowLength * Math.cos(angle) + arrowWidth / 2 * Math.sin(angle);
        double n1 = y2 - arrowLength * Math.sin(angle) - arrowWidth / 2 * Math.cos(angle);

        double m2 = x2 - arrowLength * Math.cos(angle) - arrowWidth / 2 * Math.sin(angle);
        double n2 = y2 - arrowLength * Math.sin(angle) + arrowWidth / 2 * Math.cos(angle);

        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(adjustedEndX);
        line.setEndY(adjustedEndY);
        line.setMouseTransparent(true);
        line.setStroke(Color.GREEN);
        line.setStrokeWidth(20);
        line.setManaged(false);
        line.setOpacity(0.3);
        line.setId(arrowID + "");
        Polygon arrowHead = new Polygon();
        arrowHead.getPoints().addAll(
                x2, y2,
                m1, n1,
                m2, n2
        );
        arrowHead.setId(arrowID + "");
        arrowHead.setFill(Color.GREEN);
        arrowHead.setMouseTransparent(true);
        arrowHead.setManaged(false);
        arrowHead.setOpacity(0.3);

        arrowID++;
        gridPane.getChildren().addAll(line, arrowHead);
    }
     
}
