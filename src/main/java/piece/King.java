package piece;

import board.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import player.Turn;

import java.util.ArrayList;


public class  King extends Piece {
    boolean colour;
    boolean hasMoved;
    public King(boolean colour, boolean hasMoved){
        this.colour = colour;
        this.hasMoved = hasMoved;
    }

    //TODO
    Image WKing = new Image(getClass().getResourceAsStream("/pieces/w-king.png"));
    Image BKing = new Image(getClass().getResourceAsStream("/pieces/b-king.png"));
    public Image getImage(){
        if (this.colour) {
            return WKing;
        }else{
            return BKing;
        }

    }
    public String draw(){
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
    public Field[][] possibleFields(int[] pos1, Field[][] Brett, ArrayList<Turn> Zuege) {
        int x1 = pos1[0]; // First Coordinate
        int y1 = pos1[1];
        boolean isWhite = Brett[x1][y1].piece.getColour();

        // Alle möglichen Bewegungen eines Königs
        int[][] moves = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},          {0, 1},
                {1, -1}, {1, 0},  {1, 1}
        };

        // Überprüfe alle möglichen Züge
        for (int[] move : moves) {
            int newX = x1 + move[0];
            int newY = y1 + move[1];

            if (isInBounds(newX, newY)) {
                Field targetField = Brett[newX][newY];
                // Setze das Feld als möglich, wenn es leer ist oder eine gegnerische Figur enthält
                if (targetField.piece == null || targetField.piece.getColour() != isWhite) {
                    targetField.isPossible = true;
                }
            }
        }

        // Überprüfe Rochade, wenn der König sich noch nicht bewegt hat
        if (!Brett[x1][y1].piece.hasMoved()) {
            checkCastle(pos1, Brett);
        }

        return Brett;
    }

    /**
     * Überprüft die Möglichkeit einer Rochade und setzt die entsprechenden Felder auf "möglich".
     *
     * @param pos1 Die Position des Königs
     * @param Brett Das Schachbrett
     */
    private void checkCastle(int[] pos1, Field[][] Brett) {
        int x1 = pos1[0];
        int y1 = pos1[1];
        boolean isWhite = Brett[x1][y1].piece.getColour();

        // Kurze Rochade
        if (canCastle(isWhite, true, Brett)) {
            Brett[x1][y1 + 2].isPossible = true;
        }

        // Lange Rochade
        if (canCastle(isWhite, false, Brett)) {
            Brett[x1][y1 - 2].isPossible = true;
        }
    }

    /**
     * Überprüft, ob eine Rochade möglich ist.
     *
     * @param isWhite Ob der Spieler Weiß ist
     * @param isShort Ob es sich um eine kurze Rochade handelt
     * @param Brett Das Schachbrett
     * @return true, wenn die Rochade möglich ist
     */
    private boolean canCastle(boolean isWhite, boolean isShort, Field[][] Brett) {
        int row = isWhite ? 7 : 0;
        int rookCol = isShort ? 7 : 0;
        int kingCol = 4;

        // Überprüfe, ob die beteiligten Figuren vorhanden sind und sich nicht bewegt haben
        Field rookField = Brett[row][rookCol];
        Field kingField = Brett[row][kingCol];
        if (rookField.piece == null || kingField.piece == null) return false;
        if (rookField.piece.getID() != 2 || rookField.piece.hasMoved()) return false;
        if (kingField.piece.hasMoved()) return false;

        // Überprüfe, ob die Felder zwischen König und Turm frei sind
        int startCol = Math.min(kingCol, rookCol) + 1;
        int endCol = Math.max(kingCol, rookCol) - 1;

        for (int col = startCol; col <= endCol; col++) {
            if (Brett[row][col].piece != null) {
                return false;
            }
        }

        // (Optional) Hier könntest du zusätzliche Regeln wie das Überprüfen, ob der König durch Schach zieht, implementieren

        return true;
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
