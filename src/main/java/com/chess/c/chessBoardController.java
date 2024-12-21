package com.chess.c;

import board.Board;
import board.Field;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import player.Player;
import player.Turn;
import player.moveTree;

import java.io.*;
import java.util.ArrayList;

public class chessBoardController implements Serializable {



    @FXML
    ListView<String> movesView;
    @FXML
    ListView<String> openingsView;
    @FXML
    Button forwardButton;
    @FXML
    Button backwardButton;
    @FXML
    Button toStartButton;
    @FXML
    Button toEndButton;

    ArrayList<Turn> Zuege = new ArrayList<>();
    ArrayList<Turn> ZuegeSpeicher = new ArrayList<>();
    Player player = new Player(true);

    Board board = new Board(new Field[8][8]);
    Board possibleBoard = new Board(new Field[8][8]);

    int count = 0;

    // Global MoveCheckers
    String m1 = "";
    String m2 = "";
    String moveView = "";
    @FXML
    private GridPane gridPane;
    private Button[][] buttonArray = new Button[8][8];

    @FXML
    public void setForwardButton(ActionEvent event){
        if(ZuegeSpeicher.size()>Zuege.size()) {
            resetMoveView();
            player.Colour = true;
            board.resetPieces();
            Turn a = ZuegeSpeicher.get(Zuege.size());
            Zuege.add(a);
            count=0;
            for (Turn turn : Zuege) {
                board.doSimpleMove(turn.a1, turn.b1);
                manageMoveView(turn.a1, turn.b1);
                count++;
                player.Colour = !player.Colour;
            }
            drawPieces();
        }
    }
    @FXML
    public void setBackwardButton(ActionEvent event){
        if(!Zuege.isEmpty()) {
            resetMoveView();
            board.resetPieces();
            Zuege.removeLast();
            count=0;
            player.Colour = true;
            for (Turn turn : Zuege) {
                board.doSimpleMove(turn.a1, turn.b1);
                manageMoveView(turn.a1, turn.b1);
                count++;
                player.Colour = !player.Colour;
            }

            drawPieces();
        }
    }
    @FXML
    public void setToStartButton(ActionEvent event){
        resetMoveView();
        board.resetPieces();
        player.Colour = true;
        Zuege.clear();
        drawPieces();
        count = 0;
    }
    @FXML
    public void setToEndButton(ActionEvent event){
        resetMoveView();
        board.resetPieces();
        Zuege.clear();
        count=0;
        player.Colour = true;
        for (Turn turn : ZuegeSpeicher) {
            manageMoveView(turn.a1, turn.b1);
            player.Colour = !player.Colour;
            Zuege.add(turn);
            board.doSimpleMove(turn.a1, turn.b1);
            count++;
        }
        drawPieces();
    }
    moveTree moveTree = new moveTree(new Turn());
    @FXML
    public void saveLine(){

        moveTree.add(ZuegeSpeicher);
        System.out.println(moveTree);
        moveTree.traverseLevelOrder();

        String dateiName = "moveTree.ser";

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dateiName))) {
            oos.writeObject(moveTree);
            System.out.println("Objekt gespeichert.");
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern: " + e.getMessage());
        }
    }
    @FXML
    public void loadLine(){
        String dateiName = "moveTree.ser";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dateiName))) {
            moveTree geladenerMoveTree = (moveTree) ois.readObject();
            System.out.println("Objekt geladen: " + geladenerMoveTree);
            geladenerMoveTree.traverseLevelOrder();
            System.out.println(geladenerMoveTree);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Fehler beim Laden: " + e.getMessage());
        }

    }
    @FXML
    public void boardView(ActionEvent event){
        if (m1.isEmpty()) {
            m1 = ((Button) event.getSource()).getId();
            if (board.hasPiece(m1) && player.Colour == board.getPieceColour(m1)) {
                long startTime = System.nanoTime();
                possibleBoard = board.getPossible(m1, Zuege);
                long endTime = System.nanoTime();
                System.out.println("getPossible Execution time:  " + (endTime - startTime) + " ns");
                if (possibleBoard.Possibles()) {
                    drawWPossible();
                }else{
                    m1 = "";
                }
            } else {
                m1 = "";
            }
        } else {
            m2 = ((Button) event.getSource()).getId();
            boolean x = false;
            // Überprüfe auf Promotion
            if (board.checkPromotion(m1, m2)) {
                //promotionPanel.setVisible(true); // Zeige die Promotion-Buttons an
            } else {
                x = board.doMove(m1, m2, Zuege);

                if (x) {
                    removeWPossible();
                    drawPieces();

                    manageMoveView(m1, m2);

                    player.Colour = !player.Colour;
                    Zuege.add(count, new Turn(count, (count + 2) / 2, m1, m2));
                    ZuegeSpeicher = (ArrayList<Turn>) Zuege.clone();
                    possibleBoard.clearBoard();
                    count++;


                    /*if (!board.checkCheckmate(Zuege, player.Colour, board)){
                        System.out.println("Checkmate");
                        for(Turn turn : Zuege){
                            System.out.println(turn.a1 + "|" + turn.b1);
                        }
                    }*/
                }
                m1 = "";
                m2 = "";
            }

        }
    }


    private void manageMoveView(String a, String b){
        if(player.Colour){
            movesView.getItems().add(count/2, (count + 2) / 2 + ".  " + a + "-" + b);
            moveView = (a + "-" + b);
        }else{
            movesView.getItems().add(count/2,(count + 2) / 2 + ".  " + moveView + " | " + a + "-" + b);
            movesView.getItems().removeLast();
        }
    }
    private void resetMoveView(){
        movesView.getItems().clear();
    }
    @FXML
    public void initialize() {
        board.initializeBoard();
        Field[][] field = board.getBoard();
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                Integer row = GridPane.getRowIndex(button);
                Integer col = GridPane.getColumnIndex(button);

                // Null-Sicher: Standardwert 0, wenn Row/Col null ist
                row = (row == null) ? 0 : row;
                col = (col == null) ? 0 : col;

                // Button im Array speichern
                buttonArray[row][col] = button;
            }
        }
        drawPieces();

    }
    private void drawPieces(){
        Field[][] field = board.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(field[i][j].piece != null){
                    ImageView ik = new ImageView(field[i][j].piece.getImage());
                    buttonArray[i][j].setGraphic(ik);
                }else{
                    buttonArray[i][j].setGraphic(null);
                }
            }
        }
    }
    private void drawWPossible() {
        Field[][] b = board.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (b[i][j].isPossible) {
                    buttonArray[i][j].setStyle("-fx-border-color: lightgreen");
                }
            }
        }
    }
    private void removeWPossible() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttonArray[i][j].setStyle("-fx-border-color: none");
            }
        }
    }

}
