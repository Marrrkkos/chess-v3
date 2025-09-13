package piece;

import board.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import player.Turn;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;

import static javafx.scene.transform.Transform.scale;


public class  Bishop extends Piece {
    boolean colour;

    public Bishop(boolean colour) {
        this.colour = colour;
    }


    Image WBishop = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/w-bishop.png")));
    Image BBishop = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/b-bishop.png")));
    public Image getImage(){
        if (this.colour) {
            return WBishop;
        }else{
            return BBishop;
        }

    }
    @Override
    public String toString() {
        if (colour) {
            return "WBishop";
        } else {
            return "BBishop";
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
        return Brett[x2][y2].isPossible;
    }

    @Override
    public boolean getColour() {
        return this.colour;
    }
    @Override
    public Field[][] possibleFields(int[] pos1, Field[][] Brett, ArrayList<Turn> Zuege) {
        int x1 = pos1[0];
        int y1 = pos1[1];

        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        // Iteriere über alle Richtungen
        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];
            int count = 1;

            while (true) {
                int newX = x1 + dx * count;
                int newY = y1 + dy * count;

                if (!isInBounds(newX, newY)) {
                    break;
                }

                Field targetField = Brett[newX][newY];

                if (targetField.piece != null) {

                    if (targetField.piece.getColour() != Brett[x1][y1].piece.getColour()) {
                        targetField.isPossible = true;
                    }
                    break;
                }

                targetField.isPossible = true;
                count++;
            }
        }

        return Brett;
    }

    @Override
    public int getID() {
        return 4;
    }
    @Override
    public void  setHasMoved() {

    }

    @Override
    public boolean hasMoved() {
        return false;
    }
}
