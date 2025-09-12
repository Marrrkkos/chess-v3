package com.chess.c;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("chooseOpening.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("chooseOpening.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    //TODO
    //  Sounds
    //  improve Forward/Backward Button
    //  improve ListView interaction
    //  BrettNotation
    //  Same window
    //  Window Size
    //  move Training Animation
    //  Fix LANGUAGE
    //  connect to Opening Database

    public static void main(String[] args) {
        launch();
    }
}
