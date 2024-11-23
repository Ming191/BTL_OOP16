package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.library.btl_oop16_library.Model.User;

import java.io.IOException;

public class SettingsController {
    @FXML
    private Button changePasswordButton;

    @FXML
    private TextField headBar;

    @FXML
    private BorderPane mainPane;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    void viewChangePasswordView(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/ChangePasswordView.fxml"));
        Pane pane = loader.load();
        ChangePasswordViewController controller = loader.getController();
        controller.setCurrentUser(currentUser);
        mainPane.setCenter(pane);
        System.out.println(currentUser.getRole());
    }
}
