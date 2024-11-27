package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.UserDBConnector;

import java.io.IOException;

import static org.library.btl_oop16_library.Util.GlobalVariables.emailRegex;

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

    @FXML
    private Button cancelButton;

    private User currentUser;

    private BorderPane mainPane;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setMainPane(BorderPane mainPane) {
        this.mainPane = mainPane;
    }

    @FXML
    private void updateInfor(ActionEvent event) {
        String newName = nameField.getText();
        String newEmail = emailField.getText();
        String newAddress = addressField.getText();
        String newPhone = phoneField.getText();
        String passwordCheck = passwordField.getText();

        if (newName.isEmpty() || newEmail.isEmpty() || newAddress.isEmpty() || newPhone.isEmpty() || passwordCheck.isEmpty()) {
            ApplicationAlert.missingInformation();
            return;
        }

        if (!emailField.getText().matches(emailRegex)) {
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

    @FXML
    private void onCancelButtonClick() {
        if (mainPane != null) {
            mainPane.setCenter(null);
            mainPane.setTop(null);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/Settings.fxml"));
            Pane root = null;
            try {
                root = loader.load();
                mainPane.setCenter(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
