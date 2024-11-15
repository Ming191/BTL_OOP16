package org.library.btl_oop16_library.Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.DatabaseConnector;
import org.library.btl_oop16_library.Util.Transtition;
import org.library.btl_oop16_library.Model.User;

import java.io.IOException;
import java.util.Optional;

public class SignUpController {
    @FXML
    private TextField addressField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpButton;

    public void switchToLoginScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/Login.fxml"));
        Scene loginScene = new Scene(loader.load());
        Transtition.fadeTransition((Stage) signInButton.getScene().getWindow(), signInButton.getScene(), loginScene);
    }

    @FXML
    public void signUpOnClick(ActionEvent actionEvent) {
        if(nameField.getText().isEmpty() || accountField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            ApplicationAlert.missingInformation();
            return;
        }

        if (DatabaseConnector.isUserExist(accountField.getText())) {
            ApplicationAlert.userAlreadyExists();
            return;
        }

        Optional<ButtonType> result = ApplicationAlert.areYouSureAboutThat();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            User newUser = new User(nameField.getText(), accountField.getText(), passwordField.getText(), emailField.getText());
            if(DatabaseConnector.addUserFromDB(newUser)) ApplicationAlert.signUpSuccess();
            else return;
        } else {
            System.out.println("User cancelled the sign-up process.");
        }

    }
}

