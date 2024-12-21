package piece;

import board.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import player.Turn;

import java.util.ArrayList;

public class Rook extends Piece {
    boolean colour;
    boolean hasMoved;
    public Rook(boolean colour, boolean hasMoved) {
        this.colour = colour;
        this.hasMoved = hasMoved;
    }

    //TODO
    Image WRook = new Image(getClass().getResourceAsStream("/pieces/w-rook.png"));
    Image BRook = new Image(getClass().getResourceAsStream("/pieces/b-rook.png"));
    public Image getImage(){
        if (this.colour) {
            return WRook;
        }else{
            return BRook;
        }

    }
    public String draw() {
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

        // Überprüft vertikale und horizontale Bewegungen
        checkLine(x1, y1, 0, 1, Brett);  // Nach rechts
        checkLine(x1, y1, 0, -1, Brett); // Nach links
        checkLine(x1, y1, 1, 0, Brett);  // Nach unten
        checkLine(x1, y1, -1, 0, Brett); // Nach oben

        return Brett;
    }

    /**
     * Überprüft eine Linie auf mögliche Felder basierend auf der Richtung.
     *
     * @param x       Startposition X
     * @param y       Startposition Y
     * @param dx      Richtung in X (0 für horizontal)
     * @param dy      Richtung in Y (0 für vertikal)
     * @param Brett   Das Spielfeld
     */
    private void checkLine(int x, int y, int dx, int dy, Field[][] Brett) {
        int currX = x + dx;
        int currY = y + dy;

        // Solange die Koordinaten innerhalb der Brettgrenzen sind
        while (isInBounds(currX, currY)) {
            Field currentField = Brett[currX][currY];

            // Prüfen, ob das Feld leer ist
            if (currentField.piece == null) {
                currentField.isPossible = true;
            } else {
                // Prüfen, ob ein gegnerisches Stück geschlagen werden kann
                if (currentField.piece.getColour() != Brett[x][y].piece.getColour()) {
                    currentField.isPossible = true;
                }
                break; // Blockierte Linie
            }

            // Nächste Position in der Linie
            currX += dx;
            currY += dy;
        }
    }

    /**
     * Prüft, ob die Koordinaten innerhalb des Bretts liegen.
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
        return 2;
    }
}
