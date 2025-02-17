package org.library.btl_oop16_library.controller.views.settings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.library.btl_oop16_library.model.User;
import org.library.btl_oop16_library.utils.general.ApplicationAlert;
import org.library.btl_oop16_library.utils.general.ContainerSwitcher;
import org.library.btl_oop16_library.utils.general.SessionManager;
import org.library.btl_oop16_library.utils.database.UserDBConnector;

import static org.library.btl_oop16_library.utils.general.GlobalVariables.SETTINGS_PATH;
import static org.library.btl_oop16_library.utils.general.GlobalVariables.EMAIL_REGEX;

public class UpdateUserController {

    @FXML
    private TextField addressField;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneField;

    @FXML
    private Button cancelButton;

    @FXML
    VBox updateInfoBox;

    @FXML
    private void initialize() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        nameField.setText(currentUser.getName());
        emailField.setText(currentUser.getEmail());
        phoneField.setText(currentUser.getPhoneNumber());
        addressField.setText(currentUser.getAddress());
    }

    @FXML
    private void updateInfo(ActionEvent event) {
        String newName = nameField.getText();
        String newEmail = emailField.getText();
        String newAddress = addressField.getText();
        String newPhone = phoneField.getText();
        String passwordCheck = passwordField.getText();

        if (newName.isEmpty() || newEmail.isEmpty() || newAddress.isEmpty() || newPhone.isEmpty() || passwordCheck.isEmpty()) {
            ApplicationAlert.missingInformation();
            return;
        }

        if (!emailField.getText().matches(EMAIL_REGEX)) {
            ApplicationAlert.wrongEmailPattern();
            return;
        }

        String phoneNumber = phoneField.getText();
        if (phoneNumber.length() != 10 || !phoneNumber.matches("\\d+") || !phoneNumber.startsWith("0")) {
            ApplicationAlert.invalidPhoneNumber();
            return;
        }

        if (!passwordCheck.equals(SessionManager.getInstance().getCurrentUser().getPassword())) {
            ApplicationAlert.wrongPassword();
            return;
        }

        SessionManager.getInstance().getCurrentUser().setName(newName);
        SessionManager.getInstance().getCurrentUser().setEmail(newEmail);
        SessionManager.getInstance().getCurrentUser().setAddress(newAddress);
        SessionManager.getInstance().getCurrentUser().setPhoneNumber(newPhone);

        boolean result = ApplicationAlert.areYouSureAboutThat();
        if (result) {
            UserDBConnector.updateUserInfo(SessionManager.getInstance().getCurrentUser());
            ApplicationAlert.updateSuccess();
            return;
        }
        else {
            System.out.println("User cancelled the sign-up process.");
        }
    }

    @FXML
    private void onCancelButtonClick() {
        ContainerSwitcher.switchView(updateInfoBox, SETTINGS_PATH, "settings");
    }

}