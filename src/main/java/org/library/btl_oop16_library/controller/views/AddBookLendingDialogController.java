package org.library.btl_oop16_library.controller.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.model.BookLoans;
import org.library.btl_oop16_library.utils.general.ApplicationAlert;
import org.library.btl_oop16_library.utils.database.BookLoanDBConnector;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AddBookLendingDialogController {
    BookLoanDBConnector bookLoanDBConnector = BookLoanDBConnector.getInstance();
    @FXML
    private TextField bookIDField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField userIdField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onConfirmButtonClick(ActionEvent event) {
        int quantity = Integer.parseInt(quantityField.getText());
        int bookAvailable = bookLoanDBConnector.getBookAvailable(bookIDField.getText());
        int bookLentAmount = bookLoanDBConnector.getBookLentAmount(userIdField.getText());

        if (quantityField.getText().isEmpty() || bookIDField.getText().isEmpty() || userIdField.getText().isEmpty()) {
            ApplicationAlert.missingInformation();
        }

        if (quantity < 0) {
            ApplicationAlert.invalidQuantity();
        }

        if (quantity > 5 || quantity + bookLentAmount > 5 || bookAvailable - quantity < 0) {
            ApplicationAlert.overMaxQuantity();

        } else {
            int userID = Integer.parseInt(userIdField.getText());
            int bookID = Integer.parseInt(bookIDField.getText());
            LocalDate startLocalDate = startDatePicker.getValue();
            LocalDate dueLocalDate = dueDatePicker.getValue();

            Date startDate = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date dueDate = Date.from(dueLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            BookLoans bookLoan = new BookLoans(userID, bookID, startDate, dueDate, quantity, "not returned");
            try {
                bookLoanDBConnector.addToDB(bookLoan);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        }
    }
}
