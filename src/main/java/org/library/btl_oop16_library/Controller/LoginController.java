package org.library.btl_oop16_library.Controller;

import atlantafx.base.theme.Styles;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.Transition;
import org.library.btl_oop16_library.Util.UserDBConnector;

import java.io.IOException;

public class LoginController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpButton;

    @FXML
    private AnchorPane mainPane;

    @FXML
    public void signInOnClick(ActionEvent event) throws IOException {

        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            ApplicationAlert.emptyAccountOrPassword();
            return;
        } else {
            User user = UserDBConnector.getInstance().getUser(usernameField.getText(), passwordField.getText());
            if(user != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/MainMenu.fxml"));
                    Parent root = loader.load();
                    MainMenuController mainMenuController = loader.getController();
                    mainMenuController.setCurrentUser(user);
                    Scene mainMenuScene = new Scene(root);
                    Stage stage = (Stage) signInButton.getScene().getWindow();
                    Transition.fadeTransition(stage, signInButton.getScene(), mainMenuScene);

                } catch (IOException e) {
                    e.printStackTrace();
                }

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
        signUpButton.setStyle(Styles.BG_DEFAULT);
    }
}