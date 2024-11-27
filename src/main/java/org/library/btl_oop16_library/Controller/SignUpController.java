package org.library.btl_oop16_library.Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Util.*;
import org.library.btl_oop16_library.Model.User;

import java.io.IOException;
import java.sql.SQLException;

import static org.library.btl_oop16_library.Util.GlobalVariables.emailRegex;

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
        Transition.fadeTransition((Stage) signInButton.getScene().getWindow(), signInButton.getScene(), loginScene);
    }

    @FXML
    public void signUpOnClick(ActionEvent actionEvent) throws SQLException {
        if(nameField.getText().isEmpty() || usernameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                passwordField.getText().isEmpty() || phoneField.getText().isEmpty() || addressField.getText().isEmpty()) {
            ApplicationAlert.missingInformation();
            return;
        }

        if (!emailField.getText().matches(emailRegex)) {
            ApplicationAlert.wrongEmailPattern();
            return;
        }


        if (UserDBConnector.isAlreadyExist(usernameField.getText())) {
            ApplicationAlert.userAlreadyExists();
            return;
        }

        boolean result = ApplicationAlert.areYouSureAboutThat();
        if (result) {
            User newUser = new User(nameField.getText(), emailField.getText(), phoneField.getText(), addressField.getText(), usernameField.getText(), passwordField.getText());
            UserDBConnector.getInstance().addToDB(newUser);
            ActivitiesDBConnector activitiesDBConnector = new ActivitiesDBConnector();
            activitiesDBConnector.logActivity(String.format("New user signed up: %s", newUser.getName()));
            ApplicationAlert.signUpSuccess();
            return;
        }
        else {
            System.out.println("User cancelled the sign-up process.");
        }


    }
}

