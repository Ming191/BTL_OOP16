package org.library.btl_oop16_library.controller.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.model.Book;
import org.library.btl_oop16_library.model.BookLoans;
import org.library.btl_oop16_library.utils.general.ApplicationAlert;
import org.library.btl_oop16_library.utils.database.BookLoanDBConnector;
import org.library.btl_oop16_library.utils.general.SessionManager;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PreorderDialogController {
    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField quantityField;

    private Book currentBook;

    @FXML
    void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onConfirmButtonClick(ActionEvent event) {
        int bookId = currentBook.getId();
        int quantity = Integer.parseInt(quantityField.getText());
        int bookAvailable = BookLoanDBConnector.getInstance().getBookAvailable(String.valueOf(bookId));

        if (quantity <= 0 || quantity > bookAvailable) {
            ApplicationAlert.invalidQuantity();
            return;
        }

        LocalDate startLocalDate = LocalDate.now();
        LocalDate dueLocalDate = startLocalDate.plusDays(3);

        Date startDate = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dueDate = Date.from(dueLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        BookLoans bookLoan = new BookLoans(SessionManager.getInstance().getCurrentUser().getId(),
                           bookId, startDate, dueDate, quantity, "pre-ordered");
        try {
            BookLoanDBConnector.getInstance().addToDB(bookLoan);
            ApplicationAlert.preorderSuccess();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    public void setCurrentBook(Book currentBook) {
        this.currentBook = currentBook;
        System.out.printf(String.valueOf(currentBook.getId()));
    }
}
