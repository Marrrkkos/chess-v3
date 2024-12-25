package piece;

import board.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import player.Turn;

import java.util.ArrayList;
import java.util.Objects;

public class Knight extends Piece{
    boolean colour;
    public Knight(boolean colour){

        this.colour = colour;
    }

    public String draw(){
        if(colour) {
            return "WKnight";
        }else{
            return "BKnight";
        }
    }
    Image WKnight = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/w-knight.png")));
    Image BKnight = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/b-knight.png")));
    public Image getImage(){
        if (this.colour) {
            return WKnight;
        }else{
            return BKnight;
        }

    }
    @Override
    public Piece getPiece() {
        return this;
    }

    @Override
    public boolean canMove(int[] pos1, int[] pos2, Field[][] Brett, ArrayList<Turn> Zuege) {
        int x2 = pos2[0];
        int y2 = pos2[1];
        possibleFields(pos1, Brett, Zuege);
        return Brett[x2][y2].isPossible;}

    @Override
    public boolean getColour() {
        return this.colour;
    }
    @Override
    public Field[][] possibleFields(int[] pos1, Field[][] Brett, ArrayList<Turn> Zuege) {
        int x1 = pos1[0];
        int y1 = pos1[1];

        // Alle möglichen Bewegungen des Springers
        int[][] moves = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        // Iteriere über alle möglichen Bewegungen
        for (int[] move : moves) {
            int newX = x1 + move[0];
            int newY = y1 + move[1];

            if (isInBounds(newX, newY)) { // Überprüfe, ob die neue Position innerhalb des Bretts liegt
                Field targetField = Brett[newX][newY];

                // Setze das Feld als möglich, wenn es leer ist oder eine gegnerische Figur enthält
                if (targetField.piece == null || targetField.piece.getColour() != Brett[x1][y1].piece.getColour()) {
                    targetField.isPossible = true;
                }
            }
        }

        return Brett;
    }

    /**
     * Prüft, ob die Koordinaten innerhalb der Brettgrenzen liegen.
     *
     * @param x Die X-Koordinate
     * @param y Die Y-Koordinate
     * @return true, wenn die Koordinaten gültig sind
     */
    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
    @Override
    public int getID() {
        return 3;
    }
    @Override
    public void  setHasMoved() {

    }

    @Override
    public boolean hasMoved() {
        return false;
    }
}
