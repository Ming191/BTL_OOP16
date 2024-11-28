package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.BookLoans;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.BookLoanDBConnector;
import org.library.btl_oop16_library.Util.SessionManager;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PreorderDialogController {

    @FXML
    private TextField bookIDField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField quantityField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker dueDatePicker;

    private User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @FXML
    void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onConfirmButtonClick(ActionEvent event) {
        int bookId = Integer.parseInt(bookIDField.getText());
        int quantity = Integer.parseInt(quantityField.getText());
        int bookAvailable = BookLoanDBConnector.getInstance().getBookAvailable(bookIDField.getText());
        if (quantity <= 0 || quantity > bookAvailable) {
            ApplicationAlert.invalidQuantity();
            return;
        }
        LocalDate startLocalDate = startDatePicker.getValue();
        LocalDate dueLocalDate = dueDatePicker.getValue();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dueDate = Date.from(dueLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        BookLoans bookLoan = new BookLoans(SessionManager.getInstance().getCurrentUser().getId(),
                           bookId, startDate, dueDate, quantity, "pre-ordered");
        try {
            BookLoanDBConnector.getInstance().addToDB(bookLoan);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
}
