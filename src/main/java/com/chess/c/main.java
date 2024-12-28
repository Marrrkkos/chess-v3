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
    //  TrainingsModus
    //  Improve Remove-Line
    //  Improve showPromotion  
    //  Notation  <-
    //  BrettNotation  <-
    //  ListView interaction
    //  improve removeOpening
    public static void main(String[] args) {
        launch();
    }
}
