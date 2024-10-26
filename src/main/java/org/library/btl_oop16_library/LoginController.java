package org.library.btl_oop16_library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField accountField;

    @FXML
    private TextField passwordField;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpButton;

    @FXML
    public void signInOnClick(ActionEvent event) throws IOException {

        if (accountField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            ApplicationAlert.emptyAccountOrPassword();
            return;
        } else {
            User user = DatabaseConnector.checkUser(accountField.getText(), passwordField.getText());

            if(user != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
                Scene mainMenuScene = new Scene(loader.load());
                Stage stage = (Stage) signInButton.getScene().getWindow();
                Transtition.fadeTransition(stage, signInButton.getScene(),mainMenuScene);
            } else {
                ApplicationAlert.wrongUsernameOrPassword();
            }
        }
    }

    @FXML
    public void switchToSignUpScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        Scene signUpScene = new Scene(loader.load());
        Transtition.fadeTransition((Stage) signUpButton.getScene().getWindow(), signUpButton.getScene(), signUpScene);
    }
}