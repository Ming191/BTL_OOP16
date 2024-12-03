package org.library.btl_oop16_library.controller.dialogs;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.model.User;
import org.library.btl_oop16_library.utils.general.ApplicationAlert;
import org.library.btl_oop16_library.utils.database.UserDBConnector;

import static org.library.btl_oop16_library.utils.general.GlobalVariables.EMAIL_REGEX;

public class UpdateUserDialogForAdminController {

    @FXML
    private TextField addressField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private ChoiceBox<String> roleBox;

    private User selectedUser;

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    @FXML
    public void initialize() {
        roleBox.getItems().addAll("user", "admin");
        roleBox.setValue("user");

        Platform.runLater(() -> {
           nameField.setText(selectedUser.getName());
           emailField.setText(selectedUser.getEmail());
           phoneField.setText(selectedUser.getPhoneNumber());
           addressField.setText(selectedUser.getAddress());
        });
    }

    @FXML
    private void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onConfirmButtonClick(ActionEvent event) {
        try {
            UserDBConnector userDBConnector = UserDBConnector.getInstance();

            if (selectedUser != null) {
                String newName = nameField.getText();
                String newPhone = phoneField.getText();
                String newAddress = addressField.getText();
                String newEmail = emailField.getText();
                String newRole = roleBox.getValue();

                if (!newEmail.matches(EMAIL_REGEX)) {
                    ApplicationAlert.wrongEmailPattern();
                    return;
                }

                String phoneNumber = phoneField.getText();
                if (phoneNumber.length() != 10 || !phoneNumber.matches("\\d+") || !phoneNumber.startsWith("0")) {
                    ApplicationAlert.invalidPhoneNumber();
                    return;
                }

                selectedUser.setName(newName);
                selectedUser.setPhoneNumber(newPhone);
                selectedUser.setAddress(newAddress);
                selectedUser.setEmail(newEmail);
                selectedUser.setRole(newRole);

                boolean success = userDBConnector.updateUserForAdmin(selectedUser);

                if (success) {
                    System.out.println("User updated successfully.");
                    ApplicationAlert.updateSuccess();
                } else {
                    System.out.println("Failed to update user.");
                }
            } else {
                System.out.println("User with ID " + selectedUser.getId() + " not found.");
                ApplicationAlert.notFound();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format: " + e.getMessage());
        }
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
}
