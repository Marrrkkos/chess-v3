package board;

import com.chess.c.Sound;
import piece.Piece;

public class NotationHandler {
    String m1;
    String m2;
    Piece piece1;
    Piece piece2;
    boolean checkiChecks;
    Sound sound = new Sound();
    public NotationHandler(String m1, String m2, Piece piece1, Piece piece2, boolean checkiChecks){
        this.m1 = m1;
        this.m2 = m2;
        this.piece1 = piece1;
        this.piece2 = piece2;
        this.checkiChecks = checkiChecks;
    }
    public String handleNotation() {
        String firstPiece = "";
        String x = "";
        String secondPlace = "";
        String check = "";

        //first part
        if (piece1 != null) {
            if (piece1.getID() != 1) {     //Pawn
                firstPiece = piece1.toString().substring(1, 2);
            }
        }

        //second Part (capture)
        if (piece2 != null) {
            x = "x";
        }


        //Third Part
        secondPlace = m2;

        //Fourth Part
        if(checkiChecks){
            check = "+";
        }

        //enPasant
        if(piece1.getID() == 1){
            if(!m1.substring(0,1).equals(m2.substring(0,1))){
                x = "x";
            }
        }

        //Castle
        if (piece1 != null) {
                //Short Castle
            if (((m1.equals("e1") && m2.equals("g1")) || (m1.equals("e8") && m2.equals("g8"))) && piece1.getID() == 6) {
                setCastlingSound();
                return "0-0";
                //Long Castle
            } else if (((m1.equals("e1") && m2.equals("c1")) || (m1.equals("e8") && m2.equals("c8"))) && piece1.getID() == 6) {
                setCastlingSound();
                return "0-0-0";
            }
        }
        setSounds(x,check);
        return firstPiece + "" + x + "" + secondPlace + "" + check;
    }
    public void setAll(String m1, String m2, Piece piece1, Piece piece2){
        this.m1 = m1;
        this.m2 = m2;
        this.piece1 = piece1;
        this.piece2 = piece2;
    }
    public void setCheckiChecks(boolean checkiChecks){
        this.checkiChecks = checkiChecks;
    }
    public void clear(){
        this.m1 = null;
        this.m2 = null;
        this.piece1 = null;
        this.piece2 = null;
        this.checkiChecks = false;
    }
    public void addPromotion(String p){
        this.m2 = m2 + "" + p;
    }
    private void setSounds(String x, String check){
        if(check.equals("+")){
            sound.setFile(2);
        }else if(x.equals("x")){
            sound.setFile(0);
        }else{
            sound.setFile(5);
        }
        sound.play();
    }
    private void setCastlingSound(){
        sound.setFile(1);
        sound.play();
    }
}
