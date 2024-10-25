package org.library.btl_oop16_library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {
    @FXML
    private TextField account_text_field;

    @FXML
    private TextField name_text_field;

    @FXML
    private TextField email_text_field;

    @FXML
    private TextField password_text_field;

    @FXML
    private Button sign_in_button;

    @FXML
    private Button sign_up_button;

    public void switchToLoginScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene loginScene = new Scene(loader.load());
        Transtition.fadeTransition((Stage) sign_in_button.getScene().getWindow(), sign_in_button.getScene(), loginScene);
    }

    @FXML
    public void signUpOnClick(ActionEvent actionEvent) {
        if(name_text_field.getText().isEmpty() || account_text_field.getText().isEmpty() || email_text_field.getText().isEmpty() || password_text_field.getText().isEmpty()) {
            ApplicationAlert.missingInformation();
            return;
        }
        User newUser = new User(name_text_field.getText(), email_text_field.getText(), account_text_field.getText(), password_text_field.getText());
        DatabaseConnector.addUser(newUser);
        ApplicationAlert.signUpSuccess();
    }
}
