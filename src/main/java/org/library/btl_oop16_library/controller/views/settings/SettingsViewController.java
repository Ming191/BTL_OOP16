package org.library.btl_oop16_library.controller.views.settings;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.library.btl_oop16_library.utils.general.ContainerSwitcher;

import static org.library.btl_oop16_library.utils.general.GlobalVariables.*;

public class SettingsViewController {
    @FXML
    private VBox settingsBox;

    @FXML
    private Button changePasswordButton;

    @FXML
    private Button updateInformationButton;

    @FXML
    private Button changeThemeButton;

    @FXML
    private void viewChangePasswordView(ActionEvent event) {
        ContainerSwitcher.switchView(settingsBox, CHANGE_PASSWORD_PATH, "settings");
    }

    @FXML
    private void viewUpdateInformationView(ActionEvent event) {
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