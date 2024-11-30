package org.library.btl_oop16_library.Controller;

import atlantafx.base.theme.Styles;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.SessionManager;
import org.library.btl_oop16_library.Util.Transition;
import org.library.btl_oop16_library.Util.UserDBConnector;

import java.io.IOException;
import java.util.Objects;

import static org.library.btl_oop16_library.Util.GlobalVariables.MAINMENU_PATH;

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
            if(user != null) {
                SessionManager.getInstance().setCurrentUser(user);
                Transition.switchScene(rootPane,MAINMENU_PATH);

            } else {
                ApplicationAlert.wrongUsernameOrPassword();
            }
        }
    }

    @FXML
    public void switchToSignUpScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/SignUp.fxml"));
        Scene signUpScene = new Scene(loader.load());
        Transition.fadeTransition((Stage) signUpButton.getScene().getWindow(), signUpButton.getScene(), signUpScene);
    }

    @FXML
    public void initialize() {
        signInButton.setDefaultButton(true);
    }
}