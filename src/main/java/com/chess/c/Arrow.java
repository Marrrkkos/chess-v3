package com.chess.c;

import board.Board;
import board.Field;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import player.MoveTree;
import player.Turn;

import java.util.ArrayList;

public class Arrow
{
    Board board = new Board(new Field[8][8]);
    public void drawArrow(String a, String b, int fieldsize, GridPane gridPane, int arrowID) {
        board.initializeBoard();

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

    public void removeAllArrows(GridPane gridPane) {
        // Filtern und alle Lines entfernen
        gridPane.getChildren().removeIf(node -> node instanceof Line);
        gridPane.getChildren().removeIf(node -> node instanceof Polygon);
    }
    public void repaintArrows(GridPane gridPane, MoveTree moveTree, ArrayList<Turn> Zuege, int FIELDSIZE){
        ArrayList<Turn> nextMoves = moveTree.getCurrentChildren(Zuege);
        removeAllArrows(gridPane);
        drawAllArrows(gridPane, nextMoves, FIELDSIZE);

    }
    public void drawAllArrows(GridPane gridPane, ArrayList<Turn> Zuege, int FIELDSIZE){
        int arrowID = 0;
        for(Turn turn: Zuege){
            if(turn.b1.contains("=")) {
                drawArrow(turn.a1, turn.b1.substring(0, 2),FIELDSIZE, gridPane, arrowID);
            }else{
                drawArrow(turn.a1, turn.b1, FIELDSIZE, gridPane, arrowID);
            }
            arrowID++;
        }
        arrowID = 0;
    }
}
