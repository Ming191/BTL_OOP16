package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.BookLoans;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Services.EmailAPI;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.BookLoanDBConnector;
import org.library.btl_oop16_library.Util.SessionManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.SQLException;
import java.util.List;

public class ServicesViewController {
    private List<BookLoans> history;
    private final BookLoanDBConnector bookLoanDBConnector = BookLoanDBConnector.getInstance();
    boolean canLendBook = true;
    boolean canPreorder = true;

    @FXML
    private Button mailButton;

    @FXML
    private TableView<BookLoans> table;

    @FXML
    private TableColumn<BookLoans, Date> startDateCol;

    @FXML
    private TableColumn<BookLoans, String> statusCol;

    @FXML
    private TableColumn<BookLoans, String> userNameCol;

    @FXML
    private TableColumn<BookLoans, Integer> amountCol;

    @FXML
    private TableColumn<BookLoans, String> bookTitleCol;

    @FXML
    private TableColumn<BookLoans, Date> dueDateCol;

    @FXML
    private TableColumn<BookLoans, Integer> idCol;

    @FXML
    private Button lendBookButton;

    @FXML
    private Button returnBookButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button importButton;

    @FXML
    private TextField searchField;

    @FXML
    private ChoiceBox<String> typeSearchBox;

    public void setCurrentUser() {
        if (!"admin".equalsIgnoreCase(SessionManager.getInstance().getCurrentUser().getRole())) {
            canLendBook = bookLoanDBConnector.canLendBook(SessionManager.getInstance().getCurrentUser(), 20);
            canPreorder = bookLoanDBConnector.canPreorderBook(SessionManager.getInstance().getCurrentUser());
            userNameCol.setVisible(false);
            returnBookButton.setVisible(false);
            lendBookButton.setVisible(false);
            importButton.setVisible(false);
            mailButton.setVisible(false);
            exportButton.setVisible(false);
        }
        try {
            initializeBaseOnUser();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void lendBookButtonOnClick(ActionEvent event) throws IOException {
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
            Image favicon = new Image(getClass().getResource("/img/logo_2min.png").toExternalForm())   ;
            bookLendingStage.getIcons().add(favicon);
            bookLendingStage.showAndWait();

        } else {
            System.out.println("Can not lend book");
            ApplicationAlert.canNotLendBook();
        }
        refreshHistory();
    }

    @FXML
    private void returnBookButtonOnClick(ActionEvent event) throws IOException {
        Stage bookReturnStage = new Stage();
        bookReturnStage.setResizable(false);
        bookReturnStage.initModality(Modality.APPLICATION_MODAL);
        bookReturnStage.setTitle("Return Book");
        System.out.println("Return Book button clicked.");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/ReturnBookLoansDialog.fxml"));
        Parent root = loader.load();
        bookReturnStage.setScene(new Scene(root));
        Image favicon = new Image(getClass().getResource("/img/logo_2min.png").toExternalForm())   ;
        bookReturnStage.getIcons().add(favicon);
        bookReturnStage.showAndWait();

        refreshHistory();
    }

    @FXML
    private void searchButtonOnClick(ActionEvent event) throws IOException {
        String searchText = searchField.getText();
        List<BookLoans>  searchedBook = null;
        if (!searchText.isEmpty()) {
            if (SessionManager.getInstance().getCurrentUser().getRole().equalsIgnoreCase("admin")) {
                searchedBook = bookLoanDBConnector.searchBookFromDB(searchText);
            } else {
                searchedBook = bookLoanDBConnector.searchBookFromDBForUser(searchText, SessionManager.getInstance().getCurrentUser());
            }
        }
        table.getItems().clear();

        if (searchedBook != null) {
            table.getItems().addAll(searchedBook);
        } else {
            refreshHistory();
        }
    }

    @FXML
    private void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        formatDateColumn(startDateCol);
        formatDateColumn(dueDateCol);

        typeSearchBox.getItems().addAll("id", "name", "bookTitle", "startDate", "dueDate", "status");
        typeSearchBox.setValue("id");

        setCurrentUser();
    }

    private void initializeBaseOnUser() throws SQLException {
        if (!"admin".equalsIgnoreCase(SessionManager.getInstance().getCurrentUser().getRole())) {
            loadHistoryForUser();
        } else {
            loadHistory();
        }
    }

    private void loadHistory() throws SQLException {
        bookLoanDBConnector.updateBookLoan();
        history = bookLoanDBConnector.importFromDB();
        table.getItems().addAll(history);
    }

    private void loadHistoryForUser() throws SQLException {
        bookLoanDBConnector.updateBookLoan();
        history = bookLoanDBConnector.importFromDBForUser(SessionManager.getInstance().getCurrentUser());
        table.getItems().addAll(history);
    }

    private void refreshHistory() {
        table.getItems().clear();
        history.clear();
        try {
            initializeBaseOnUser();
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
    private void exportOnClick() {
        bookLoanDBConnector.exportToExcel();
    }

    @FXML
    private void importOnClick() throws SQLException {
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

    @FXML
    private void mailButtonOnClick() {
        BookLoanDBConnector dbConnector = BookLoanDBConnector.getInstance();
        List<String[]> overdueEmails = dbConnector.getOverdueUserEmails();

        for (String[] details : overdueEmails) {
            String email = details[0];
            String userName = details[1];
            String bookTitle = details[2];
            String dueDate = details[3];

            EmailAPI.sendEmail(email, userName, bookTitle, dueDate);
        }
    }
}
