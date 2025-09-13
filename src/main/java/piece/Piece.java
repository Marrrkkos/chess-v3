package piece;
import board.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import player.Turn;

import java.util.ArrayList;

/**
 * This class represents the main functionality of the pieces
 * IMPORTANT: It gives only basic functionality of the pieces, so it's unfiltered from checks
 */
public abstract class Piece {
    public abstract Image getImage();
    @Override
    public abstract String toString();
    public abstract Piece getPiece();
    /** Checks if this move, got by the player is possible
     * @param Zuege important, because you have to check last move for EnPassant-Rule (Has to be a double Pawn Move)
     */
    public abstract boolean canMove(int[]pos1, int[]pos2, Field[][] Brett, ArrayList<Turn> Zuege);
    public abstract boolean getColour();

    /**
     * Gets all possible Fields of this piece, from which this method is called from
     * IMPORTANT: Unfiltered from checks
     *
     * @param Zuege important, because you have to check last move for EnPassant-Rule (Has to be a double Pawn Move)
     * @return a 8x8 field of booleans, where all fields which are possible are true
     */
    public abstract Field[][] possibleFields(int[] pos1, Field[][] Brett, ArrayList<Turn> Zuege);
    public abstract int getID();

    public abstract void setHasMoved();
    public abstract boolean hasMoved();
    /**
     * Help Function to make sure everything is in 8x8 (ChessBoard) Bounds
     */
    boolean isInBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }


}
