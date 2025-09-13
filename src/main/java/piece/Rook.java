package piece;

import board.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import player.Turn;

import java.util.ArrayList;
import java.util.Objects;

public class Rook extends Piece {
    boolean colour;
    boolean hasMoved;
    public Rook(boolean colour, boolean hasMoved) {
        this.colour = colour;
        this.hasMoved = hasMoved;
    }

    Image WRook = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/w-rook.png")));
    Image BRook = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/b-rook.png")));
    public Image getImage(){
        if (this.colour) {
            return WRook;
        }else{
            return BRook;
        }

    }
    @Override
    public String toString() {
        if (colour) {
            return " WRook ";
        } else {
            return " BRook ";
        }
    }
    @Override
    public void setHasMoved() {
        this.hasMoved = true;
    }

    @Override
    public boolean hasMoved() {
        return this.hasMoved;
    }

    @Override
    public Piece getPiece() {
        return this;
    }

    @Override
    public boolean canMove(int[] pos1, int[] pos2, Field[][] Brett, ArrayList<Turn> Zuege) {
        int x2 = pos2[0];       // Second Coordinate
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

        checkLine(x1, y1, 0, 1, Brett);
        checkLine(x1, y1, 0, -1, Brett);
        checkLine(x1, y1, 1, 0, Brett);
        checkLine(x1, y1, -1, 0, Brett);

        return Brett;
    }

    private void checkLine(int x, int y, int dx, int dy, Field[][] Brett) {
        int currX = x + dx;
        int currY = y + dy;

        while (isInBounds(currX, currY)) {
            Field currentField = Brett[currX][currY];

            if (currentField.piece == null) {
                currentField.isPossible = true;
            } else {
                if (currentField.piece.getColour() != Brett[x][y].piece.getColour()) {
                    currentField.isPossible = true;
                }
                break;
            }

            currX += dx;
            currY += dy;
        }
    }


    @Override
    public int getID() {
        return 2;
    }
}
