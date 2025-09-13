package piece;

import board.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import player.Turn;

import java.util.ArrayList;
import java.util.Objects;

public class  Queen extends Piece {
    boolean colour;
    public Queen(boolean colour){
        this.colour = colour;
    }

    Image WQueen = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/w-queen.png")));
    Image BQueen = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/b-queen.png")));
    public Image getImage(){
        if (this.colour) {
            return WQueen;
        }else{
            return BQueen;
        }

    }
    @Override
    public String toString(){
        if(colour) {
            return "WQueen ";
        }else{
            return "BQueen ";
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

        // Rook
        exploreLine(x1, y1, 1, 0, Brett);
        exploreLine(x1, y1, -1, 0, Brett);
        exploreLine(x1, y1, 0, 1, Brett);
        exploreLine(x1, y1, 0, -1, Brett);

        // Bishop
        exploreLine(x1, y1, 1, 1, Brett);
        exploreLine(x1, y1, 1, -1, Brett);
        exploreLine(x1, y1, -1, 1, Brett);
        exploreLine(x1, y1, -1, -1, Brett);

        return Brett;
    }

    private void exploreLine(int startX, int startY, int dirX, int dirY, Field[][] Brett) {
        int currX = startX + dirX;
        int currY = startY + dirY;

        while (isInBounds(currX, currY)) {
            Field currentField = Brett[currX][currY];

            if (currentField.piece != null) {
                if (currentField.piece.getColour() != Brett[startX][startY].piece.getColour()) {        // Cant capture own colour
                    currentField.isPossible = true;
                }
                break;
            }

            currentField.isPossible = true;

            currX += dirX;
            currY += dirY;
        }
    }

    @Override
    public int getID() {
        return 5;
    }
    @Override
    public void  setHasMoved() {

    }

    @Override
    public boolean hasMoved() {
        return false;
    }
}
