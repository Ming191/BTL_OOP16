package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.BookLending;
import org.library.btl_oop16_library.Util.DatabaseConnector;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddBookLendingDialogController {

    @FXML
    private TextField bookIDField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField dueDateField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField startDateField;

    @FXML
    private TextField userIdField;

    @FXML
    void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onConfirmButtonClick(ActionEvent event) {
        int userID = Integer.parseInt(userIdField.getText());
        int bookID = Integer.parseInt(bookIDField.getText());
        int quantity = Integer.parseInt(quantityField.getText());
        java.util.Date dueDate = null;
        java.util.Date startDate = null;

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
             startDate = df.parse(startDateField.getText());
             dueDate = df.parse(dueDateField.getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        java.sql.Date startDateSQL = new java.sql.Date(startDate.getTime());
        java.sql.Date dueDateSQL = new java.sql.Date(dueDate.getTime());
        BookLending bookLending = new BookLending(
                userID, bookID,
                startDateSQL,
                dueDateSQL,
                quantity);
        try {
            DatabaseConnector.addBookLendingToDB(bookLending);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();

    }
}
