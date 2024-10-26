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

    public static void signUpSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign up success");
        alert.setHeaderText("Sign up success");
        alert.setContentText("Your account has been created successfully.");
        alert.showAndWait();
    }

    public static void signInSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign in success");
        alert.setHeaderText("Sign in success");
        alert.setContentText("Your account has been logged in successfully.");
        alert.showAndWait();
    }

    public static void missingInformation() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Missing Information");
        alert.setHeaderText("Missing Information");
        alert.setContentText("Please enter a valid information.");
        alert.showAndWait();
    }

}
