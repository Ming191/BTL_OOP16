package org.library.btl_oop16_library;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class LoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField account_text_field;

    @FXML
    private TextField password_text_field;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button sign_in_button;

    @FXML
    private Button sign_up_button;

    @FXML
    public void signInOnClick(ActionEvent event) throws IOException {
        if(account_text_field.getText().isEmpty() || password_text_field.getText().isEmpty()) {
            ApplicationAlert.emptyAccountOrPassword();
            return;
        } else {
            User user = DatabaseConnector.checkUser(account_text_field.getText(), password_text_field.getText());
            if(user != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
                Scene mainMenuScene = new Scene(loader.load());
                Stage stage = (Stage) sign_in_button.getScene().getWindow();
                Transtition.fadeTransition(stage, sign_in_button.getScene(),mainMenuScene);
            } else {
                ApplicationAlert.wrongUsernameOrPassword();
            }
        }
    }
}