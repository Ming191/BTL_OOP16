package org.library.btl_oop16_library.controller.views;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.library.btl_oop16_library.model.BookLoans;
import org.library.btl_oop16_library.model.User;
import org.library.btl_oop16_library.services.EmailAPI;
import org.library.btl_oop16_library.services.ExcelAPI;
import org.library.btl_oop16_library.utils.general.ApplicationAlert;
import org.library.btl_oop16_library.utils.database.BookLoanDBConnector;
import org.library.btl_oop16_library.utils.general.SessionManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.SQLException;

import static org.library.btl_oop16_library.utils.general.GlobalVariables.ADD_BOOKLOANS_DIALOG_PATH;

public class ServicesViewController {
    private List<BookLoans> history;
    private final BookLoanDBConnector bookLoanDBConnector = BookLoanDBConnector.getInstance();
    boolean canLendBook = true;
    boolean canPreorder = true;
    BookLoans selectedBookLoan = null;

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
    private MFXButton lendBookButton;

    @FXML
    private MFXButton exportButton;

    @FXML
    private MFXButton importButton;

    @FXML
    private TextField searchField;

    @FXML
    private ChoiceBox<String> typeSearchBox;

    @FXML
    private MenuButton updateStatusMenu;

    @FXML
    private MenuItem notReturnedItem;

    @FXML
    private MenuItem preorderItem;

    @FXML
    private MenuItem returnedItem;

    @FXML
    private MenuItem cancelItem;

    public void setCurrentUser() {
        if (!"admin".equalsIgnoreCase(SessionManager.getInstance().getCurrentUser().getRole())) {
            canLendBook = bookLoanDBConnector.canLendBook(SessionManager.getInstance().getCurrentUser(), 20);
            canPreorder = bookLoanDBConnector.canPreorderBook(SessionManager.getInstance().getCurrentUser());
            userNameCol.setVisible(false);
            lendBookButton.setVisible(false);
            importButton.setVisible(false);
            mailButton.setVisible(false);
            exportButton.setVisible(false);
            updateStatusMenu.setVisible(false);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ADD_BOOKLOANS_DIALOG_PATH));
            Parent root = loader.load();
            bookLendingStage.setScene(new Scene(root));
            Image favicon = new Image(getClass().getResource("/img/logo.png").toExternalForm())   ;
            bookLendingStage.getIcons().add(favicon);
            bookLendingStage.showAndWait();

        } else {
            System.out.println("Can not lend book");
            ApplicationAlert.canNotLendBook();
        }
        refreshHistory();
    }

    @FXML
    private void searchButtonOnClick() {
        String searchText = searchField.getText();
        List<BookLoans>  searchedBook = null;
        User currentUser = SessionManager.getInstance().getCurrentUser();
        String type = typeSearchBox.getValue();
        if (!searchText.isEmpty()) {
            if (currentUser.getRole().equalsIgnoreCase("admin")) {
                searchedBook = bookLoanDBConnector.searchByAttributes(searchText, type);
            } else {
                searchedBook = bookLoanDBConnector.searchByAttributesForUser(searchText, type, currentUser);
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

        if (SessionManager.getInstance().getCurrentUser().getRole().equalsIgnoreCase("admin")) {
            typeSearchBox.getItems().addAll("id", "name", "bookTitle", "startDate", "dueDate", "status");
        } else {
            typeSearchBox.getItems().addAll("id", "bookTitle", "startDate", "dueDate", "status");
        }
        typeSearchBox.setValue("id");

        setCurrentUser();
        bookLoanDBConnector.updateBookLoan();

        PauseTransition searchPause = new PauseTransition(Duration.millis(500));
        searchPause.setOnFinished(event -> {
            searchButtonOnClick();
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchPause.stop();
            searchPause.playFromStart();
        });
    }

    @FXML
    private void handleOption(ActionEvent event) {
        selectedBookLoan = table.getSelectionModel().getSelectedItem();
        if (selectedBookLoan == null) {
            return;
        }
        System.out.println(selectedBookLoan.getBookTitle());
        MenuItem clickedItem = (MenuItem) event.getSource();
        String option = clickedItem.getText();
        bookLoanDBConnector.updateStatus(selectedBookLoan, option);
        refreshHistory();
    }

    private void initializeBaseOnUser() throws SQLException {
        if (!"admin".equalsIgnoreCase(SessionManager.getInstance().getCurrentUser().getRole())) {
            loadHistoryForUser();
        } else {
            loadHistory();
        }
    }

    private void loadHistory() {
        history = bookLoanDBConnector.importFromDB();
        table.getItems().addAll(history);
    }

    private void loadHistoryForUser() {
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
    private void importOnClick() {
        ExcelAPI.importExcel(BookLoanDBConnector.getInstance());
        refreshHistory();
    }

    @FXML
    private void mailButtonOnClick() {
        BookLoanDBConnector dbConnector = BookLoanDBConnector.getInstance();
        Map<String, List<String[]>> groupedEmails = dbConnector.getOverdueUserEmailsGrouped();

        Task<Void> emailTask = new Task<>() {
            @Override
            protected Void call() {
                EmailAPI.sendEmail(groupedEmails);
                return null;
            }
        };
        emailTask.setOnSucceeded(event -> ApplicationAlert.emailSent());
        emailTask.setOnFailed(event -> ApplicationAlert.emailFailed());

        new Thread(emailTask).start();
    }


}
