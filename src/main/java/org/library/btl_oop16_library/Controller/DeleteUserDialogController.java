package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Util.UserDBConnector;

public class DeleteUserDialogController {

    @FXML
    private TextField idField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;


    @FXML
    void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onConfirmButtonClick(ActionEvent event) {
        try {
            int id = Integer.parseInt(idField.getText());

            UserDBConnector.getInstance().deleteFromDB(id);
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
