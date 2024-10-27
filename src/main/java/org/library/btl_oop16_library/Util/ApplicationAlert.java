package org.library.btl_oop16_library.Util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ApplicationAlert {

    public static void emptyAccountOrPassword() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Empty account or password!");
        alert.setHeaderText("Account or password cant be empty!");
        alert.setContentText("Please enter a valid account or password!");
        alert.showAndWait();
    }

    public static void wrongUsernameOrPassword() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Wrong username or password!");
        alert.setHeaderText("Wrong username or password!");
        alert.setContentText("Please enter a valid username or password!");
        alert.showAndWait();
    }

    public static void userAlreadyExists() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERRORRRRRRRR!");
        alert.setHeaderText("User already exists!");
        alert.setContentText("Please enter another username!");
        alert.showAndWait();
    }

    public static void signUpSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign up success!");
        alert.setHeaderText("Sign up success!");
        alert.setContentText("Your account has been created successfully!");
        alert.showAndWait();
    }

    public static void signInSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign in success!");
        alert.setHeaderText("Sign in success!");
        alert.setContentText("Your account has been logged in successfully!");
        alert.showAndWait();
    }

    public static void missingInformation() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Missing Information!");
        alert.setHeaderText("Missing Information!");
        alert.setContentText("Please enter a valid information!");
        alert.showAndWait();
    }

    public static Optional<ButtonType> areYouSureAboutThat() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure about that?");
        return alert.showAndWait();
    }

    public static void wrongEmailPattern() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Wrong email pattern");
        alert.setHeaderText("Wrong email pattern");
        alert.setContentText("Please enter a valid email.");
        alert.showAndWait();
    }

}
