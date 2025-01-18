package com.chess.c;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import player.Turn;
import player.MoveTree;

import java.io.*;

public class ChooseOpeningController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    ListView<String> openingList;
    @FXML
    Button startButton;
    @FXML
    Button addButton;
    @FXML
    Button deleteButton;
    @FXML
    TextField nameField;
    @FXML
    Button addOpeningButton;
    @FXML
    Button yesButton;
    @FXML
    Button noButton;
    @FXML
    Label safetyLabel;
    @FXML
    ToggleButton colourButton;

    @FXML
    public void switchColour(){
        if(colourButton.isSelected()){
            colourButton.setStyle("-fx-background-color: beige");
        }else{
            colourButton.setStyle("-fx-background-color: #6e2c2c");
        }
    }
    @FXML
    public void add(){
        openingList.setOpacity(0.2);
        colourButton.setVisible(true);
        openingList.setFocusTraversable(false);
        startButton.setVisible(false);
        addButton.setVisible(false);
        deleteButton.setVisible(false);
        nameField.setVisible(true);
        addOpeningButton.setVisible(true);
    }
    @FXML
    public void delete(){
        String selectedItem = openingList.getSelectionModel().getSelectedItem();
        openingList.setOpacity(0.2);

        openingList.setFocusTraversable(false);
        startButton.setVisible(false);
        addButton.setVisible(false);
        deleteButton.setVisible(false);

        noButton.setVisible(true);
        yesButton.setVisible(true);
        safetyLabel.setText("Are you sure you want delete " + "'" + selectedItem + "'" + "?");
        safetyLabel.setVisible(true);
    }
    @FXML
    public void no(){
        noButton.setVisible(false);
        yesButton.setVisible(false);
        safetyLabel.setVisible(false);

        openingList.setOpacity(1);
        startButton.setVisible(true);
        openingList.setFocusTraversable(true);
        addButton.setVisible(true);
        deleteButton.setVisible(true);
        addOpeningButton.setVisible(false);
    }
    @FXML
    public void yes(){
        String selectedItem = openingList.getSelectionModel().getSelectedItem();
        File file = new File("Openings/" + selectedItem);
        file.delete();
        noButton.setVisible(false);
        yesButton.setVisible(false);
        safetyLabel.setVisible(false);

        openingList.setFocusTraversable(true);
        addButton.setVisible(true);
        deleteButton.setVisible(true);
        addOpeningButton.setVisible(false);
        startButton.setVisible(true);
        openingList.setOpacity(1);

        openingList.getItems().remove(selectedItem);
    }
    @FXML
    public void addOpening(){
        File dateiName = new File("Openings/");
        boolean sameName = false;
        String[] dateinamen = dateiName.list();
        assert dateinamen != null;
        for (String dateiname : dateinamen) {
            if(nameField.getCharacters().toString().equals(dateiname)){
                sameName = true;
            }
        }
        if(!nameField.getCharacters().toString().isEmpty() && !sameName) {
            openingList.getItems().add(nameField.getCharacters().toString());

            String fileName = "Openings/" + nameField.getCharacters().toString();
            new File(fileName);

                MoveTree moveTree = new MoveTree(new Turn(colourButton.isSelected()));

                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
                    oos.writeObject(moveTree);
                    System.out.println("Objekt gespeichert.");
                } catch (IOException e) {
                    System.out.println("Fehler beim Speichern: " + e.getMessage());
                }

            nameField.setVisible(false);
            openingList.setOpacity(1);

            colourButton.setVisible(false);
            startButton.setVisible(true);
            openingList.setFocusTraversable(true);
            addButton.setVisible(true);
            deleteButton.setVisible(true);
            addOpeningButton.setVisible(false);
        }
    }
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void switchToChessBoardScene(ActionEvent event) throws IOException {

        if(openingList.getSelectionModel().getSelectedItem() != null) {
            String chosenOpening = openingList.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            root = loader.load();
            chessBoardController chessBoardController = loader.getController();


            chessBoardController.setChosenOpening(chosenOpening);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("chess.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
            chessBoardController.loadTree();
        }
    }

    public void loadOpenings(){
        File dateiName = new File("Openings/");
        String[] dateinamen = dateiName.list();
        assert dateinamen != null;
        for (String dateiname : dateinamen) {
            openingList.getItems().add(dateiname);
        }
    }

    @FXML
    public void initialize(){
        colourButton.setStyle("-fx-background-color: #6e2c2c");
        colourButton.setVisible(false);
        openingList.setOnMouseClicked(mouseEvent -> {
            noButton.setVisible(false);
            yesButton.setVisible(false);
            safetyLabel.setVisible(false);
            nameField.setVisible(false);
            addOpeningButton.setVisible(false);
            colourButton.setVisible(false);

            openingList.setOpacity(1);
            startButton.setVisible(true);
            addButton.setVisible(true);
            deleteButton.setVisible(true);
        });
        noButton.setVisible(false);
        yesButton.setVisible(false);
        safetyLabel.setVisible(false);
        nameField.setVisible(false);
        addOpeningButton.setVisible(false);
        loadOpenings();
    }
}
