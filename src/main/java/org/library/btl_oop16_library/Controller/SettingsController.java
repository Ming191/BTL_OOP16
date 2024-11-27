package org.library.btl_oop16_library.Controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.library.btl_oop16_library.Model.User;

import java.io.IOException;
import java.util.Objects;

import static org.library.btl_oop16_library.Util.GlobalVariables.isLightTheme;

public class SettingsController {
    @FXML
    private Button changePasswordButton;

    @FXML
    private TextField headBar;

    @FXML
    private Button updateInformationButton;

    @FXML
    private Button changeThemeButton;

    @FXML
    private BorderPane mainPane;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void viewChangePasswordView(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/ChangePasswordView.fxml"));
        Pane pane = loader.load();
        ChangePasswordViewController controller = loader.getController();

        controller.setCurrentUser(currentUser);
        mainPane.setCenter(pane);
        System.out.println(currentUser.getRole());
    }

    @FXML
    private void viewUpdateInformationView(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/UpdateInforForUser.fxml"));
        Pane pane = loader.load();
        UpdateUserDialogController updateUserDialogController = loader.getController();
        updateUserDialogController.setCurrentUser(currentUser);
        updateUserDialogController.setMainPane(mainPane);
        mainPane.setCenter(pane);
    }

    @FXML
    private void changeTheme(ActionEvent event) {
        if(isLightTheme) {
            System.out.printf("changed to dark theme\n");
            isLightTheme = false;
            Application.setUserAgentStylesheet(Objects.requireNonNull(getClass().getResource("/css/nord-dark.css")).toExternalForm());
        }
        else {
            System.out.printf("changed to light theme\n");
            isLightTheme = true;
            Application.setUserAgentStylesheet(Objects.requireNonNull(getClass().getResource("/css/nord-light.css")).toExternalForm());
        }
    }

}
