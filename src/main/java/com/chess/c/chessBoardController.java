package com.chess.c;

import board.Board;
import board.Field;
import board.NotationHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import piece.*;
import player.Knoten;
import player.Player;
import player.Turn;
import player.MoveTree;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class chessBoardController implements Serializable {

    MoveTree moveTree = new MoveTree(new Turn(true));

    final int FIELDSIZE = 100;
    @FXML
    AnchorPane anchorPane;
    @FXML
    private GridPane gridPane;
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
    @FXML
    ToggleButton trainingsButton;
    @FXML
    ToggleButton removeButton;
    @FXML
            Button saveButton;
    @FXML
            Button rotateButton;

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

    DrawOnBoard drawOnBoard = new DrawOnBoard();
    Boolean promotion = false;

    String chosenOpening = "";
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
        if((ZuegeSpeicher.size()>Zuege.size())) {
            resetMoveView();
            player.Colour = true;
            board.resetPieces();
            Turn a = ZuegeSpeicher.get(Zuege.size());
            Zuege.add(a);
            count=0;
            for (Turn turn : Zuege) {
                notationHandler.setAll(turn.a1, turn.b1, board.getPiece(turn.a1), board.getPiece(turn.b1));
                board.doSimpleMove(turn.a1, turn.b1, player.Colour, ZuegeSpeicher,count-1);
                notationHandler.setCheckiChecks(!board.checkiChecks(Zuege, board.getBoard(), !player.Colour));
                manageMoveView(notationHandler.handleNotation());
                count++;
                player.Colour = !player.Colour;
            }
            drawOnBoard.removeAllCircles(gridPane);
            possibleBoard.clearBoard();
            drawOnBoard.drawPieces(board,buttonArray,rotation);
            arrow.repaintArrows(gridPane, moveTree, Zuege, FIELDSIZE);
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
                notationHandler.setAll(turn.a1, turn.b1, board.getPiece(turn.a1), board.getPiece(turn.b1));
                board.doSimpleMove(turn.a1, turn.b1, player.Colour, Zuege, count-1);
                notationHandler.setCheckiChecks(!board.checkiChecks(Zuege, board.getBoard(), !player.Colour));
                manageMoveView(notationHandler.handleNotation());
                count++;
                player.Colour = !player.Colour;
            }
            drawOnBoard.removeAllCircles(gridPane);
            possibleBoard.clearBoard();
            drawOnBoard.drawPieces(board,buttonArray,rotation);
            arrow.repaintArrows(gridPane, moveTree, Zuege, FIELDSIZE);
        }
    }
    @FXML
    public void setToStartButton(){
        restartGame();
    }
    @FXML
    public void setToEndButton(){
        resetMoveView();
        board.resetPieces();
        Zuege.clear();
        count=0;
        player.Colour = true;
        for (Turn turn : ZuegeSpeicher) {
            notationHandler.setAll(turn.a1, turn.b1, board.getPiece(turn.a1), board.getPiece(turn.b1));
            manageMoveView(notationHandler.handleNotation());
            board.doSimpleMove(turn.a1, turn.b1, player.Colour, ZuegeSpeicher, count-1);
            notationHandler.setCheckiChecks(!board.checkiChecks(Zuege, board.getBoard(), !player.Colour));
            Zuege.add(turn);
            player.Colour = !player.Colour;
            count++;
        }
        drawOnBoard.removeAllCircles(gridPane);
        possibleBoard.clearBoard();
        drawOnBoard.drawPieces(board,buttonArray,rotation);
        arrow.repaintArrows(gridPane, moveTree, Zuege, FIELDSIZE);
    }
    @FXML
    public void saveLine(){

        if(chosenOpening != null){
            System.out.println("Line safed: " + moveTree.checkForMoreThenOneMove(Zuege, moveTree.getWurzelColour()) + "WurzelColour: " + moveTree.getWurzelColour());
            if(moveTree.checkForMoreThenOneMove(Zuege, moveTree.getWurzelColour())) {
                moveTree.add(Zuege);

                String dateiName = "Openings/" + chosenOpening;

                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dateiName))) {
                    oos.writeObject(moveTree);
                    System.out.println("Objekt gespeichert.");
                } catch (IOException e) {
                    System.out.println("Fehler beim Speichern: " + e.getMessage());
                }
            }
        }
    }
    @FXML
    public void setRemoveButton(){
        if(removeButton.isSelected()) {
            Line line = new Line();
            Polygon polygon = new Polygon();
            int countID = 0;
            for (Node node1 : gridPane.getChildren()) {

                if(node1 instanceof Button || node1 instanceof ToggleButton){
                    node1.setMouseTransparent(true);
                }

                if (node1 instanceof Line || node1 instanceof Polygon) {
                    node1.setMouseTransparent(false);
                    for (Node node : gridPane.getChildren()) {
                        if (node instanceof Line line1) {
                            System.out.println(line1.getId() + " + " + countID);
                            if (Objects.equals(line1.getId(), "" + countID)) {
                                line = line1;
                            }

                        }
                        if (node instanceof Polygon polygon1) {
                            if (Objects.equals(polygon1.getId(), "" + countID)) {
                                polygon = polygon1;
                            }
                        }
                    }
                    Line finalLine = line;
                    Polygon finalPolygon = polygon;

                    line.setOnMouseEntered(mouseEvent -> {
                        finalLine.setStroke(Color.RED);
                        finalPolygon.setFill(Color.RED);
                        finalLine.setOpacity(1);
                        finalPolygon.setOpacity(1);
                    });
                    line.setOnMouseExited(mouseEvent -> {
                        finalLine.setStroke(Color.GREEN);
                        finalPolygon.setFill(Color.GREEN);
                        finalLine.setOpacity(0.3);
                        finalPolygon.setOpacity(0.3);
                    });
                    line.setOnMouseClicked(mouseEvent -> {
                        String m1;
                        String m2;
                            if(player.Colour) {
                                System.out.println(finalLine.getStartX() + " " + finalLine.getStartY() + " " + finalLine.getEndX() + " " + finalLine.getEndY());
                                int startX = (int) finalLine.getStartX() / 100;
                                int startY = (int) (finalLine.getStartY()) / 100;
                                m1 = buttonArray[startY][startX].getId();
                                System.out.println("Start: " + buttonArray[startY][startX].getId());
                                int endX = (int) finalLine.getEndX() / 100;
                                int endY = (int) (finalLine.getEndY() - 55) / 100;      //-55 wegen arrowSize
                                m2 = buttonArray[endY][endX].getId();

                                System.out.println("endX: " + endX + "endY: " + endY);
                                System.out.println("End: " + buttonArray[endY][endX].getId());
                            }else {
                                System.out.println(finalLine.getStartX() + " " + finalLine.getStartY() + " " + finalLine.getEndX() + " " + finalLine.getEndY());
                                int startX = (int) finalLine.getStartX() / 100;
                                int startY = (int) (finalLine.getStartY()) / 100;
                                m1 = buttonArray[startY][startX].getId();
                                System.out.println("Start: " + buttonArray[startY][startX].getId());
                                int endX = (int) finalLine.getEndX() / 100;
                                int endY = (int) (finalLine.getEndY() + 50) / 100;
                                m2 = buttonArray[endY][endX].getId();

                                System.out.println("endX: " + endX + "endY: " + endY);
                                System.out.println("End: " + buttonArray[endY][endX].getId());

                            }



                        areYouSure(m1, m2);
                    });
                    polygon.setOnMouseEntered(mouseEvent -> {
                        finalLine.setStroke(Color.RED);
                        finalPolygon.setFill(Color.RED);
                        finalLine.setOpacity(1);
                        finalPolygon.setOpacity(1);
                    });
                    polygon.setOnMouseExited(mouseEvent -> {
                        finalLine.setStroke(Color.GREEN);
                        finalPolygon.setFill(Color.GREEN);
                        finalLine.setOpacity(0.3);
                        finalPolygon.setOpacity(0.3);
                    });
                    polygon.setOnMouseClicked(mouseEvent -> {
                        String m1;
                        String m2;
                        if(player.Colour) {
                            System.out.println(finalLine.getStartX() + " " + finalLine.getStartY() + " " + finalLine.getEndX() + " " + finalLine.getEndY());
                            int startX = (int) finalLine.getStartX() / 100;
                            int startY = (int) (finalLine.getStartY()) / 100;
                            m1 = buttonArray[startY][startX].getId();
                            System.out.println("Start: " + buttonArray[startY][startX].getId());
                            int endX = (int) finalLine.getEndX() / 100;
                            int endY = (int) (finalLine.getEndY() - 55) / 100;      //-55 wegen arrowSize
                            m2 = buttonArray[endY][endX].getId();

                            System.out.println("endX: " + endX + "endY: " + endY);
                            System.out.println("End: " + buttonArray[endY][endX].getId());
                        }else {
                            System.out.println(finalLine.getStartX() + " " + finalLine.getStartY() + " " + finalLine.getEndX() + " " + finalLine.getEndY());
                            int startX = (int) finalLine.getStartX() / 100;
                            int startY = (int) (finalLine.getStartY()) / 100;
                            m1 = buttonArray[startY][startX].getId();
                            System.out.println("Start: " + buttonArray[startY][startX].getId());
                            int endX = (int) finalLine.getEndX() / 100;
                            int endY = (int) (finalLine.getEndY() + 50) / 100;
                            m2 = buttonArray[endY][endX].getId();

                            System.out.println("endX: " + endX + "endY: " + endY);
                            System.out.println("End: " + buttonArray[endY][endX].getId());

                        }


                        areYouSure(m1,m2);
                    });
                    countID++;
                }
            }
        }else{
            for(Node node : gridPane.getChildren()){
                if(node instanceof Button || node instanceof ToggleButton){
                    node.setMouseTransparent(false);
                }
            }
            arrow.repaintArrows(gridPane, moveTree, Zuege, FIELDSIZE);
        }
    }

    public void areYouSure(String move1, String move2){
        StackPane overlayPane = new StackPane();
        overlayPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // Transparenter Hintergrund

        VBox overlayContent = new VBox(10); // VBox für besseren Abstand
        overlayContent.setAlignment(Pos.CENTER);

        Label label = new Label("Sicher?");
        Button button1 = new Button("Yes");
        Button button2 = new Button("No");
        MoveTree moveTree1 = new MoveTree(new Turn(true));

        Zuege.add(count, new Turn(count, (count + 2) / 2, move1, (move2), ""));
        moveTree1.addKnoten(moveTree.getSubTree(Zuege));
        List<ArrayList<Knoten>> a = moveTree1.erstellePfade();
        Zuege.removeLast();
        label.setText("Are you sure you want to delete " + a.size() + " Lines?");
        m1= "";
        m2 = "";
        button1.setOnMouseClicked(mouseEvent -> {
            overlayPane.setVisible(false);
            removeLine(move1, move2);

        });
        button2.setOnMouseClicked(mouseEvent -> {
            overlayPane.setVisible(false);
            arrow.repaintArrows(gridPane, moveTree, Zuege, FIELDSIZE);
            removeButton.setSelected(false);
        });
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        button1.setPrefWidth(80);
        button2.setPrefWidth(80);

        HBox buttonBox = new HBox(10); // Abstand zwischen den Buttons
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(button1, button2);

        overlayContent.getChildren().addAll(label, buttonBox);
        overlayPane.getChildren().add(overlayContent);

        overlayPane.setPrefSize(300, 200); // Feste Größe für das Overlay
        overlayPane.setVisible(true);

        anchorPane.getChildren().add(overlayPane);
        AnchorPane.setTopAnchor(overlayPane, 0.0);
        AnchorPane.setBottomAnchor(overlayPane, 0.0);
        AnchorPane.setLeftAnchor(overlayPane, 0.0);
        AnchorPane.setRightAnchor(overlayPane, 0.0);
        for(Node node : gridPane.getChildren()){
            if(node instanceof Button || node instanceof ToggleButton){
                node.setMouseTransparent(false);
            }
        }

    }
    public void removeLine(String move1, String move2){
        m1 = move1;
        m2 = move2;
        possibleBoard = board.getPossible(m1,Zuege);
        board.doSimpleMove(m1, m2, player.Colour, Zuege,count-1);
        nextTurn(0);
        if(!Zuege.isEmpty()) {

            moveTree.remove(Zuege);
            resetMoveView();
            board.resetPieces();
            count=0;
            player.Colour = true;
            for (Turn turn : Zuege) {
                notationHandler.setAll(turn.a1, turn.b1, board.getPiece(turn.a1), board.getPiece(turn.b1));
                board.doSimpleMove(turn.a1, turn.b1, player.Colour, ZuegeSpeicher,count-1);
                notationHandler.setCheckiChecks(board.checkiChecks(Zuege, board.getBoard(), player.Colour));
                manageMoveView(notationHandler.handleNotation());
                count++;
                player.Colour = !player.Colour;
            }

            drawOnBoard.drawPieces(board,buttonArray,rotation);
            arrow.repaintArrows(gridPane, moveTree, Zuege, FIELDSIZE);
            removeButton.setSelected(false);

            String dateiName = "Openings/" + chosenOpening;

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dateiName))) {
                oos.writeObject(moveTree);
                System.out.println("Objekt gespeichert.");
            } catch (IOException e) {
                System.out.println("Fehler beim Speichern: " + e.getMessage());
            }
        }
    }

    public void loadTree(){
        String dateiName = "Openings/" + chosenOpening;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dateiName))) {
            moveTree = (MoveTree) ois.readObject();

            System.out.println("Objekt geladen: " + moveTree);
            ArrayList<Turn> nextMoves = moveTree.getCurrentChildren(Zuege);
            //drawAllArrows(nextMoves);
            arrow.repaintArrows(gridPane, moveTree, Zuege, FIELDSIZE);


            for(Turn turn: nextMoves) {
                notationHandler.setAll(turn.a1, turn.b1, board.getPiece(turn.a1), board.getPiece(turn.b1));
                openingsView.getItems().add(notationHandler.handleNotation());
            }

            if(!moveTree.getWurzelColour()){
                rotateBoard();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Fehler beim Laden: " + e.getMessage());
        }

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
    int promoteXPos = 0;
    int promoteYPos = 0;
    Button previousButton;
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
                        notationHandler.setAll(m1, m2, board.getPiece(m1),board.getPiece(m2));
                        int a = doPromotion(button, promoteXPos,promoteYPos, player.Colour);
                        notationHandler.addPromotion(writePromotion(a));
                        if(promotion) {
                            playerMove1 = m1;
                            playerMove2 = m2 + "" + writePromotion(a);
                            nextTurn(a);
                            if (trainingActive) {
                                trainingsModus();
                            }

                        }
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
                                drawOnBoard.removeAllCircles(gridPane);
                                m1 = button.getId();
                                if (board.hasPiece(m1) && player.Colour == board.getPieceColour(m1)) {

                                    possibleBoard = board.getPossible(m1, Zuege);

                                    if (possibleBoard.Possibles()) {
                                        drawOnBoard.drawAllPossibleMoves(board, gridPane);

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
                                            button.getGraphic().setOpacity(0.4);
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
                                    promoteXPos = xPos;
                                    promoteYPos = yPos;
                                    showPromotion(xPos, yPos, player.Colour);
                                    dragImageView.setVisible(false);
                                    drawOnBoard.removeAllCircles(gridPane);
                                } else {
                                        notationHandler.setAll(m1,m2,board.getPiece(m1), board.getPiece(m2));
                                        x = board.doMove(m1, m2, Zuege);
                                        if (x) {
                                            playerMove1 = m1;
                                            playerMove2 = m2;
                                            nextTurn(0);
                                            dragImageView.setVisible(false);
                                            if(trainingActive){
                                               trainingsModus();                                                                                      //<----
                                            }

                                        } else {
                                            dragImageView.setVisible(false);
                                        }
                                        m1 = "";
                                        m2 = "";

                                }
                                possibleBoard.clearBoard();
                                drawOnBoard.removeAllCircles(gridPane);

                            }
                        } else {
                            m1 = button.getId();

                            if (board.hasPiece(m1) && player.Colour == board.getPieceColour(m1)) {

                                possibleBoard = board.getPossible(m1, Zuege);

                                if (possibleBoard.Possibles()) {
                                    drawOnBoard.drawAllPossibleMoves(board, gridPane);

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
                                        button.getGraphic().setOpacity(0.4);
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
                    int x1 = (int) (mouseEvent.getSceneX() - 50) / 100;
                    int x2 = (int) (mouseEvent.getSceneY() - 50) / 100;

                    // Stelle sicher, dass die Koordinaten innerhalb des gültigen Bereichs liegen
                    if (x1 >= 0 && x1 < 8 && x2 >= 0 && x2 < 8) {
                        Button currentButton = buttonArray[x2][x1];

                        if (board.getBoard()[x2][x1].isPossible) {
                            if (previousButton != null && previousButton != currentButton) {
                                previousButton.setOpacity(1); // Setze den vorherigen Button zurück
                            }

                            currentButton.setOpacity(0.4);
                            previousButton = currentButton; // Aktualisiere den vorherigen Button
                        } else {
                            // Setze alle Buttons zurück, falls keine gültige Position
                            for (int i = 0; i < 8; i++) {
                                for (int j = 0; j < 8; j++) {
                                    buttonArray[i][j].setOpacity(1);
                                }
                            }
                            previousButton = null; // Zurücksetzen, da keine gültige Position
                        }
                    }
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
                    if(previousButton != null) {
                        previousButton.setOpacity(1);
                    }
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
                        if(yPos<8 && yPos >= 0 && xPos<8 && xPos>=0) {
                            m2 = buttonArray[yPos][xPos].getId();
                        }
                        notationHandler.setAll(m1,m2,board.getPiece(m1),board.getPiece(m2));
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
                            promoteXPos = xPos;
                            promoteYPos = yPos;
                            showPromotion(xPos, yPos, player.Colour);
                            dragImageView.setVisible(false);
                            drawOnBoard.removeAllCircles(gridPane);

                        } else if (board.doMove(m1, m2, Zuege)) {
                            playerMove1 = m1;
                            playerMove2 = m2;

                            nextTurn(0);
                            dragImageView.setVisible(false);                                                 //<---
                            m1 = "";
                            m2 = "";
                            drawOnBoard.removeAllCircles(gridPane);

                            if(trainingActive){
                                trainingsModus();
                            }

                        } else {
                            if ((button.getGraphic() != null) && (button.getGraphic().getOpacity() != 1) && !m1.isEmpty()) {
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
        drawOnBoard.drawPieces(board,buttonArray,rotation);
    }

    @FXML
    public void training() {
        if(trainingsButton.isSelected()) {
            if (!moveTree.getWurzelKnoten().isEmpty()) {
                restartGame();
                allLines = moveTree.erstellePfade();
                Collections.shuffle(allLines);
                currentTrainingsLine = knotenToTurns(moveTree.erstellePfade().getFirst());
                currentTrainingsLine.removeFirst();
                trainingActive = true;
                trainingOnVisual();
                if (!moveTree.getWurzelColour()) {
                    m1 = currentTrainingsLine.getFirst().a1;
                    possibleBoard = board.getPossible(m1, Zuege);
                    m2 = currentTrainingsLine.getFirst().b1;
                    board.doSimpleMove(m1, m2, player.Colour, Zuege, count - 1);
                    currentTrainingsLine.removeFirst();
                    nextTurn(0);
                    m1 = "";
                    m2 = "";
                }
            }
        }else{
            trainingActive = false;
            trainingOffVisual();
            restartGame();
            gridPane.setStyle("-fx-border-color: transparent; -fx-border-width: 10; -fx-border-insets: -10;");
        }
    }
    private void wrongMoveVisual(){
        gridPane.setStyle("-fx-border-color: red; -fx-border-width: 10; -fx-border-insets: -10;");
        Rectangle filter = new Rectangle(gridPane.getWidth(), gridPane.getHeight(), Color.RED);
        filter.setBlendMode(BlendMode.OVERLAY);
        filter.setManaged(false);
        filter.setMouseTransparent(false);
        gridPane.getChildren().add(filter);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        e -> filter.setOpacity(0.2)),
                new KeyFrame(Duration.seconds(0.025),
                        e -> filter.setOpacity(0.4)),
                new KeyFrame(Duration.seconds(0.05),
                        e -> filter.setOpacity(0.6)),
                new KeyFrame(Duration.seconds(0.075),
                        e -> filter.setOpacity(0.4)),
                new KeyFrame(Duration.seconds(0.1),
                        e -> filter.setOpacity(0.2)),
                new KeyFrame(Duration.seconds(0.125),
                        e -> {  filter.setOpacity(0);
                                gridPane.setStyle("-fx-border-color: transparent; -fx-border-width: 10; -fx-border-insets: -10;");
                                gridPane.getChildren().remove(filter);}
                ));
        timeline.play();


    }
    boolean trainingActive = false;
    String playerMove1 = "";
    String playerMove2 = "";
    List<ArrayList<Knoten>> allLines;
    ArrayList<Turn> currentTrainingsLine;
    private void trainingsModus(){
        Turn moveByPlayer;
        if(!currentTrainingsLine.isEmpty()){
            moveByPlayer = currentTrainingsLine.removeFirst();
            if(!moveByPlayer.a1.equals(playerMove1) || !moveByPlayer.b1.equals(playerMove2)){

                wrongMoveVisual();
                System.out.println("Wrong Move");
                currentTrainingsLine.clear();
            }
        }


        if(!currentTrainingsLine.isEmpty()) {
            m1 = currentTrainingsLine.getFirst().a1;
            possibleBoard = board.getPossible(m1, Zuege);
            m2 = currentTrainingsLine.getFirst().b1;
            board.doSimpleMove(m1, m2, player.Colour, Zuege, count-1);
        }
        if(!currentTrainingsLine.isEmpty()){
            currentTrainingsLine.removeFirst();
            nextTurn(0);
            m1 = "";
            m2 = "";
            drawOnBoard.removeAllCircles(gridPane);
        }
        if(currentTrainingsLine.isEmpty()){
            board.resetPieces();
            allLines.removeFirst();
            if(!allLines.isEmpty()) {
                currentTrainingsLine = knotenToTurns(allLines.getFirst());
                currentTrainingsLine.removeFirst();
                restartGame();
                if(!moveTree.getWurzelColour()){
                    m1 = currentTrainingsLine.getFirst().a1;
                    possibleBoard = board.getPossible(m1, Zuege);
                    m2 = currentTrainingsLine.getFirst().b1;
                    board.doSimpleMove(m1, m2, player.Colour, Zuege, count-1);
                    currentTrainingsLine.removeFirst();
                    nextTurn(0);
                    m1 = "";
                    m2 = "";
                }
            }else {
                trainingActive = false;
                trainingOffVisual();
                currentTrainingsLine.clear();
                restartGame();
                gridPane.setStyle("-fx-border-color: transparent; -fx-border-width: 10; -fx-border-insets: -10;");
                trainingsButton.setSelected(false);
                System.out.println("Training abgeschlossen");
            }
        }

    }
    private void restartGame(){
        resetMoveView();
        board.resetPieces();
        player.Colour = true;
        Zuege.clear();
        drawOnBoard.drawPieces(board,buttonArray,rotation);
        count = 0;
        arrow.repaintArrows(gridPane, moveTree, Zuege, FIELDSIZE);
        drawOnBoard.removeAllCircles(gridPane);
        possibleBoard.clearBoard();
    }
    private int doPromotion(Button button, int xPosButton, int yPosButton, boolean colour){
        int x = 0;
        if(colour) {
            if (Objects.equals(button, buttonArray[yPosButton][xPosButton])) {
                x = 1;
            }else if (Objects.equals(button, buttonArray[yPosButton + 1][xPosButton])) {
                x = 2;
            }else if (Objects.equals(button, buttonArray[yPosButton + 2][xPosButton])) {
                x = 4;
            }else if (Objects.equals(button, buttonArray[yPosButton + 3][xPosButton])) {
                x = 3;
            }else{
                promotion = false;
            }
            buttonArray[yPosButton][xPosButton].setStyle("-fx-background-radius: 0");
            buttonArray[yPosButton + 1][xPosButton].setStyle("-fx-background-radius: 0");
            buttonArray[yPosButton + 2][xPosButton].setStyle("-fx-background-radius: 0");
            buttonArray[yPosButton + 3][xPosButton].setStyle("-fx-background-radius: 0");


        }else {
            if (Objects.equals(button, buttonArray[yPosButton][xPosButton])) {
                x = 1;
            }else if (Objects.equals(button, buttonArray[yPosButton - 1][xPosButton])) {
                x = 2;
            }else if (Objects.equals(button, buttonArray[yPosButton - 2][xPosButton])) {
                x = 4;
            }else if (Objects.equals(button, buttonArray[yPosButton - 3][xPosButton])) {
                x = 3;
            }else{
                promotion = false;
            }
            buttonArray[yPosButton][xPosButton].setStyle("-fx-background-radius: 0");
            buttonArray[yPosButton - 1][xPosButton].setStyle("-fx-background-radius: 0");
            buttonArray[yPosButton - 2][xPosButton].setStyle("-fx-background-radius: 0");
            buttonArray[yPosButton - 3][xPosButton].setStyle("-fx-background-radius: 0");

        }
        if(promotion) {
            board.promotion(x, m1, m2);
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttonArray[i][j].setOpacity(1);
            }
        }
        drawOnBoard.drawPieces(board,buttonArray,rotation);
        return x;
    }
    private void showPromotion(int xPosButton, int yPosButton, boolean colour){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttonArray[i][j].setOpacity(0.2);
            }
        }
        Queen queen = new Queen(colour);
        Rook rook = new Rook(colour, true);
        Bishop bishop = new Bishop(colour);
        Knight knight = new Knight(colour);

        ImageView queenView = new ImageView(queen.getImage());
        ImageView rookView = new ImageView(rook.getImage());
        ImageView bishopView = new ImageView(bishop.getImage());
        ImageView knightView = new ImageView(knight.getImage());

        if(colour) {
            buttonArray[yPosButton][xPosButton].setStyle("-fx-background-radius: 90");
            buttonArray[yPosButton + 1][xPosButton].setStyle("-fx-background-radius: 90");
            buttonArray[yPosButton + 2][xPosButton].setStyle("-fx-background-radius: 90");
            buttonArray[yPosButton + 3][xPosButton].setStyle("-fx-background-radius: 90");

            buttonArray[yPosButton][xPosButton].setOpacity(1);
            buttonArray[yPosButton + 1][xPosButton].setOpacity(1);
            buttonArray[yPosButton + 2][xPosButton].setOpacity(1);
            buttonArray[yPosButton + 3][xPosButton].setOpacity(1);

            buttonArray[yPosButton][xPosButton].setGraphic(queenView);
            buttonArray[yPosButton+1][xPosButton].setGraphic(rookView);
            buttonArray[yPosButton+2][xPosButton].setGraphic(bishopView);
            buttonArray[yPosButton+3][xPosButton].setGraphic(knightView);
            if(rotation == 180){
                buttonArray[yPosButton][xPosButton].getGraphic().setRotate(180);
                buttonArray[yPosButton + 1][xPosButton].getGraphic().setRotate(180);
                buttonArray[yPosButton + 2][xPosButton].getGraphic().setRotate(180);
                buttonArray[yPosButton + 3][xPosButton].getGraphic().setRotate(180);
            }else{
                buttonArray[yPosButton][xPosButton].getGraphic().setRotate(0);
                buttonArray[yPosButton + 1][xPosButton].getGraphic().setRotate(0);
                buttonArray[yPosButton + 2][xPosButton].getGraphic().setRotate(0);
                buttonArray[yPosButton + 3][xPosButton].getGraphic().setRotate(0);
            }
        }else{
            buttonArray[yPosButton][xPosButton].setStyle("-fx-background-radius: 90");
            buttonArray[yPosButton - 1][xPosButton].setStyle("-fx-background-radius: 90");
            buttonArray[yPosButton - 2][xPosButton].setStyle("-fx-background-radius: 90");
            buttonArray[yPosButton - 3][xPosButton].setStyle("-fx-background-radius: 90");

            buttonArray[yPosButton][xPosButton].setOpacity(1);
            buttonArray[yPosButton - 1][xPosButton].setOpacity(1);
            buttonArray[yPosButton - 2][xPosButton].setOpacity(1);
            buttonArray[yPosButton - 3][xPosButton].setOpacity(1);

            buttonArray[yPosButton][xPosButton].setGraphic(queenView);
            buttonArray[yPosButton-1][xPosButton].setGraphic(rookView);
            buttonArray[yPosButton-2][xPosButton].setGraphic(bishopView);
            buttonArray[yPosButton-3][xPosButton].setGraphic(knightView);
            if(rotation == 180){
                buttonArray[yPosButton][xPosButton].getGraphic().setRotate(180);
                buttonArray[yPosButton - 1][xPosButton].getGraphic().setRotate(180);
                buttonArray[yPosButton - 2][xPosButton].getGraphic().setRotate(180);
                buttonArray[yPosButton - 3][xPosButton].getGraphic().setRotate(180);
            }else{
                buttonArray[yPosButton][xPosButton].getGraphic().setRotate(0);
                buttonArray[yPosButton - 1][xPosButton].getGraphic().setRotate(0);
                buttonArray[yPosButton - 2][xPosButton].getGraphic().setRotate(0);
                buttonArray[yPosButton - 3][xPosButton].getGraphic().setRotate(0);
            }
        }


        promotion = true;
    }
    private void trainingOnVisual(){
        removeButton.setDisable(true);
        forwardButton.setDisable(true);
        backwardButton.setDisable(true);
        toStartButton.setDisable(true);
        toEndButton.setDisable(true);
        saveButton.setDisable(true);
        rotateButton.setDisable(true);
    }
    private void trainingOffVisual(){
        removeButton.setDisable(false);
        forwardButton.setDisable(false);
        backwardButton.setDisable(false);
        toStartButton.setDisable(false);
        toEndButton.setDisable(false);
        saveButton.setDisable(false);
        rotateButton.setDisable(false);
    }
    private ArrayList<Turn> knotenToTurns(ArrayList<Knoten> c){
        ArrayList<Turn> b = new ArrayList<>();
        for(Knoten knoten : c){
            b.add(knoten.getTurn());
        }
        return b;
    }
    NotationHandler notationHandler = new NotationHandler("", "", new Pawn(false, false), new Pawn(false, false), false);
    private void nextTurn(int p){
        String notationHandled = "";
        if(promotion){
            Zuege.add(count, new Turn(count, (count + 2) / 2, m1, (m2 + "" + writePromotion(p)), notationHandled));
            notationHandler.setCheckiChecks(!board.checkiChecks(Zuege, board.getBoard(), !player.Colour));
            notationHandled = notationHandler.handleNotation();
            manageMoveView(notationHandled);
        }else {
            Zuege.add(count, new Turn(count, (count + 2) / 2, m1, m2, notationHandler.handleNotation()));
            notationHandler.setCheckiChecks(!board.checkiChecks(Zuege, board.getBoard(), !player.Colour));
            notationHandled = notationHandler.handleNotation();
            manageMoveView(notationHandled);
        }
        player.Colour = !player.Colour;
        ZuegeSpeicher = (ArrayList<Turn>) Zuege.clone();

        possibleBoard.clearBoard();

        count++;
        arrow.repaintArrows(gridPane, moveTree, Zuege, FIELDSIZE);

        if (!board.checkCheckmate(Zuege, player.Colour, board)){
            System.out.println("Checkmate");
            for(Turn turn : Zuege){
                System.out.println(turn.a1 + "|" + turn.b1);
            }
        }
        drawOnBoard.drawPieces(board, buttonArray, rotation);
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
    Arrow arrow = new Arrow();


    private void manageMoveView(String a){
        if(player.Colour){
            movesView.getItems().add(count/2, (count + 2) / 2 + ".  " + a);
            moveView = (a);
        }else{
            movesView.getItems().add(count/2,(count + 2) / 2 + ".  " + moveView + " | " + a);
            movesView.getItems().removeLast();
        }
    }
    private void resetMoveView(){
        movesView.getItems().clear();
    }

}