package piece;
import board.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import player.Turn;

import java.util.ArrayList;

public abstract class Piece {
    public abstract Image getImage();
    public abstract String draw();
    public abstract Piece getPiece();
    public abstract boolean canMove(int[]pos1, int[]pos2, Field[][] Brett, ArrayList<Turn> Zuege);
    public abstract boolean getColour();
    public abstract Field[][] possibleFields(int[] pos1, Field[][] Brett, ArrayList<Turn> Zuege);
    public abstract int getID();

    public abstract void setHasMoved();
    public abstract boolean hasMoved();


}
