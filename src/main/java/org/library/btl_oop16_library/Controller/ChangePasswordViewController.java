package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.SessionManager;
import org.library.btl_oop16_library.Util.UserDBConnector;
import org.library.btl_oop16_library.Model.User;

import java.io.IOException;

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
    private void cancelChange (ActionEvent event) throws IOException {
        Scene scene = changePasswordBox.getScene();
        VBox settings = (VBox) scene.lookup("#settings");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/Settings.fxml"));
        VBox box = loader.load();

        settings.getChildren().setAll(box);
    }
}