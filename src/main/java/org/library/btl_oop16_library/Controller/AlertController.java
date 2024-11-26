package org.library.btl_oop16_library.Controller;

import atlantafx.base.theme.Styles;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class AlertController {

    @FXML
    private Label messageLabel;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label titleLabel;

    @FXML
    private FontIcon icon;

    @FXML
    private AnchorPane mainPane;

    private boolean isConfirmed = false;

    public AnchorPane getMainPane() {
        return mainPane;
    }

    @FXML
    void onOkButtonClick(ActionEvent event) {
        isConfirmed = true;
        closeAlert();
    }

    @FXML
    void onCancelButtonClick(ActionEvent event) {
        isConfirmed = false;
        closeAlert();
    }

    private void closeAlert() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setIcon(String iconPath) {
        icon.setIconLiteral(iconPath);
    }

    public void setShowCancelButton(boolean showCancelButton) {
        cancelButton.setVisible(showCancelButton);
    }

    @FXML
    void initialize() {
        okButton.setDefaultButton(true);
        cancelButton.setCancelButton(true);
    }
}