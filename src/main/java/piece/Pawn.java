package piece;

import board.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import player.Turn;

import java.util.ArrayList;
import java.util.Objects;

public class  Pawn extends Piece {
    boolean colour;
    boolean hasMovedOnlyOnes;
    public Pawn(boolean colour, boolean hasMovedOnlyOnes){
        this.colour = colour;
        this.hasMovedOnlyOnes = hasMovedOnlyOnes;

    }

    Image WPawn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/w-pawn.png")));
    Image BPawn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pieces/b-pawn.png")));
    public Image getImage(){
        if (this.colour) {
            return WPawn;
        }else{
            return BPawn;
        }

    }
    public String draw(){
        if(colour) {
            return " WPawn ";
        }else{
            return " BPawn ";
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
        int direction = this.colour ? -1 : 1; // -1 für Weiß, +1 für Schwarz

        // Reguläre Bewegungen: Ein Feld vorwärts
        if (isInBounds(x1 + direction, y1) && Brett[x1 + direction][y1].piece == null) {
            Brett[x1 + direction][y1].isPossible = true;

            // Zwei Felder vorwärts (nur Startposition)
            if ((this.colour && x1 == 6) || (!this.colour && x1 == 1)) {
                if (Brett[x1 + 2 * direction][y1].piece == null) {
                    Brett[x1 + 2 * direction][y1].isPossible = true;
                }
            }
        }

        // Schlagen von Gegnern
        checkCaptureMoves(x1, y1, direction, Brett);

        // En passant
        if ((this.colour && x1 == 3) || (!this.colour && x1 == 4)) {
            checkEnPassant(pos1, Brett, Zuege, direction);
        }

        // Rückgabe des Bretts, kein weiteres Handeln notwendig
        return Brett;
    }

    private void checkCaptureMoves(int x1, int y1, int direction, Field[][] Brett) {
        // Links vorne
        if (isInBounds(x1 + direction, y1 - 1) && Brett[x1 + direction][y1 - 1].piece != null
                && Brett[x1 + direction][y1 - 1].piece.getColour() != this.colour) {
            Brett[x1 + direction][y1 - 1].isPossible = true;
        }

        // Rechts vorne
        if (isInBounds(x1 + direction, y1 + 1) && Brett[x1 + direction][y1 + 1].piece != null
                && Brett[x1 + direction][y1 + 1].piece.getColour() != this.colour) {
            Brett[x1 + direction][y1 + 1].isPossible = true;
        }
    }

    private void checkEnPassant(int[] pos1, Field[][] Brett, ArrayList<Turn> Zuege, int direction) {
        int x1 = pos1[0];
        int y1 = pos1[1];

        // Letzter Zug
        Turn lastTurn = Zuege.get(Zuege.size() - 1);
        Board board = new Board(new Field[8][8]);
        board.initializeFields();
        int[] startPos = board.NameToCoordinate(lastTurn.a1);
        int[] endPos = board.NameToCoordinate(lastTurn.b1);

        // Prüfen, ob En Passant möglich ist
        if (Math.abs(startPos[0] - endPos[0]) == 2) { // Letzter Zug war ein Doppelschritt
            int opponentX = endPos[0];
            int opponentY = endPos[1];

            // Links
            if (y1 > 0 && Brett[x1][y1 - 1].name.equals(Brett[opponentX][opponentY].name)) {
                Brett[x1 + direction][y1 - 1].isPossible = true;
            }
            // Rechts
            if (y1 < 7 && Brett[x1][y1 + 1].name.equals(Brett[opponentX][opponentY].name)) {
                Brett[x1 + direction][y1 + 1].isPossible = true;
            }
        }
    }

    // Utility-Methode zur Prüfung, ob Koordinaten im Brett liegen
    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public void setHasMoved() {     //sethasMovedOnlyOnes
        hasMovedOnlyOnes = !hasMovedOnlyOnes;
    }

    @Override
    public boolean hasMoved() {     //hasMovedOnlyOnes
        return hasMovedOnlyOnes;
    }
}
