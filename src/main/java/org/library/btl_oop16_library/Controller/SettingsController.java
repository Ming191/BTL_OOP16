package org.library.btl_oop16_library.Controller;

import eu.iamgio.animated.transition.AnimatedThemeSwitcher;
import eu.iamgio.animated.transition.animations.clip.CircleClipOut;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Util.ContainerSwitcher;

import java.io.IOException;
import java.util.Objects;

import static org.library.btl_oop16_library.Util.GlobalVariables.*;

public class SettingsController {
    @FXML
    private VBox settingsBox;

    @FXML
    private Button changePasswordButton;

    @FXML
    private TextField headBar;

    @FXML
    private Button updateInformationButton;

    @FXML
    private Button changeThemeButton;

    @FXML
    private void viewChangePasswordView(ActionEvent event) throws IOException {
        ContainerSwitcher.switchView(settingsBox, CHANGE_PASSWORD_PATH, "settings");
    }

    @FXML
    private void viewUpdateInformationView(ActionEvent event) throws IOException {
        ContainerSwitcher.switchView(settingsBox, UPDATE_INFO_PATH, "settings");
    }

    @FXML
    private void changeTheme(ActionEvent event) {
        isLightTheme = !isLightTheme;
        String styleSheetPath = (isLightTheme ? LIGHT_THEME : DARK_THEME);
        Application.setUserAgentStylesheet(styleSheetPath);
    }

    @FXML
    void initialize() {

    }
}