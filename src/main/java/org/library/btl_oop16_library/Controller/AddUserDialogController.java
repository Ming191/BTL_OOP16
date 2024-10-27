package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Util.DatabaseConnector;
import org.library.btl_oop16_library.Model.User;

public class AddUserDialogController {
    @FXML
    private TextField accountField;

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
    void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onConfirmButtonClick(ActionEvent event) {

        String name = nameField.getText();
        String account = accountField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();

        DatabaseConnector db = new DatabaseConnector();
        db.addUserFromDB(new User(name, account, password, email));
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
}
