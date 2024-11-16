package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.Book;

public class DeleteBookDialogController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField quantityField;

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
        String title = titleField.getText();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        DatabaseConnector db = new DatabaseConnector();
        try {
            db.deleteBook(new Book(title, quantity));
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
