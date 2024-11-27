package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.BookLoans;
import org.library.btl_oop16_library.Util.BookLoanDBConnector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PreorderDialogController {

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
    private void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onConfirmButtonClick(ActionEvent event) {
        int userID = Integer.parseInt(userIdField.getText());
        int bookID = Integer.parseInt(bookIDField.getText());
        int quantity = Integer.parseInt(quantityField.getText());
        String start = startDateField.getText();
        String due = dueDateField.getText();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date startDate = formatter.parse(start);
            Date dueDate = formatter.parse(due);
            BookLoans bookLoan = new BookLoans(userID, bookID, startDate, dueDate, quantity, "pre-ordered");
            BookLoanDBConnector bookLoanDBConnector = BookLoanDBConnector.getInstance();
            try {
                bookLoanDBConnector.addToDB(bookLoan);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

}
