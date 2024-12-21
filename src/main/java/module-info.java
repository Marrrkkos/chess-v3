module com.chess.c {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.chess.c to javafx.fxml;
    exports com.chess.c;
}