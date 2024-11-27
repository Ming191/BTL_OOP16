package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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

    private BorderPane mainPane;

    public void setMainPane(BorderPane mainPane) {
        this.mainPane = mainPane;
    }

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

        boolean success = UserDBConnector.getInstance().updatePassword(SessionManager.getInstance().getCurrentUser().getId(), newPassword);

        if (success) {
            ApplicationAlert.updateSuccess();
        } else {
            ApplicationAlert.notFound();
        }
    }

    @FXML
    private void cancelChange (ActionEvent event) throws IOException {
        if (mainPane != null) {
            mainPane.setCenter(null);
            mainPane.setTop(null);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/Settings.fxml"));
            Pane pane = (Pane) fxmlLoader.load();
            mainPane.setCenter(pane);
        }
    }
}