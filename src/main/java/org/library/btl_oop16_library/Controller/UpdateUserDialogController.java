package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.User;
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
    void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onConfirmButtonClick(ActionEvent event) {
        int id = Integer.parseInt(idField.getText());

        UserDBConnector userDBConnector = UserDBConnector.getInstance();
        User existingUser = userDBConnector.searchById(id);

        if (existingUser != null) {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();
            String email = emailField.getText();

            existingUser.setName(name);
            existingUser.setPhone(phone);
            existingUser.setAddress(address);
            existingUser.setEmail(email);

            boolean success = userDBConnector.updateUser(existingUser);

            if (success) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("Failed to update user.");
            }
        } else {
            System.out.println("User with ID " + id + " not found.");
        }
    }

}
