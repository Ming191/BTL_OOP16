package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Model.BookLending;
import org.library.btl_oop16_library.Util.DatabaseConnector;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class CatalogViewController {
    private List<BookLending> history;

    @FXML
    private TableView<BookLending> table;

    @FXML
    private TableColumn<BookLending, Date> startDateCol;

    @FXML
    private TableColumn<BookLending, String> statusCol;

    @FXML
    private TableColumn<BookLending, Integer> userIdCol;

    @FXML
    private TableColumn<BookLending, Integer> amountCol;

    @FXML
    private TableColumn<BookLending, Integer> bookIdCol;

    @FXML
    private TableColumn<BookLending, Date> dueDateCol;

    @FXML
    private TableColumn<BookLending, Integer> idCol;

    @FXML
    private Button lendBookButton;


    @FXML
    void lendBookButtonOnClick(ActionEvent event) throws IOException {
        Stage bookLendingStage = new Stage();
        bookLendingStage.setResizable(false);
        bookLendingStage.initModality(Modality.APPLICATION_MODAL);
        bookLendingStage.setTitle("Lending Book");
        System.out.println("Lending Book button clicked.");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/AddBookLendingDialog.fxml"));
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

        loadHistory();

    }

    private void loadHistory() {
        history = DatabaseConnector.loadBookLendingFromDB();
        table.getItems().addAll(history);
    }

    private void refreshHistory() {
        table.getItems().clear();
        history.clear();
        loadHistory();
    }

}
