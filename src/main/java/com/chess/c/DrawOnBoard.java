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

    public void removeAllArrows(GridPane gridPane) {
        // Filtern und alle Lines entfernen
        gridPane.getChildren().removeIf(node -> node instanceof Line);
        gridPane.getChildren().removeIf(node -> node instanceof Polygon);
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



    public void drawArrow(String a, String b, int fieldsize, Board board, GridPane gridPane, int arrowID) {
        double x1, y1;
        double x2, y2;

        int[] firstCoordinate = board.NameToCoordinate(a);
        int[] secondCoordinate = board.NameToCoordinate(b);

        x1 = firstCoordinate[1] * fieldsize + 50;
        y1 = firstCoordinate[0] * fieldsize + 50;
        x2 = secondCoordinate[1] * fieldsize + 50;
        y2 = secondCoordinate[0] * fieldsize + 50;

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
        gridPane.getChildren().addAll(line, arrowHead);
    }
     
}
