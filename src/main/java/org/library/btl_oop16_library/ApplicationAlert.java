package org.library.btl_oop16_library;

import javafx.scene.control.Alert;

public class ApplicationAlert {
    public static void emptyAccountOrPassword() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Empty account or password");
        alert.setHeaderText("Account or password cant be empty");
        alert.setContentText("Please enter a valid account or password.");
        alert.showAndWait();
    }

    public static void wrongUsernameOrPassword() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Wrong username or password");
        alert.setHeaderText("Wrong username or password");
        alert.setContentText("Please enter a valid username or password.");
        alert.showAndWait();
    }
}
