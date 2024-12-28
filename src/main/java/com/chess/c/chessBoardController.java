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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
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
                board.doSimpleMove(turn.a1, turn.b1, player.Colour, ZuegeSpeicher,count-1);
                manageMoveView(turn.a1, turn.b1);
                count++;
                player.Colour = !player.Colour;
            }
            drawPieces();
            possibleBoard.clearBoard();
            repaintArrows();
            removeAllCircles();
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
                board.doSimpleMove(turn.a1, turn.b1, player.Colour, Zuege, count-1);
                manageMoveView(turn.a1, turn.b1);
                count++;
                player.Colour = !player.Colour;
            }
            removeAllCircles();
            possibleBoard.clearBoard();
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
            board.doSimpleMove(turn.a1, turn.b1, player.Colour, ZuegeSpeicher, count-1);
            player.Colour = !player.Colour;
            count++;
        }
        drawPieces();
        repaintArrows();
    }
    @FXML
    public void saveLine(){

        moveTree.add(Zuege);

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
                board.doSimpleMove(turn.a1, turn.b1, player.Colour, ZuegeSpeicher,count-1);
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
    public void initialize() {

        board.initializeBoard();
        ImageView dragImageView = new ImageView(); // Zum Anzeigen des Schachstücks beim Draggen
        dragImageView.setVisible(false);          // Standardmäßig unsichtbar
        dragImageView.setManaged(false);
        gridPane.getChildren().add(dragImageView);

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

                buttonArray[row][col].setOnMousePressed(mouseEvent -> {
                    if(promotion){
                        int a = doPromotion(button);
                        nextTurn(a);
                        m1 = "";
                        m2 = "";
                        promotion = false;
                    }else {
                        if (!m1.isEmpty()) {
                            double mouseX = mouseEvent.getSceneX();
                            double mouseY = mouseEvent.getSceneY();
                            int xPos, yPos;
                            if (rotation == 0) {
                                xPos = (int) ((mouseX - 50) / 100);
                                yPos = (int) ((mouseY - 50) / 100);
                            } else {
                                xPos = 7 - (int) ((mouseX - 50) / 100);
                                yPos = 7 - (int) ((mouseY - 50) / 100);
                            }
                            m2 = buttonArray[yPos][xPos].getId();
                            //Selbe Farbe → Neue Figur ausgewählt
                            if (board.hasPiece(m2) && board.getPieceColour(m1) == board.getPieceColour(m2)) {
                                removeAllCircles();
                                m1 = button.getId();
                                if (board.hasPiece(m1) && player.Colour == board.getPieceColour(m1)) {

                                    long startTime = System.nanoTime();
                                    possibleBoard = board.getPossible(m1, Zuege);
                                    long endTime = System.nanoTime();
                                    System.out.println("getPossible Execution time:  " + (endTime - startTime) + " ns");

                                    if (possibleBoard.Possibles()) {
                                        drawWPossible();

                                        Node graphic = button.getGraphic();
                                        if (graphic instanceof ImageView) {
                                            ImageView imageView = (ImageView) graphic;

                                            dragImageView.setImage(imageView.getImage());
                                            dragImageView.setFitWidth(imageView.getFitWidth());
                                            dragImageView.setFitHeight(imageView.getFitHeight());
                                            dragImageView.setVisible(true);

                                            if (rotation == 180) {
                                                dragImageView.setRotate(180);
                                                dragImageView.setLayoutX(gridPane.getWidth() - mouseEvent.getSceneX());
                                                dragImageView.setLayoutY(gridPane.getHeight() - mouseEvent.getSceneY());
                                            } else {
                                                dragImageView.setRotate(0);
                                                dragImageView.setLayoutX(mouseEvent.getSceneX() - gridPane.getWidth() / 8);
                                                dragImageView.setLayoutY(mouseEvent.getSceneY() - gridPane.getHeight() / 8);
                                            }

                                            button.setGraphic(null);
                                        }
                                    } else {
                                        m1 = "";
                                    }
                                } else {
                                    m1 = "";
                                }
                            } else {

                                boolean x;
                                if (board.checkPromotion(m1, m2)) {
                                    showPromotion();
                                    dragImageView.setVisible(false);
                                    removeAllCircles();
                                } else {

                                        x = board.doMove(m1, m2, Zuege);
                                        if (x) {

                                            nextTurn(0);
                                            dragImageView.setVisible(false);

                                        } else {
                                            dragImageView.setVisible(false);
                                        }
                                        m1 = "";
                                        m2 = "";

                                }
                                possibleBoard.clearBoard();
                                removeAllCircles();

                            }
                        } else {
                            m1 = button.getId();

                            if (board.hasPiece(m1) && player.Colour == board.getPieceColour(m1)) {

                                long startTime = System.nanoTime();
                                possibleBoard = board.getPossible(m1, Zuege);
                                long endTime = System.nanoTime();
                                System.out.println("getPossible Execution time:  " + (endTime - startTime) + " ns");

                                if (possibleBoard.Possibles()) {
                                    drawWPossible();

                                    // Drag-Bild initialisieren
                                    Node graphic = button.getGraphic();
                                    if (graphic instanceof ImageView) {
                                        ImageView imageView = (ImageView) graphic;

                                        dragImageView.setImage(imageView.getImage());
                                        dragImageView.setFitWidth(imageView.getFitWidth());
                                        dragImageView.setFitHeight(imageView.getFitHeight());
                                        dragImageView.setVisible(true); // Drag-Bild anzeigen
                                        if (rotation == 180) {
                                            dragImageView.setRotate(180);
                                            dragImageView.setLayoutX(gridPane.getWidth() - mouseEvent.getSceneX());
                                            dragImageView.setLayoutY(gridPane.getHeight() - mouseEvent.getSceneY());
                                        } else {
                                            dragImageView.setRotate(0);
                                            dragImageView.setLayoutX(mouseEvent.getSceneX() - gridPane.getWidth() / 8);
                                            dragImageView.setLayoutY(mouseEvent.getSceneY() - gridPane.getHeight() / 8);
                                        }
                                        button.setGraphic(null);
                                    }
                                } else {
                                    m1 = "";
                                }
                            } else {
                                m1 = "";
                            }
                        }
                    }
                });
                //-----------------------------------------------------------------------------------------------------------------
                //-----------------------------------------------------------------------------------------------------------------
                //-----------------------------------------------------------------------------------------------------------------
                buttonArray[row][col].setOnMouseDragged(mouseEvent -> {

                    if (rotation == 180) {
                        dragImageView.setLayoutX(gridPane.getWidth() - mouseEvent.getSceneX());
                        dragImageView.setLayoutY(gridPane.getHeight() - mouseEvent.getSceneY());
                    } else {
                        dragImageView.setLayoutX(mouseEvent.getSceneX() - gridPane.getWidth() / 8);
                        dragImageView.setLayoutY(mouseEvent.getSceneY() - gridPane.getHeight() / 8);
                    }
                });
                //-----------------------------------------------------------------------------------------------------------------
                //-----------------------------------------------------------------------------------------------------------------
                //-----------------------------------------------------------------------------------------------------------------
                buttonArray[row][col].setOnMouseReleased(mouseEvent -> {
                    if(!promotion) {
                        double mouseX = mouseEvent.getSceneX();
                        double mouseY = mouseEvent.getSceneY();
                        int xPos, yPos;
                        if (rotation == 0) {
                            xPos = (int) ((mouseX - 50) / 100);
                            yPos = (int) ((mouseY - 50) / 100);
                        } else {
                            xPos = 7 - (int) ((mouseX - 50) / 100);
                            yPos = 7 - (int) ((mouseY - 50) / 100);
                        }
                        m2 = buttonArray[yPos][xPos].getId();


                        if (m1.equals(m2)) {
                            m2 = "";
                            dragImageView.setVisible(false);
                            ImageView currentPieceView = new ImageView(dragImageView.getImage());
                            if (rotation == 180) {
                                currentPieceView.setRotate(180);
                            } else {
                                currentPieceView.setRotate(0);
                            }

                            button.setGraphic(currentPieceView);


                        }else if (board.checkPromotion(m1, m2)) {
                            showPromotion();
                            dragImageView.setVisible(false);
                            removeAllCircles();
                        } else if (board.doMove(m1, m2, Zuege)) {
                            nextTurn(0);
                            dragImageView.setVisible(false);
                            m1 = "";
                            m2 = "";
                            removeAllCircles();
                        } else {
                            if (button.getGraphic() == null && !m1.isEmpty()) {
                                ImageView currentPieceView = new ImageView(dragImageView.getImage());
                                if (rotation == 180) {
                                    currentPieceView.setRotate(180);
                                } else {
                                    currentPieceView.setRotate(0);
                                }

                                button.setGraphic(currentPieceView);
                                dragImageView.setVisible(false);
                            }
                        }
                    }
                });
            }
        }
        drawPieces();
    }
/*
 double mouseX = mouseEvent.getSceneX();
                    double mouseY = mouseEvent.getSceneY();
                    int xPos = (int) ((mouseX - 50) / 100);
                    int yPos = (int) ((mouseY - 50) / 100);
                    m2 = buttonArray[yPos][xPos].getId();


                    if (m1.equals(m2)) {
                        System.out.println("1");
                        m2 = "";
                        dragImageView.setVisible(false);
                        button.setGraphic(new ImageView(dragImageView.getImage()));
                    } else {
                        System.out.println("2");
                        if (board.hasPiece(m2) && board.getPieceColour(m1) == board.getPieceColour(m2)) {
                            System.out.println("3");
                            m2 = "";
                            dragImageView.setVisible(false);
                            System.out.println(button.getId());
                            button.setGraphic(new ImageView(dragImageView.getImage()));
                            removeAllCircles();

                            m1 = button.getId();
                            if (board.hasPiece(m1) && player.Colour == board.getPieceColour(m1)) {
                                System.out.println("4");
                                long startTime = System.nanoTime();
                                possibleBoard = board.getPossible(m1, Zuege);
                                long endTime = System.nanoTime();
                                System.out.println("getPossible Execution time:  " + (endTime - startTime) + " ns");

                                if (possibleBoard.Possibles()) {
                                    System.out.println("5");
                                    drawWPossible();

                                    // Drag-Bild initialisieren
                                    Node graphic = button.getGraphic();
                                    if (graphic instanceof ImageView) {
                                        System.out.println("6");
                                        ImageView imageView = (ImageView) graphic;

                                        dragImageView.setImage(imageView.getImage());
                                        dragImageView.setFitWidth(imageView.getFitWidth());
                                        dragImageView.setFitHeight(imageView.getFitHeight());
                                        dragImageView.setVisible(true); // Drag-Bild anzeigen
                                        dragImageView.setLayoutX(mouseEvent.getSceneX() - dragImageView.getFitWidth());
                                        dragImageView.setLayoutY(mouseEvent.getSceneY() - dragImageView.getFitHeight());
                                        button.setGraphic(null); // Verstecke ursprüngliches Bild
                                    }
                                } else {
                                    m1 = "";
                                }
                            } else {
                                m1 = "";
                            }


                        } else {
                            System.out.println("7");
                            boolean x;
                            if (board.checkPromotion(m1, m2)) {
                                System.out.println("8");
                                showPromotion();
                            } else {
                                System.out.println("9");
                                x = board.doMove(m1, m2, Zuege);
                                if (x) {
                                    System.out.println("10");
                                    nextTurn(0);
                                    System.out.println(Zuege);
                                    dragImageView.setVisible(false);
                                }else{
                                    System.out.println("11");
                                    button.setGraphic(new ImageView(dragImageView.getImage()));
                                    dragImageView.setVisible(false);
                                }
                                m1 = "";
                                m2 = "";
                            }
                            possibleBoard.clearBoard();
                            removeAllCircles();
                        }
                    }
                    System.out.println(m1);
                    System.out.println(m2);*/
    private void manF(ImageView dragImageView, Button button, MouseEvent mouseEvent){

    }
    @FXML
    public void boardView(ActionEvent event){
/*
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
        }*/
    }
    private void manageFirstClick(){

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

    //ImageView currentPieceView = (ImageView)((Button) event.getSource()).getGraphic();
    //currentPieceView.setMouseTransparent(false);
    //gridPane.getChildren().add(currentPieceView);
    //draggableMaker.makeDraggable(currentPieceView);
    private int doPromotion(Button button){
        int x = 0;
        if(Objects.equals(button.getId(), "c5")){
            x = 1;
        }
        if(Objects.equals(button.getId(), "d5")){
            x = 2;
        }
        if(Objects.equals(button.getId(), "e5")){
            x = 4;
        }
        if(Objects.equals(button.getId(), "f5")){
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
        drawPieces();
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
                    //ik.setX(j*100);
                    //ik.setY(i*100);
                    //ik.setManaged(false);
                    //gridPane.getChildren().add(ik);
                    // Make the piece draggable
                    //draggableMaker.makeDraggable(ik);

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