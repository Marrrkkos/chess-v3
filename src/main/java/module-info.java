module com.chess.c {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.chess.c to javafx.fxml;
    exports com.chess.c;
}