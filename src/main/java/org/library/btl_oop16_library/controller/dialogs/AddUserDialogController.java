package org.library.btl_oop16_library.controller.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.model.User;
import org.library.btl_oop16_library.utils.general.ApplicationAlert;
import org.library.btl_oop16_library.utils.database.UserDBConnector;

import static org.library.btl_oop16_library.utils.general.GlobalVariables.EMAIL_REGEX;

public class AddUserDialogController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onConfirmButtonClick(ActionEvent event) {

        String name = nameField.getText();
        String userName = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String address = addressField.getText();

        if(nameField.getText().isEmpty() || usernameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                passwordField.getText().isEmpty() || phoneNumberField.getText().isEmpty() || addressField.getText().isEmpty()) {
            ApplicationAlert.missingInformation();
            return;
        }
        if (!email.matches(EMAIL_REGEX)) {
            ApplicationAlert.wrongEmailPattern();
            return;
        }
        UserDBConnector userDBConnector = UserDBConnector.getInstance();
        userDBConnector.addToDB(new User(name, email, phoneNumber, address, userName, password));
        ApplicationAlert.addSuccess();

        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
}
