package com.chess.c;

import board.Board;
import board.Field;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import piece.Bishop;
import piece.Knight;
import piece.Queen;
import piece.Rook;
import player.Player;
import player.Turn;
import player.moveTree;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class chessBoardController implements Serializable {

    moveTree moveTree = new moveTree(new Turn(false));

    final int FIELDSIZE = 100;

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

    Boolean promotion = false;

    String chosenOpening = "";

    @FXML
    private GridPane gridPane;
    private Button[][] buttonArray = new Button[8][8];


    private Stage stage;
    private Scene scene;
    private Parent root;
    public void setChosenOpening(String chosen){
        chosenOpening = chosen;
    }
    @FXML
    public void switchToOpeningScene(ActionEvent event) throws IOException{
        root = FXMLLoader.load((getClass().getResource("chooseOpening.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("chooseOpening.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void setForwardButton(){
        if(ZuegeSpeicher.size()>Zuege.size()) {
            resetMoveView();
            player.Colour = true;
            board.resetPieces();
            Turn a = ZuegeSpeicher.get(Zuege.size());
            Zuege.add(a);
            count=0;
            for (Turn turn : Zuege) {
                board.doSimpleMove(turn.a1, turn.b1, player.Colour, ZuegeSpeicher, count);
                manageMoveView(turn.a1, turn.b1);
                count++;
                player.Colour = !player.Colour;
            }
            drawPieces();
            repaintArrows();
        }
    }
    @FXML
    public void setBackwardButton(){
        if(!Zuege.isEmpty()) {
            resetMoveView();
            board.resetPieces();
            Zuege.removeLast();
            count=0;
            player.Colour = true;
            for (Turn turn : Zuege) {
                board.doSimpleMove(turn.a1, turn.b1, player.Colour, Zuege, count);
                manageMoveView(turn.a1, turn.b1);
                count++;
                player.Colour = !player.Colour;
            }

            drawPieces();
            repaintArrows();
        }
    }
    @FXML
    public void setToStartButton(){
        resetMoveView();
        board.resetPieces();
        player.Colour = true;
        Zuege.clear();
        drawPieces();
        count = 0;
        repaintArrows();
    }
    @FXML
    public void setToEndButton(){
        resetMoveView();
        board.resetPieces();
        Zuege.clear();
        count=0;
        player.Colour = true;
        for (Turn turn : ZuegeSpeicher) {
            manageMoveView(turn.a1, turn.b1);
            Zuege.add(turn);
            board.doSimpleMove(turn.a1, turn.b1, player.Colour, ZuegeSpeicher, count);
            player.Colour = !player.Colour;
            count++;
        }
        drawPieces();
        repaintArrows();
    }
    @FXML
    public void saveLine(){

        moveTree.add(ZuegeSpeicher);

        String dateiName = "Openings/" + chosenOpening;

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dateiName))) {
            oos.writeObject(moveTree);
            System.out.println("Objekt gespeichert.");
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern: " + e.getMessage());
        }
    }
    @FXML
    public void removeLine(){
        if(!Zuege.isEmpty()) {
            System.out.println(Zuege);
            moveTree.remove(Zuege);
            System.out.println(Zuege);
            resetMoveView();
            board.resetPieces();
            count=0;
            player.Colour = true;
            for (Turn turn : Zuege) {
                board.doSimpleMove(turn.a1, turn.b1, player.Colour, ZuegeSpeicher,count);
                manageMoveView(turn.a1, turn.b1);
                count++;
                player.Colour = !player.Colour;
            }

            drawPieces();
            repaintArrows();

            String dateiName = "Openings/" + chosenOpening;

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dateiName))) {
                oos.writeObject(moveTree);
                System.out.println("Objekt gespeichert.");
            } catch (IOException e) {
                System.out.println("Fehler beim Speichern: " + e.getMessage());
            }
        }
    }
    @FXML
    public void loadLine(){
        String dateiName = "Openings/" + chosenOpening;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dateiName))) {
            moveTree = (moveTree) ois.readObject();

            System.out.println("Objekt geladen: " + moveTree);
            ArrayList<Turn> nextMoves = moveTree.getCurrentChildren(Zuege);
            drawAllArrows(nextMoves);
            for(Turn turn: nextMoves) {
                openingsView.getItems().add(turn.a1 + " " + turn.b1);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Fehler beim Laden: " + e.getMessage());
        }

    }
    @FXML
    public void training(){

    }
    int rotation = 0;
    @FXML
    public void rotateBoard(){
        Field[][] f = board.getBoard();
        rotation = (rotation+180)%360;
        gridPane.setRotate(rotation);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(f[i][j].piece != null) {
                    buttonArray[i][j].getGraphic().setRotate(rotation);
                }
            }
        }
    }
    @FXML
    public void boardView(ActionEvent event){
        if(promotion){
            int a = doPromotion(event);
            nextTurn(a);
            m1 = "";
            m2 = "";
            promotion = false;
        }
        if (m1.isEmpty()) {
            manageFirstClick(event);
        } else {
            m2 = ((Button) event.getSource()).getId();

            if(board.hasPiece(m2) && board.getPieceColour(m1) == board.getPieceColour(m2)){
                m2 = "";
                removeAllCircles();
                manageFirstClick(event);

            }else {
                boolean x;
                if (board.checkPromotion(m1, m2)) {
                    showPromotion();
                } else {
                    x = board.doMove(m1, m2, Zuege);
                    if (x) {
                        nextTurn(0);
                        System.out.println(Zuege);
                    }
                    m1 = "";
                    m2 = "";
                }
                possibleBoard.clearBoard();
                removeAllCircles();
            }
        }
    }
    private void manageFirstClick(ActionEvent event){
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
    }
    private int doPromotion(ActionEvent event){
        int x = 0;
        if(Objects.equals(((Button) event.getSource()).getId(), "c5")){
            x = 1;
        }
        if(Objects.equals(((Button) event.getSource()).getId(), "d5")){
            x = 2;
        }
        if(Objects.equals(((Button) event.getSource()).getId(), "e5")){
            x = 4;
        }
        if(Objects.equals(((Button) event.getSource()).getId(), "f5")){
            x = 3;
        }
        board.promotion(x,m1,m2);


        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttonArray[i][j].setVisible(true);
            }
        }
        return x;
    }
    private void showPromotion(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttonArray[i][j].setVisible(false);
            }
        }
        buttonArray[3][2].setVisible(true);
        buttonArray[3][3].setVisible(true);
        buttonArray[3][4].setVisible(true);
        buttonArray[3][5].setVisible(true);

        Queen queen = new Queen(player.Colour);
        Rook rook = new Rook(player.Colour, true);
        Bishop bishop = new Bishop(player.Colour);
        Knight knight = new Knight(player.Colour);

        ImageView queenView = new ImageView(queen.getImage());
        ImageView rookView = new ImageView(rook.getImage());
        ImageView bishopView = new ImageView(bishop.getImage());
        ImageView knightView = new ImageView(knight.getImage());

        buttonArray[3][2].setGraphic(queenView);
        buttonArray[3][3].setGraphic(rookView);
        buttonArray[3][4].setGraphic(bishopView);
        buttonArray[3][5].setGraphic(knightView);

        buttonArray[3][2].setVisible(true);
        buttonArray[3][3].setVisible(true);
        buttonArray[3][4].setVisible(true);
        buttonArray[3][5].setVisible(true);

        promotion = true;
    }
    private void nextTurn(int p){
        drawPieces();


        if(promotion){
            Zuege.add(count, new Turn(count, (count + 2) / 2, m1, (m2 + "" + writePromotion(p))));
            manageMoveView(m1, m2 + "" + writePromotion(p));
        }else {
            Zuege.add(count, new Turn(count, (count + 2) / 2, m1, m2));
            manageMoveView(m1, m2);
        }
        player.Colour = !player.Colour;
        ZuegeSpeicher = (ArrayList<Turn>) Zuege.clone();
        possibleBoard.clearBoard();

        count++;
        repaintArrows();
        long startTime = System.nanoTime();

        if (!board.checkCheckmate(Zuege, player.Colour, board)){
            System.out.println("Checkmate");
            for(Turn turn : Zuege){
                System.out.println(turn.a1 + "|" + turn.b1);
            }
        }
        long endTime = System.nanoTime();
        System.out.println("checkCheckmate Execution time:  " + (endTime - startTime) + " ns");
    }
    private String writePromotion(int p){
        return switch (p) {
            case 1 -> "=Q";
            case 2 -> "=R";
            case 3 -> "=K";
            case 4 -> "=B";
            default -> "=Q";
        };
    }
    private void repaintArrows(){
        ArrayList<Turn> nextMoves = moveTree.getCurrentChildren(Zuege);
        removeAllArrows();
        drawAllArrows(nextMoves);
        while(!openingsView.getItems().isEmpty()) {
            openingsView.getItems().removeLast();
        }
        for(Turn turn: nextMoves) {
            openingsView.getItems().add(turn.a1 + " " + turn.b1);
        }
    }
    public void drawAllArrows(ArrayList<Turn> Zuege){
        for(Turn turn: Zuege){
            if(turn.b1.contains("=")) {
                drawArrow(turn.a1, turn.b1.substring(0, 2));
            }else{
                drawArrow(turn.a1, turn.b1);
            }
        }
    }
    public void drawArrow(String a, String b) {
        double x1, y1;
        double x2, y2;

        int[] firstCoordinate = board.NameToCoordinate(a);
        int[] secondCoordinate = board.NameToCoordinate(b);

        x1 = firstCoordinate[1] * FIELDSIZE + 50;
        y1 = firstCoordinate[0] * FIELDSIZE + 50;
        x2 = secondCoordinate[1] * FIELDSIZE + 50;
        y2 = secondCoordinate[0] * FIELDSIZE + 50;

        Line line = new Line();


        double dx = x2 - x1;
        double dy = y2 - y1;
        double angle = Math.atan2(dy, dx);

        double arrowLength = 45;
        double arrowWidth = 65;

        double adjustedEndX = x2 - arrowLength * Math.cos(angle)*1.22;
        double adjustedEndY = y2 - arrowLength * Math.sin(angle)*1.22;

        double m1 = x2 - arrowLength * Math.cos(angle) + arrowWidth / 2 * Math.sin(angle);
        double n1 = y2 - arrowLength * Math.sin(angle) - arrowWidth / 2 * Math.cos(angle);

        double m2 = x2 - arrowLength * Math.cos(angle) - arrowWidth / 2 * Math.sin(angle);
        double n2 = y2 - arrowLength * Math.sin(angle) + arrowWidth / 2 * Math.cos(angle);

        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(adjustedEndX);
        line.setEndY(adjustedEndY);
        line.setMouseTransparent(true);
        line.setStroke(Color.GREEN);
        line.setStrokeWidth(20);
        line.setManaged(false);
        line.setOpacity(0.3);
        Polygon arrowHead = new Polygon();
        arrowHead.getPoints().addAll(
                x2, y2,
                m1, n1,
                m2, n2
        );
        arrowHead.setFill(Color.GREEN);
        arrowHead.setMouseTransparent(true);
        arrowHead.setManaged(false);
        arrowHead.setOpacity(0.3);
        gridPane.getChildren().addAll(line, arrowHead);
    }
    public void removeAllArrows() {
        // Filtern und alle Lines entfernen
        gridPane.getChildren().removeIf(node -> node instanceof Line);
        gridPane.getChildren().removeIf(node -> node instanceof Polygon);
    }
    private void removeAllCircles(){
        gridPane.getChildren().removeIf(node -> node instanceof Circle);
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
    DraggableMaker draggableMaker = new DraggableMaker();
    private void drawPieces() {
        Field[][] field = board.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (field[i][j].piece != null) {
                    ImageView ik = new ImageView(field[i][j].piece.getImage());
                    ik.setFitHeight(100);
                    ik.setFitWidth(100);
                    buttonArray[i][j].setGraphic(ik);
                    buttonArray[i][j].getGraphic().setRotate(rotation);

                    // Make the piece draggable
                    draggableMaker.makeDraggable(ik);

                    // Add drag-and-drop event listeners
                } else {
                    buttonArray[i][j].setGraphic(null);
                }
            }
        }
    }


    private Circle drawPoint(double i, double j){
        Circle circle = new Circle();
        circle.setCenterX(j*100+50);
        circle.setCenterY(i*100+50);
        circle.setManaged(false);
        circle.setRadius(12);
        circle.setOpacity(0.8);
        circle.setFill(Color.GRAY);
        circle.setMouseTransparent(true);

        return circle;
    }
    private void drawWPossible() {
        Field[][] b = board.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (b[i][j].isPossible) {
                    gridPane.getChildren().add(drawPoint(i,j));
                }
            }
        }
    }
    public Field[][] getBoard(){
        return board.getBoard();
    }

}