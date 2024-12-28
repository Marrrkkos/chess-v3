package board;

import piece.Pawn;
import piece.Piece;

public class Field {
    public Piece piece;
    public String name;
    public boolean isPossible;
    public Field(String name, Piece piece, boolean isPossible){
        this.piece = piece;
        this.name = name;
        this.isPossible = isPossible;
    }
    public Field(String name){
        this.name = name;
    }
    public static void copy(Field[][] a, Field[][] b){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //b[i][j] = new Field("clone",new Pawn(false, false), false);
                b[i][j] = new Field(a[i][j].name, a[i][j].piece, false);

                b[i][j].piece = a[i][j].piece;
                b[i][j].isPossible = a[i][j].isPossible;
                b[i][j].name = a[i][j].name;
            }
        }
    }
    public static void clearPossible(Field[][] a){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                a[i][j].isPossible = false;
            }
        }
    }
}
