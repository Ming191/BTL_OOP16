package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SettingsController {
    @FXML
    private Button changePasswordButton;

    @FXML
    private TextField headBar;

    @FXML
    private BorderPane mainPane;

    @FXML
    void viewChangePasswordView(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane pane = loader.load(getClass().getResource("/org/library/btl_oop16_library/view/ChangePasswordView.fxml"));
        mainPane.setCenter(pane);
    }
}
