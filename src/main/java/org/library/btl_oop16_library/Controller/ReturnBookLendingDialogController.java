package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Util.BookLoanDBConnector;
import org.library.btl_oop16_library.Util.DBConnector;

import java.sql.SQLException;

public class ReturnBookLendingDialogController {

    @FXML
    private TextField IDField;

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
        int ID = Integer.parseInt(IDField.getText());
        BookLoanDBConnector db = new BookLoanDBConnector();
        try {
            db.deleteFromDB(ID);
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
