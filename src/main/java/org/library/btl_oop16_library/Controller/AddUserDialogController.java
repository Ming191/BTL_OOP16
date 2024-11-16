package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.Account;
import org.library.btl_oop16_library.Util.AccountDBConnector;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Util.UserDBConnector;

public class AddUserDialogController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField phoneField;

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
        String userName = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();

        UserDBConnector userDBConnector = UserDBConnector.getInstance();
        userDBConnector.addUserAndAccount(new User(name,email,phone,address), new Account(userName,password,false));

        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
}
