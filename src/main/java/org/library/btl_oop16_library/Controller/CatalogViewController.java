package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;
import org.library.btl_oop16_library.Model.BookLoans;
import org.library.btl_oop16_library.Util.BookLoanDBConnector;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CatalogViewController {
    private List<BookLoans> history;

    @FXML
    private TableView<BookLoans> table;

    @FXML
    private TableColumn<BookLoans, Date> startDateCol;

    @FXML
    private TableColumn<BookLoans, String> statusCol;

    @FXML
    private TableColumn<BookLoans, Integer> userIdCol;

    @FXML
    private TableColumn<BookLoans, Integer> amountCol;

    @FXML
    private TableColumn<BookLoans, Integer> bookIdCol;

    @FXML
    private TableColumn<BookLoans, Date> dueDateCol;

    @FXML
    private TableColumn<BookLoans, Integer> idCol;

    @FXML
    private Button lendBookButton;


    @FXML
    void lendBookButtonOnClick(ActionEvent event) throws IOException {
        Stage bookLendingStage = new Stage();
        bookLendingStage.setResizable(false);
        bookLendingStage.initModality(Modality.APPLICATION_MODAL);
        bookLendingStage.setTitle("Lending Book");
        System.out.println("Lending Book button clicked.");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/AddBookLoansDialog.fxml"));
        Parent root = loader.load();

        bookLendingStage.setScene(new Scene(root));
        bookLendingStage.showAndWait();

        refreshHistory();
    }

    @FXML
    void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        formatDateColumn(startDateCol);
        formatDateColumn(dueDateCol);

        try {
            loadHistory();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private void loadHistory() throws SQLException {
        BookLoanDBConnector bookLoanDBConnector = new BookLoanDBConnector();
        history = bookLoanDBConnector.importFromDB();
        table.getItems().addAll(history);
    }

    private void refreshHistory() {
        table.getItems().clear();
        history.clear();
        try{
            loadHistory();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void formatDateColumn(TableColumn<BookLoans, Date> column) {
        column.setCellFactory(param -> new TableCell<>() {
            private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(format.format(item));
                }
            }
        });
    }
}
