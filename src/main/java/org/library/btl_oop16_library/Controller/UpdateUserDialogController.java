package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.UserDBConnector;

public class UpdateUserDialogController {

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

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    void updateInfor(ActionEvent event) {
        String newName = nameField.getText();
        String newEmail = emailField.getText();
        String newAddress = addressField.getText();
        String newPhone = phoneField.getText();
        String passwordCheck = passwordField.getText();

        if (newName.isEmpty() || newEmail.isEmpty() || newAddress.isEmpty() || newPhone.isEmpty() || passwordCheck.isEmpty()) {
            ApplicationAlert.missingInformation();
            return;
        }

        String gmailRegex = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
        if (!emailField.getText().matches(gmailRegex)) {
            ApplicationAlert.wrongEmailPattern();
            return;
        }

        if (!passwordCheck.equals(currentUser.getPassword())) {
            ApplicationAlert.wrongPassword();
            return;
        }

        currentUser.setName(newName);
        currentUser.setEmail(newEmail);
        currentUser.setAddress(newAddress);
        currentUser.setPhoneNumber(newPhone);


        boolean result = ApplicationAlert.areYouSureAboutThat();
        if (result) {
            UserDBConnector.updateUserInfor(currentUser);
            ApplicationAlert.updateSuccess();
            return;
        }
        else {
            System.out.println("User cancelled the sign-up process.");
        }
    }
}
