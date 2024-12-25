package com.chess.c;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import player.Turn;
import player.moveTree;

import java.io.*;
import java.util.ArrayList;

public class ChooseOpeningController {
    @FXML
    ListView<String> openingList;
    @FXML
    Button addButton;
    @FXML
    Button deleteButton;
    @FXML
    TextField nameField;
    @FXML
    Button addOpeningButton;
    @FXML
    public void add(){
        openingList.setOpacity(0.2);

        openingList.setFocusTraversable(false);
        addButton.setVisible(false);
        deleteButton.setVisible(false);
        nameField.setVisible(true);
        addOpeningButton.setVisible(true);
    }
    @FXML
    public void delete(){
        String selectedItem = openingList.getSelectionModel().getSelectedItem();
        openingList.getItems().remove(selectedItem);
    }

    @FXML
    public void addOpening() throws FileNotFoundException {

        openingList.getItems().add(nameField.getCharacters().toString());
        new FileOutputStream("Openings/" + nameField.getCharacters().toString());

        nameField.setVisible(false);
        openingList.setOpacity(1);

        openingList.setFocusTraversable(true);
        addButton.setVisible(true);
        deleteButton.setVisible(true);
        addOpeningButton.setVisible(false);
    }
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void switchToChessBoardScene(ActionEvent event) throws IOException {

        String chosenOpening = openingList.getSelectionModel().getSelectedItem();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        root = loader.load();
        chessBoardController chessBoardController = loader.getController();


        chessBoardController.setChosenOpening(chosenOpening);


        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("chess.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        chessBoardController.loadLine();
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
        nameField.setVisible(false);
        addOpeningButton.setVisible(false);
        loadOpenings();
    }
}
