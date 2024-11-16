package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Util.BookDBConnector;

import java.sql.SQLException;

public class AddBookDialogController {

    @FXML
    private TextField authorField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField languageField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField titleField;

    @FXML
    private TextField typeField;

    @FXML
    void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onConfirmButtonClick(ActionEvent event) throws SQLException {
        String title = titleField.getText();
        String authorId =authorField.getText();
        String language = languageField.getText();
        String type = typeField.getText();
        int quantity;

        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        BookDBConnector db = BookDBConnector.getInstance();
        db.addToDB(new Book(title, authorId, language, type, quantity));
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

}
