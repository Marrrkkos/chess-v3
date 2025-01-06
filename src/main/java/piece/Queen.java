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

        // Rook Bewegungen (horizontal und vertikal)
        exploreLine(x1, y1, 1, 0, Brett);  // Nach unten
        exploreLine(x1, y1, -1, 0, Brett); // Nach oben
        exploreLine(x1, y1, 0, 1, Brett);  // Nach rechts
        exploreLine(x1, y1, 0, -1, Brett); // Nach links

        // Bishop Bewegungen (diagonal)
        exploreLine(x1, y1, 1, 1, Brett);  // Diagonal rechts unten
        exploreLine(x1, y1, 1, -1, Brett); // Diagonal links unten
        exploreLine(x1, y1, -1, 1, Brett); // Diagonal rechts oben
        exploreLine(x1, y1, -1, -1, Brett); // Diagonal links oben

        return Brett;
    }

    /**
     * Erkundet eine Linie in einer bestimmten Richtung, bis ein Hindernis auftritt.
     *
     * @param startX Startposition X
     * @param startY Startposition Y
     * @param dx     Richtung in X (z.B. 1 für unten, -1 für oben, 0 für keine Bewegung)
     * @param dy     Richtung in Y (z.B. 1 für rechts, -1 für links, 0 für keine Bewegung)
     * @param Brett  Das Spielfeld
     */
    private void exploreLine(int startX, int startY, int dx, int dy, Field[][] Brett) {
        int currX = startX + dx;
        int currY = startY + dy;

        while (isInBounds(currX, currY)) {
            Field currentField = Brett[currX][currY];

            // Falls das Feld ein gegnerisches Stück enthält
            if (currentField.piece != null) {
                if (currentField.piece.getColour() != Brett[startX][startY].piece.getColour()) {
                    currentField.isPossible = true;
                }
                break; // Linie endet hier
            }

            // Das Feld ist leer und daher möglich
            currentField.isPossible = true;

            // Nächste Position in der Linie
            currX += dx;
            currY += dy;
        }
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
