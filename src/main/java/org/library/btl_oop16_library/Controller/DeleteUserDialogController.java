package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Util.DatabaseConnector;
import org.library.btl_oop16_library.Model.User;

public class DeleteUserDialogController {

    @FXML
    private TextField accountField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField emailField;

    @FXML
    void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onConfirmButtonClick(ActionEvent event) {
        String account = accountField.getText();
        String email = emailField.getText();

        DatabaseConnector db = new DatabaseConnector();
        try {
            db.deleteUser(new User(account, email));
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
