package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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
    private ImageView icon;

    private boolean isConfirmed = false;

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
        Image img = new Image(getClass().getResourceAsStream(iconPath));
        icon.setImage(img);
    }

    public void setShowCancelButton(boolean showCancelButton) {
        cancelButton.setVisible(showCancelButton);
    }
}
