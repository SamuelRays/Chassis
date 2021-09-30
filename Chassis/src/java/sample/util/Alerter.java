package sample.util;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Alerter {
    public static void alert(Stage stage, String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
