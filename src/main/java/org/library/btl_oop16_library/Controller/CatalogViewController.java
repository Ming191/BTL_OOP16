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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.BookLoans;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.BookLoanDBConnector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.SQLException;
import java.util.List;

public class CatalogViewController {
    private List<BookLoans> history;
    private final BookLoanDBConnector bookLoanDBConnector = BookLoanDBConnector.getInstance();
    private User currentUser;
    boolean canLendBook;
    boolean canPreorder;

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
    private Button returnBookButton;

    @FXML
    private Button preorderButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button importButton;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        canLendBook = bookLoanDBConnector.canLendBook(currentUser, 20);
        canPreorder = bookLoanDBConnector.canPreorderBook(currentUser);
        if (currentUser != null && !"admin".equalsIgnoreCase(currentUser.getRole())) {
            userIdCol.setVisible(false);
            returnBookButton.setVisible(false);
            if (!canPreorder) {
                preorderButton.setVisible(false);
            }
        }
        try {
            initializeBaseOnUser(currentUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void lendBookButtonOnClick(ActionEvent event) throws IOException {
        if (canLendBook) {
            System.out.println("Can lend book");
            Stage bookLendingStage = new Stage();
            bookLendingStage.setResizable(false);
            bookLendingStage.initModality(Modality.APPLICATION_MODAL);
            bookLendingStage.setTitle("Lending Book");
            System.out.println("Lending Book button clicked.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/AddBookLoansDialog.fxml"));
            Parent root = loader.load();
            bookLendingStage.setScene(new Scene(root));
            bookLendingStage.showAndWait();

        } else {
            System.out.println("Can not lend book");
            ApplicationAlert.canNotLendBook();
        }
        refreshHistory();
    }

    @FXML
    void preorderButtonOnClick(ActionEvent event) throws IOException {
        if (canPreorder) {
            System.out.println("Can preorder book");
            Stage preorderStage = new Stage();
            preorderStage.setResizable(false);
            preorderStage.initModality(Modality.APPLICATION_MODAL);
            preorderStage.setTitle("Lending Book");
            System.out.println("Lending Book button clicked.");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/PreorderDialog.fxml"));
            Parent root = loader.load();
            preorderStage.setScene(new Scene(root));
            preorderStage.showAndWait();

        } else {
            System.out.println("Can not preorder book");
            ApplicationAlert.canNotLendBook();
        }
        refreshHistory();
    }

    @FXML
    void returnBookButtonOnClick(ActionEvent event) throws IOException {
        Stage bookReturnStage = new Stage();
        bookReturnStage.setResizable(false);
        bookReturnStage.initModality(Modality.APPLICATION_MODAL);
        bookReturnStage.setTitle("Return Book");
        System.out.println("Return Book button clicked.");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/ReturnBookLoansDialog.fxml"));
        Parent root = loader.load();
        bookReturnStage.setScene(new Scene(root));
        bookReturnStage.showAndWait();

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

    }

    private void initializeBaseOnUser(User currentUser) throws SQLException {
        if (currentUser != null && !"admin".equalsIgnoreCase(currentUser.getRole())) {
            loadHistoryForUser(currentUser);
        } else {
            loadHistory();
        }
    }

    private void loadHistory() throws SQLException {
        bookLoanDBConnector.updateBookLoan();
        history = bookLoanDBConnector.importFromDB();
        table.getItems().addAll(history);
    }

    private void loadHistoryForUser(User user) throws SQLException {
        history = bookLoanDBConnector.importFromDBForUser(user);
        table.getItems().addAll(history);
    }

    private void refreshHistory() {
        table.getItems().clear();
        history.clear();
        try {
            initializeBaseOnUser(currentUser);
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

    @FXML
    void exportOnClick() {
        bookLoanDBConnector.exportToExcel();
    }

    @FXML
    void importOnClick() throws SQLException {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter excelFilter = new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(excelFilter);

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            bookLoanDBConnector.importFromExcel(filePath);
        } else {
            System.out.println("No file selected.");
        }
        refreshHistory();
    }
}
