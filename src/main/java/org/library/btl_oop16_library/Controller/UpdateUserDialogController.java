package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.UserDBConnector;

public class UpdateUserDialogController {

    @FXML
    private TextField addressField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField emailField;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField userNameField;

    @FXML
    void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onConfirmButtonClick(ActionEvent event) {
        try {
            int id = Integer.parseInt(idField.getText());

            UserDBConnector userDBConnector = UserDBConnector.getInstance();
            User existingUser = userDBConnector.searchById(id);

            if (existingUser != null) {
                String newName = nameField.getText();
                String newPhone = phoneField.getText();
                String newAddress = addressField.getText();
                String newEmail = emailField.getText();
                String newUsername = userNameField.getText();
                String newPassword = passwordField.getText();

                if (!newEmail.matches("[a-zA-Z0-9._%+-]+@gmail\\.com")) {
                    ApplicationAlert.wrongEmailPattern();
                    return;
                }

                if (UserDBConnector.isAlreadyExist(newUsername)) {
                    ApplicationAlert.userAlreadyExists();
                    return;
                }
                existingUser.setName(newName);
                existingUser.setPhoneNumber(newPhone);
                existingUser.setAddress(newAddress);
                existingUser.setEmail(newEmail);
                existingUser.setUserName(newUsername);
                existingUser.setPassword(newPassword);

                boolean success = userDBConnector.updateUser(existingUser);

                if (success) {
                    System.out.println("User updated successfully.");
                    ApplicationAlert.updateSuccess();
                } else {
                    System.out.println("Failed to update user.");
                }
            } else {
                System.out.println("User with ID " + id + " not found.");
                ApplicationAlert.notFound();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format: " + e.getMessage());
        }
    }

}
