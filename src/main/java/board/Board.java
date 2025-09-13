package board;

import player.Turn;
import piece.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static board.Field.*;

public class Board{
    Field[][] Brett;

    public Board(Field[][] field){
        this.Brett = field;
    }

    public void initializeBoard(){
        initializeFields();
        initializePieces();
    }
    public Field[][] getBoard(){
        return Brett;
    }
    public void resetPieces(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Brett[i][j].piece = null;
            }
        }
        initializePieces();
    }
    public void doSimpleMove(String a, String b, boolean colour, ArrayList<Turn> Zuege, int zuegePos){
        int[] pos1 = NameToCoordinate(a);
        int[] pos2 = NameToCoordinate(b);

        Piece piece = Brett[pos1[0]][pos1[1]].piece.getPiece();

        if(b.contains("=")){
            String c = b.substring(0,2);
            int[] pos3 = NameToCoordinate(c);
            char promotedPiece = b.charAt(3);

            if(promotedPiece == 'Q'){

                Brett[pos3[0]][pos3[1]].piece = new Queen(colour);
                Brett[pos1[0]][pos1[1]].piece = null;

            }else if(promotedPiece == 'R'){

                Brett[pos3[0]][pos3[1]].piece = new Rook(colour, false);
                Brett[pos1[0]][pos1[1]].piece = null;

            }else if(promotedPiece == 'B'){

                Brett[pos3[0]][pos3[1]].piece = new Bishop(colour);
                Brett[pos1[0]][pos1[1]].piece = null;

            }else if(promotedPiece == 'K'){

                Brett[pos3[0]][pos3[1]].piece = new Knight(colour);
                Brett[pos1[0]][pos1[1]].piece = null;
            }

        }else {
            if (piece.getID() == 1 && (pos1[0] == 3 || pos1[0] == 4)) {
                int x1 = pos1[0];       // First Coordinate
                int y1 = pos1[1];

                int x2 = pos2[0];       // First Coordinate
                int y2 = pos2[1];


                String t = Zuege.get(Zuege.get(zuegePos).getID()).a1;
                String s = Zuege.get(Zuege.get(zuegePos).getID()).b1;

                int[] arr = NameToCoordinate(t);
                int[] arr1 = NameToCoordinate(s);

                int r1 = arr[0];
                int s1 = arr[1];

                int r2 = arr1[0];
                int s2 = arr[1];


                if (piece.getColour()) {
                    if (y1 == 0) {
                        if (((Brett[x1][y1 + 1].name.equals(Brett[r2][s2].name)) && (Brett[x1 - 2][y1 + 1].name.equals(Brett[r1][s1].name))) && Brett[y1 + 1][x1 - 1].name.equals(Brett[y2][x2].name)) {
                            Brett[x1 - 1][y1 + 1].piece = Brett[x1][y1].piece;
                            Brett[x1][y1 + 1].piece = null;
                        }
                    } else if (y1 == 7) {
                        if (((Brett[x1][y1 - 1].name.equals(Brett[r2][s2].name)) && (Brett[x1 - 2][y1 - 1].name.equals(Brett[r1][s1].name))) && Brett[y1 - 1][x1 - 1].name.equals(Brett[y2][x2].name)) {
                            Brett[x1 - 1][y1 - 1].piece = Brett[x1][y1].piece;
                            Brett[x1][y1 - 1].piece = null;
                        }
                    } else {
                        if (((Brett[x1][y1 - 1].name.equals(Brett[r2][s2].name)) && (Brett[x1 - 2][y1 - 1].name.equals(Brett[r1][s1].name))) && Brett[y1 - 1][x1 - 1].name.equals(Brett[y2][x2].name)) {
                            Brett[x1 - 1][y1 - 1].piece = Brett[x1][y1].piece;
                            Brett[x1][y1 - 1].piece = null;
                        } else if ((Brett[x1][y1 + 1].name.equals(Brett[r2][s2].name)) && (Brett[x1 - 2][y1 + 1].name.equals(Brett[r1][s1].name)) && Brett[y1 + 1][x1 - 1].name.equals(Brett[y2][x2].name)) {
                            Brett[x1 - 1][y1 + 1].piece = Brett[x1][y1].piece;
                            Brett[x1][y1 + 1].piece = null;
                        }
                    }
                } else {
                    if (y1 == 0) {
                        if (((Brett[x1][y1 + 1].name.equals(Brett[r2][s2].name)) && (Brett[x1 + 2][y1 + 1].name.equals(Brett[r1][s1].name))) && Brett[y1 + 1][x1 + 1].name.equals(Brett[y2][x2].name)) {
                            Brett[x1 + 1][y1 + 1].piece = Brett[x1][y1].piece;
                            Brett[x1][y1 + 1].piece = null;
                        }
                    } else if (y1 == 7) {
                        if (((Brett[x1][y1 - 1].name.equals(Brett[r2][s2].name)) && (Brett[x1 + 2][y1 - 1].name.equals(Brett[r1][s1].name))) && Brett[y1 - 1][x1 + 1].name.equals(Brett[y2][x2].name)) {
                            Brett[x1 + 1][y1 - 1].piece = Brett[x1][y1].piece;
                            Brett[x1][y1 - 1].piece = null;
                        }
                    } else {
                        if (((Brett[x1][y1 - 1].name.equals(Brett[r2][s2].name)) && (Brett[x1 + 2][y1 - 1].name.equals(Brett[r1][s1].name))) && Brett[y1 - 1][x1 + 1].name.equals(Brett[y2][x2].name)) {
                            Brett[x1 + 1][y1 - 1].piece = Brett[x1][y1].piece;
                            Brett[x1][y1 - 1].piece = null;
                        } else if (((Brett[x1][y1 + 1].name.equals(Brett[r2][s2].name)) && (Brett[x1 + 2][y1 + 1].name.equals(Brett[r1][s1].name))) && Brett[y1 + 1][x1 + 1].name.equals(Brett[y2][x2].name)) {
                            Brett[x1 + 1][y1 + 1].piece = Brett[x1][y1].piece;
                            Brett[x1][y1 + 1].piece = null;
                        }
                    }
                }
            }
            if (((a.equals("e1") && b.equals("g1")) || (a.equals("e8") && b.equals("g8"))) && getPiece(a).getID() == 6) {
                Brett[pos2[0]][pos2[1]].piece = Brett[pos1[0]][pos1[1]].piece;
                Brett[pos1[0]][pos1[1]].piece = null;

                Brett[pos1[0]][pos1[1] + 1].piece = Brett[pos1[0]][pos1[1] + 3].piece;
                Brett[pos1[0]][pos1[1] + 3].piece = null;

                //Long Castle
            } else if (((a.equals("e1") && b.equals("c1")) || (a.equals("e8") && b.equals("c8"))) && getPiece(a).getID() == 6) {
                Brett[pos2[0]][pos2[1]].piece = Brett[pos1[0]][pos1[1]].piece;
                Brett[pos1[0]][pos1[1]].piece = null;

                Brett[pos1[0]][pos1[1] - 1].piece = Brett[pos1[0]][pos1[1] - 4].piece;
                Brett[pos1[0]][pos1[1] - 4].piece = null;

            } else {
                Brett[pos2[0]][pos2[1]].piece = Brett[pos1[0]][pos1[1]].piece;
                Brett[pos1[0]][pos1[1]].piece = null;
            }
        }
    }
    public boolean doMove(String a, String b, ArrayList<Turn> Zuege){
        int[] pos1 = NameToCoordinate(a);
        int[] pos2 = NameToCoordinate(b);

        if(!Brett[pos2[0]][pos2[1]].isPossible){
            return false;
        }else{
            Piece piece = Brett[pos1[0]][pos1[1]].piece.getPiece();
            if(piece.canMove(pos1,pos2,Brett, Zuege)){

                doEnpasant(piece,pos1,pos2,Zuege);

                if(doCastle(a, b, pos1, pos2)){
                    Brett[pos2[0]][pos2[1]].piece = Brett[pos1[0]][pos1[1]].piece;
                    Brett[pos1[0]][pos1[1]].piece = null;
                }


            }

        }

        Brett[pos2[0]][pos2[1]].piece.setHasMoved();

        return true;
    }
    public boolean doCastle(String a, String b, int[] pos1, int[] pos2){
        if (((a.equals("e1") && b.equals("g1")) || (a.equals("e8") && b.equals("g8"))) && getPiece(a).getID() == 6) {
            Brett[pos2[0]][pos2[1]].piece = Brett[pos1[0]][pos1[1]].piece;
            Brett[pos1[0]][pos1[1]].piece = null;

            Brett[pos1[0]][pos1[1] + 1].piece = Brett[pos1[0]][pos1[1] + 3].piece;
            Brett[pos1[0]][pos1[1] + 3].piece = null;
            return false;
            //Long Castle
        } else if (((a.equals("e1") && b.equals("c1")) || (a.equals("e8") && b.equals("c8"))) && getPiece(a).getID() == 6) {
            Brett[pos2[0]][pos2[1]].piece = Brett[pos1[0]][pos1[1]].piece;
            Brett[pos1[0]][pos1[1]].piece = null;

            Brett[pos1[0]][pos1[1] - 1].piece = Brett[pos1[0]][pos1[1] - 4].piece;
            Brett[pos1[0]][pos1[1] - 4].piece = null;
            return false;
        }
        return true;
    }
    public void doEnpasant(Piece piece,int[] pos1, int[] pos2, ArrayList<Turn> Zuege){
        if(piece.getID() == 1 && (pos1[0] == 3 || pos1[0] == 4)){
            int x1 = pos1[0];       // First Coordinate
            int y1 = pos1[1];

            int x2 = pos2[0];       // First Coordinate
            int y2 = pos2[1];



            String t = Zuege.get(Zuege.getLast().getID()).a1;
            String s = Zuege.get(Zuege.getLast().getID()).b1;

            int[] arr = NameToCoordinate(t);
            int[] arr1 = NameToCoordinate(s);

            int r1 = arr[0];
            int s1 = arr[1];

            int r2 = arr1[0];
            int s2 = arr[1];


            if(piece.getColour()) {
                if(y1 == 0) {
                    if (((Brett[x1][y1 + 1].name.equals(Brett[r2][s2].name)) && (Brett[x1 - 2][y1 + 1].name.equals(Brett[r1][s1].name))) && Brett[y1 + 1][x1-1].name.equals(Brett[y2][x2].name)) {
                        Brett[x1 - 1][y1 + 1].piece = Brett[x1][y1].piece;
                        Brett[x1][y1+1].piece = null;
                    }
                }else if(y1 == 7){
                    if (((Brett[x1][y1 - 1].name.equals(Brett[r2][s2].name)) && (Brett[x1 - 2][y1 - 1].name.equals(Brett[r1][s1].name))) && Brett[y1 - 1][x1 - 1].name.equals(Brett[y2][x2].name)) {
                        Brett[x1 - 1][y1 - 1].piece = Brett[x1][y1].piece;
                        Brett[x1][y1-1].piece = null;
                    }
                }else {
                    if (((Brett[x1][y1 - 1].name.equals(Brett[r2][s2].name)) && (Brett[x1 - 2][y1 - 1].name.equals(Brett[r1][s1].name))) && Brett[y1-1][x1-1].name.equals(Brett[y2][x2].name)) {
                        Brett[x1 - 1][y1 - 1].piece = Brett[x1][y1].piece;
                        Brett[x1][y1-1].piece = null;
                    }else if((Brett[x1][y1 + 1].name.equals(Brett[r2][s2].name)) && (Brett[x1 - 2][y1 + 1].name.equals(Brett[r1][s1].name)) && Brett[y1+1][x1-1].name.equals(Brett[y2][x2].name)){
                        Brett[x1 - 1][y1 + 1].piece = Brett[x1][y1].piece;
                        Brett[x1][y1+1].piece = null;
                    }
                }
            }else{
                if(y1 == 0) {
                    if (((Brett[x1][y1 + 1].name.equals(Brett[r2][s2].name)) && (Brett[x1 + 2][y1 + 1].name.equals(Brett[r1][s1].name))) && Brett[y1+1][x1+1].name.equals(Brett[y2][x2].name)) {
                        Brett[x1 + 1][y1 + 1].piece = Brett[x1][y1].piece;
                        Brett[x1][y1+1].piece = null;
                    }
                }else if(y1 == 7){
                    if (((Brett[x1][y1 - 1].name.equals(Brett[r2][s2].name)) && (Brett[x1 + 2][y1 - 1].name.equals(Brett[r1][s1].name))) && Brett[y1-1][x1 + 1].name.equals(Brett[y2][x2].name)) {
                        Brett[x1 + 1][y1 - 1].piece = Brett[x1][y1].piece;
                        Brett[x1][y1-1].piece = null;
                    }
                }else {
                    if (((Brett[x1][y1 - 1].name.equals(Brett[r2][s2].name)) && (Brett[x1 + 2][y1 - 1].name.equals(Brett[r1][s1].name))) && Brett[y1-1][x1 + 1].name.equals(Brett[y2][x2].name)) {
                        Brett[x1 + 1][y1 - 1].piece = Brett[x1][y1].piece;
                        Brett[x1][y1-1].piece = null;
                    }else if(((Brett[x1][y1 + 1].name.equals(Brett[r2][s2].name)) && (Brett[x1 + 2][y1 + 1].name.equals(Brett[r1][s1].name))) && Brett[y1+1][x1 + 1].name.equals(Brett[y2][x2].name)){
                        Brett[x1 + 1][y1 + 1].piece = Brett[x1][y1].piece;
                        Brett[x1][y1+1].piece = null;
                    }
                }
            }
        }
    }
    public Boolean checkPromotion(String a, String b) {
        int[] pos1 = NameToCoordinate(a);
        int[] pos2 = NameToCoordinate(b);
        if(!Brett[pos2[0]][pos2[1]].isPossible){
            return false;
        }
        if(Brett[pos1[0]][pos1[1]].piece == null){
            return false;
        }
        if(pos1[0] == 1 && Brett[pos1[0]][pos1[1]].piece.getID() == 1 && Brett[pos1[0]][pos1[1]].piece.getColour() && pos2[0] == 0){
            return true;
        }
        if(pos1[0] == 6 && Brett[pos1[0]][pos1[1]].piece.getID() == 1 && !Brett[pos1[0]][pos1[1]].piece.getColour() && pos2[0] == 7){
            return true;
        }
        return false;
    }
    public int promotion(int x, String a, String b){
        int[] pos1 = NameToCoordinate(a);
        int[] pos2 = NameToCoordinate(b);
        if(Brett[pos1[0]][pos1[1]].piece.getColour()){
            if(x==2){
                Brett[pos2[0]][pos2[1]].piece = new Rook(true, true);
            }else if(x==3){
                Brett[pos2[0]][pos2[1]].piece = new Knight(true);
            }else if(x==4){
                Brett[pos2[0]][pos2[1]].piece = new Bishop(true);
            }else if(x==5){
                Brett[pos2[0]][pos2[1]].piece = new Queen(true);
            }else{
                Brett[pos2[0]][pos2[1]].piece = new Queen(true);
            }
            Brett[pos1[0]][pos1[1]].piece = null;
        }else if(!Brett[pos1[0]][pos1[1]].piece.getColour()){
            if(x==2){
                Brett[pos2[0]][pos2[1]].piece = new Rook(false, true);
            }else if(x==3){
                Brett[pos2[0]][pos2[1]].piece = new Knight(false);
            }else if(x==4){
                Brett[pos2[0]][pos2[1]].piece = new Bishop(false);
            }else if(x==5){
                Brett[pos2[0]][pos2[1]].piece = new Queen(false);
            }else{
                Brett[pos2[0]][pos2[1]].piece = new Queen(false);
            }
            Brett[pos1[0]][pos1[1]].piece = null;
        }
        return x;
    }

    public void clearBoard(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Brett[i][j].isPossible = false;
            }
        }
    }
    public int[] NameToCoordinate(String a){                 // Brett[arr[1]][arr[0]]: position des Feldes
        int[] arr = new int[2];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(a.equals(Brett[i][j].name)){
                    arr[1] = i;
                    arr[0] = j;
                }
            }
        }
        return arr;
    }
    private void initializePieces(){
        Brett[0][0].piece = new Rook(false, false);  //Rooks
        Brett[0][7].piece = new Rook(false, false);
        Brett[7][0].piece = new Rook(true, false);
        Brett[7][7].piece = new Rook(true, false);

        Brett[0][1].piece = new Knight(false);  //Knights
        Brett[0][6].piece = new Knight(false);
        Brett[7][6].piece = new Knight(true);
        Brett[7][1].piece = new Knight(true);

        Brett[0][2].piece = new Bishop(false);  //Bishop
        Brett[0][5].piece = new Bishop(false);
        Brett[7][5].piece = new Bishop(true);
        Brett[7][2].piece = new Bishop(true);

        Brett[0][3].piece = new Queen(false);  //Queens
        Brett[7][3].piece = new Queen(true);

        Brett[0][4].piece = new King(false, false);  //Kings
        Brett[7][4].piece = new King(true, false);

        for (int i = 0; i < 8; i++) {               //Pawns
            Brett[1][i].piece = new Pawn(false, false);
        }
        for (int i = 0; i < 8; i++) {
            Brett[6][i].piece = new Pawn(true, false);
        }
    }
    public void initializeFields(){
        int k = 8;
        for (int i = 0; i < 8; i++) {
            Brett[0][i] = new Field("a" + (k));
            Brett[1][i] = new Field("b" + (k));
            Brett[2][i] = new Field("c" + (k));
            Brett[3][i] = new Field("d" + (k));
            Brett[4][i] = new Field("e" + (k));
            Brett[5][i] = new Field("f" + (k));
            Brett[6][i] = new Field("g" + (k));
            Brett[7][i] = new Field("h" + (k));
            k--;
        }
    }
    public boolean checkCheckmate(ArrayList<Turn> Zuege, boolean PlayerColour, Board b){
        clearPossible(Brett);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(Brett[j][i].piece != null && Brett[j][i].piece.getColour() == PlayerColour) {

                    b.getPossible(Brett[i][j].name, Zuege);
                    if(b.Possibles()){
                        clearPossible(Brett);
                        return true;
                    }
                }
            }
        }
        clearPossible(Brett);
        return false;
    }
    public Board getPossible(String a, ArrayList<Turn> Zuege){

        Field[][] BrettCopy = new Field[8][8];

        copy(Brett, BrettCopy);
        int[] pos1 = NameToCoordinate(a);
        if(BrettCopy[pos1[0]][pos1[1]].piece == null) {
            return new Board(new Field[8][8]);
        }else{

            Piece piece = BrettCopy[pos1[0]][pos1[1]].piece.getPiece();
            clearPossible(Brett);


            Field[][] field = piece.possibleFields(pos1, Brett, Zuege);

            //remove Castling while in check
            if(piece.getID() == 6 && !piece.hasMoved()){
                if(piece.getColour()){
                    if(!checkiChecks(Zuege, BrettCopy, piece.getColour())) {
                        field[7][6].isPossible = false;
                        field[7][2].isPossible = false;
                    }
                }else{
                    if(!checkiChecks(Zuege, BrettCopy, piece.getColour())) {
                        field[0][6].isPossible = false;
                        field[0][2].isPossible = false;
                    }
                }
            }

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {

                    if(field[i][j].isPossible){
                        //remove enPasant when in check
                        if(piece.getID() == 1 && pos1[1] != j && field[i][j].piece == null){
                            if (piece.getColour()){
                                BrettCopy[i+1][j].piece = null;
                            }else{
                                BrettCopy[i-1][j].piece = null;
                            }


                        }


                        BrettCopy[i][j].piece = BrettCopy[pos1[0]][pos1[1]].piece;
                        BrettCopy[pos1[0]][pos1[1]].piece = null;

                        clearPossible(BrettCopy);
                        if(!checkiChecks(Zuege, BrettCopy, BrettCopy[i][j].piece.getColour())) {
                            field[i][j].isPossible = false;
                        }

                    }
                    copy(Brett, BrettCopy);


                }
            }

            //check move through Check while Casteling
            if(piece.getID() == 6 && !piece.hasMoved()){
                if(piece.getColour()){
                    if(!field[7][5].isPossible){
                        field[7][6].isPossible = false;
                    }
                    if(!field[7][3].isPossible){
                        field[7][2].isPossible = false;
                    }
                }else{
                    if(!field[0][5].isPossible){
                        field[0][6].isPossible = false;
                    }
                    if(!field[0][3].isPossible){
                        field[0][2].isPossible = false;
                    }
                }
            }
            return new Board(field);
        }
    }
    public boolean checkiChecks(ArrayList<Turn> Zuege, Field[][] Brett, boolean pieceColour){     //Überprüft ob jemand im Schach ist
        Field[][] WhitePiecesPossible = new Field[8][8];
        Field[][] BlackPiecesPossible = new Field[8][8];
        Field[][] BrettCopy = new Field[8][8];

        copy(Brett, WhitePiecesPossible);
        copy(Brett, BlackPiecesPossible);
        copy(Brett, BrettCopy);

        int WhiteKingX=9, WhiteKingY=9;
        int BlackKingX=9, BlackKingY=9;

        WhitePiecesPossible = allPossibleMoves(true,Brett,Zuege);
        BlackPiecesPossible = allPossibleMoves(false, Brett, Zuege);

        //Find position of Kings
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (BrettCopy[i][j].piece != null) {
                    if (BrettCopy[i][j].piece.getID() == 6 && BrettCopy[i][j].piece.getColour()) {
                        WhiteKingX = i;
                        WhiteKingY = j;
                    }
                    //Black Pieces
                    if (BrettCopy[i][j].piece.getID() == 6 && !BrettCopy[i][j].piece.getColour()) {
                        BlackKingX = i;
                        BlackKingY = j;
                    }
                }
            }
        }
        clearPossible(BrettCopy);

        if (BlackPiecesPossible[WhiteKingX][WhiteKingY].isPossible) {
            if (pieceColour) {
                return false;
            }
        }
        if (WhitePiecesPossible[BlackKingX][BlackKingY].isPossible) {
            if (!pieceColour) {
                return false;
            }
        }

        return true;

    }

    public Field[][] allPossibleMoves(boolean Colour, Field[][] Brett, ArrayList<Turn> Zuege) {
        Field[][] PiecesPossible = new Field[8][8];
        copy(Brett, PiecesPossible);
        Field[][] BrettCopy = new Field[8][8];
        copy(Brett, BrettCopy);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int[] arr = {i, j};
                if (BrettCopy[i][j].piece != null && BrettCopy[i][j].piece.getColour() == Colour) {
                    clearPossible(BrettCopy);
                    BrettCopy[i][j].piece.possibleFields(arr, BrettCopy, Zuege);

                    for (int k = 0; k < 8; k++) {
                        for (int l = 0; l < 8; l++) {
                            if(BrettCopy[k][l].isPossible) {
                                PiecesPossible[k][l].isPossible = true;
                            }
                        }
                    }
                }
            }
        }

        return PiecesPossible;
    }
    public boolean hasPiece(String a){
        int[] arr = NameToCoordinate(a);
        return Brett[arr[0]][arr[1]].piece != null;
    }
    public boolean getPieceColour(String a){
        int[] arr = NameToCoordinate(a);
        return Brett[arr[0]][arr[1]].piece.getColour();
    }
    public Piece getPiece(String a){
        int[] arr = NameToCoordinate(a);
        if(Brett[arr[0]][arr[1]].piece != null) {
            return Brett[arr[0]][arr[1]].piece.getPiece();
        }else{
            return null;
        }
    }
    public boolean Possibles(){        //return true wenn no possible moves
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(Brett[i][j].isPossible){
                    return true;
                }
            }
        }
        return false;
    }

}

