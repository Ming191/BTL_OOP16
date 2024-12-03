package org.library.btl_oop16_library.controller.authentication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.library.btl_oop16_library.model.User;
import org.library.btl_oop16_library.utils.general.ApplicationAlert;
import org.library.btl_oop16_library.utils.general.SessionManager;
import org.library.btl_oop16_library.utils.general.Animation;
import org.library.btl_oop16_library.utils.database.UserDBConnector;

import java.io.IOException;

import static org.library.btl_oop16_library.utils.general.GlobalVariables.MAINMENU_PATH;
import static org.library.btl_oop16_library.utils.general.GlobalVariables.SIGNUP_PATH;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpButton;

    @FXML
    private BorderPane mainPane;

    @FXML
    public void signInOnClick(ActionEvent event) throws IOException {

        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            ApplicationAlert.emptyAccountOrPassword();
            return;
        } else {
            User user = UserDBConnector.getInstance().getUser(usernameField.getText(), passwordField.getText());
            if (user != null) {
                SessionManager.getInstance().setCurrentUser(user);
                Animation.switchScene(rootPane,MAINMENU_PATH);

            } else {
                ApplicationAlert.wrongUsernameOrPassword();
            }
        }
    }

    @FXML
    public void switchToSignUpScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(SIGNUP_PATH));
        Scene signUpScene = new Scene(loader.load());
        Animation.fadeTransition((Stage) signUpButton.getScene().getWindow(), signUpButton.getScene(), signUpScene);
    }

    @FXML
    private void initialize() {
        signInButton.setDefaultButton(true);
    }
}