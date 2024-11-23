package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.UserDBConnector;
import org.library.btl_oop16_library.Model.User;

public class ChangePasswordViewController {


    @FXML
    private Button confirmButton;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private TextField currentPasswordField;

    @FXML
    private TextField newPasswordField;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    void confirmPasswordChange (ActionEvent event) {
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

        if (!currentUser.getPassword().equals(currentPassword)) {
            ApplicationAlert.wrongPassword();
            return;
        }

        boolean success = UserDBConnector.getInstance().updatePassword(currentUser.getId(), newPassword);

        if (success) {
            ApplicationAlert.updateSuccess();
        } else {
            ApplicationAlert.notFound();
        }
    }
}
