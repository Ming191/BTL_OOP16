package org.library.btl_oop16_library.controller.views.settings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.library.btl_oop16_library.utils.general.ApplicationAlert;
import org.library.btl_oop16_library.utils.general.ContainerSwitcher;
import org.library.btl_oop16_library.utils.general.SessionManager;
import org.library.btl_oop16_library.utils.database.UserDBConnector;

import java.io.IOException;

import static org.library.btl_oop16_library.utils.general.GlobalVariables.SETTINGS_PATH;

public class ChangePasswordViewController {
    @FXML
    private Button confirmButton;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private TextField currentPasswordField;

    @FXML
    private TextField newPasswordField;

    @FXML
    private VBox changePasswordBox;

    @FXML
    private void confirmPasswordChange (ActionEvent event) {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            ApplicationAlert.missingInformation();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            ApplicationAlert.passwordMismatch();
            return;
        }

        if (!SessionManager.getInstance().getCurrentUser().getPassword().equals(currentPassword)) {
            ApplicationAlert.wrongPassword();
            return;
        }

        if (newPassword.length() < 8 ||
                !newPassword.matches(".*[A-Z].*") ||
                !newPassword.matches(".*[a-z].*") ||
                !newPassword.matches(".*\\d.*")) {
            ApplicationAlert.weakPassword();
            return;
        }

        boolean success = UserDBConnector.getInstance().updatePassword(SessionManager.getInstance().getCurrentUser().getId(), newPassword);

        if (success) {
            ApplicationAlert.updateSuccess();
        } else {
            ApplicationAlert.notFound();
        }
    }

    @FXML
    private void cancelChange (ActionEvent event) {
        ContainerSwitcher.switchView(changePasswordBox, SETTINGS_PATH, "settings");
    }
}