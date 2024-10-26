package org.library.btl_oop16_library.Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.DatabaseConnector;
import org.library.btl_oop16_library.Util.Transtition;
import org.library.btl_oop16_library.Model.User;

import java.io.IOException;

public class SignUpController {
    @FXML
    private TextField accountField;

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
        User newUser = new User(nameField.getText(), emailField.getText(), accountField.getText(), passwordField.getText());
        DatabaseConnector.addUser(newUser);
        ApplicationAlert.signUpSuccess();
    }
}
