package com.chess.c;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("chooseOpening.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("chooseOpening.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    //TODO
    //  Fix Casteling
    //  SimpleMove CastlingEnPasant
    //  Same opening Names
    //  TrainingsModus
    //  Drag N Drop
    //  Improve Remove-Line
    //  HotKeys
    //  Notation
    //  BrettNotation
    //  ListView interaction
    public static void main(String[] args) {
        launch();
    }
}
