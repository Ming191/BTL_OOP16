package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.SessionManager;
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


    private BorderPane mainPane;

    public void setMainPane(BorderPane mainPane) {
        this.mainPane = mainPane;
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

        if (!emailField.getText().matches(emailRegex)) {
            ApplicationAlert.wrongEmailPattern();
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
    private void onCancelButtonClick() throws IOException {
        if (mainPane != null) {
            mainPane.setCenter(null);
            mainPane.setTop(null);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/Settings.fxml"));
            Pane pane = (Pane) fxmlLoader.load();
            mainPane.setCenter(pane);
        }
    }

}