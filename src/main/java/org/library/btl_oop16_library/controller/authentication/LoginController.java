package org.library.btl_oop16_library.controller.authentication;

import eu.iamgio.animated.transition.AnimatedSwitcher;
import eu.iamgio.animated.transition.AnimationPair;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.library.btl_oop16_library.model.User;
import org.library.btl_oop16_library.services.HashPassword;
import org.library.btl_oop16_library.utils.general.ApplicationAlert;
import org.library.btl_oop16_library.utils.general.SessionManager;
import org.library.btl_oop16_library.utils.general.Motion;
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
    private MFXButton signInButton;

    @FXML
    private MFXButton signUpButton;

    @FXML
    private BorderPane mainPane;

    private AnimatedSwitcher switcher;


    @FXML
    public void signInOnClick(ActionEvent event) throws IOException {

        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            ApplicationAlert.emptyAccountOrPassword();
            return;
        } else {
            User user = UserDBConnector.getInstance().getUser(usernameField.getText(), HashPassword.hashPassword(passwordField.getText()));
            if (user != null) {
                SessionManager.getInstance().setCurrentUser(user);
                FXMLLoader loader = new FXMLLoader(getClass().getResource(MAINMENU_PATH));
                Pane node = loader.load();
                Motion.switchScene(rootPane,node);

            } else {
                ApplicationAlert.wrongUsernameOrPassword();
            }
        }
    }

    @FXML
    public void switchToSignUpScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(SIGNUP_PATH));
        Pane node = loader.load();
        Motion.switchScene(rootPane, node);
    }

    @FXML
    private void initialize() {
        signInButton.setDefaultButton(true);
        switcher = new AnimatedSwitcher(AnimationPair.zoom().setSpeed(2, .8));
        rootPane.getChildren().add(switcher);
        switcher.setChild(mainPane);
    }
}