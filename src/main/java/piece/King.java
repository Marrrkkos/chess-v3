package piece;

import board.*;
import com.chess.c.chessBoardController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import player.Turn;

import java.util.ArrayList;
import java.util.Objects;


public class  King extends Piece {
    boolean colour;
    boolean hasMoved;
    public King(boolean colour, boolean hasMoved){
        this.colour = colour;
        this.hasMoved = hasMoved;
    }

    Image WKing = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/w-king.png")));
    Image BKing = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/b-king.png")));
    public Image getImage(){
        if (this.colour) {
            return WKing;
        }else{
            return BKing;
        }

    }
    @Override
    public String toString(){
        if(colour) {
            return " WKing ";
        }else{
            return " BKing ";
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
    public Field[][] possibleFields(int[] pos1, Field[][] board, ArrayList<Turn> moves) {
        int x1 = pos1[0];
        int y1 = pos1[1];
        boolean isWhite = board[x1][y1].piece.getColour(); // true = Weiß

        // all directions
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, { 0, -1}, { 0, 1}, { 1, -1}, { 1, 0}, { 1, 1}};

        for (int[] dir : directions) {
            int dirX = x1 + dir[0];
            int dirY = y1 + dir[1];

            if (isInBounds(dirX, dirY)) {
                Piece targetPiece = board[dirX][dirY].piece;

                if (targetPiece == null || targetPiece.getColour() != isWhite) {
                    board[dirX][dirY].isPossible = true;
                }
            }
        }

        if (!this.hasMoved) {
            checkCastle(pos1, board);
        }

        return board;
    }
    private boolean checkCastle(int[] pos1, Field[][] Brett){
        int x1 = pos1[0];       // First Coordinate
        int y1 = pos1[1];
        //White
        if (Brett[x1][y1].piece.getColour()) {
            // Short Castle
            if(Brett[7][7].piece != null && Brett[7][7].piece.getID() == 2) {
                if (!Brett[7][7].piece.hasMoved()) {
                    if ((Brett[7][5].piece == null && Brett[7][6].piece == null)) {
                        Brett[7][6].isPossible = true;
                    }
                }
            }
            // Long Castle
            if(Brett[7][0].piece != null && Brett[7][0].piece.getID() == 2) {
                if (!Brett[7][0].piece.hasMoved()) {
                    if ((Brett[7][3].piece == null && Brett[7][2].piece == null && Brett[7][1].piece == null)) {
                        Brett[7][2].isPossible = true;
                    }
                }
            }
        }
        //Black
        if(!Brett[x1][y1].piece.getColour()) {
            // Short Castle
            if(Brett[0][7].piece != null && Brett[0][7].piece.getID() == 2) {
                if (!Brett[0][7].piece.hasMoved()) {
                    if ((Brett[0][5].piece == null && Brett[0][6].piece == null)) {
                        Brett[0][6].isPossible = true;
                    }
                }
            }
            // Long Castle
            if(Brett[0][0].piece != null && Brett[0][0].piece.getID() == 2) {
                if (!Brett[0][0].piece.hasMoved()) {
                    if ((Brett[0][3].piece == null && Brett[0][2].piece == null && Brett[0][1].piece == null)) {
                        Brett[0][2].isPossible = true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    public int getID() {
        return 6;
    }

    @Override
    public void setHasMoved(){
        this.hasMoved = true;
    }

    @Override
    public boolean hasMoved() {
        return this.hasMoved;
    }
}
