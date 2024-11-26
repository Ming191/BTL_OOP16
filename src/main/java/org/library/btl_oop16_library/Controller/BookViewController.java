package org.library.btl_oop16_library.Controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.BookDBConnector;

public class BookViewController {
    private static final BookDBConnector db = BookDBConnector.getInstance();
    private Book selectedBook = null;

    @FXML
    private TableColumn<Book, String> authorCol;

    @FXML
    private TableColumn<Book, String> availableCol;

    @FXML
    private TableColumn<Book, Integer> idCol;

    @FXML
    private TableColumn<Book, String> languageCol;


    @FXML
    private Button addBookButton;

    @FXML
    private Button deleteBookButton;

    @FXML
    private TableView<Book> table;

    @FXML
    private TableColumn<Book, String> titleCol;

    @FXML
    private TableColumn<Book, String> category;

    @FXML
    private TextField searchField;

    @FXML
    private Button viewDetailsButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button importButton;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        initializeRoleBasedAccess();
    }

    private void initializeRoleBasedAccess() {
        if (currentUser != null && !"admin".equalsIgnoreCase(currentUser.getRole())) {
            addBookButton.setDisable(true);
            deleteBookButton.setDisable(true);
            addBookButton.setVisible(false);
            deleteBookButton.setVisible(false);
        }
    }


    @FXML
    void addBookButtonOnClick() throws IOException, SQLException {
        Stage addBookStage = new Stage();
        addBookStage.setResizable(false);
        addBookStage.initModality(Modality.APPLICATION_MODAL);
        addBookStage.setTitle("Add Book");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/SearchBookDialog.fxml"));
        Parent root = loader.load();

        addBookStage.setScene(new Scene(root));
        addBookStage.showAndWait();

        refresh();
    }

    @FXML
    void initialize() throws SQLException {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        languageCol.setCellValueFactory(new PropertyValueFactory<>("language"));
        availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));

        deleteBookButton.setDisable(true);

        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
            @Override
            public void changed(ObservableValue<? extends Book> observableValue, Book oldValue, Book newValue) {
                selectedBook = newValue;
                deleteBookButton.setDisable(selectedBook == null);
            }
        });
        loadBook();
    }

    void refresh() throws SQLException {
        table.getItems().setAll(db.importFromDB());
    }

    void loadBook() throws SQLException {
        table.getItems().addAll(db.importFromDB());
    }

    @FXML
    void deleteBookButtonOnClick () throws SQLException {
        selectedBook = table.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            return;
        }
        boolean result = ApplicationAlert.areYouSureAboutThat();
        if (result) {
            db.deleteFromDB(selectedBook.getId());
        }
        refresh();
    }

    @FXML
    void viewDetailsButtonOnClick () throws IOException, SQLException {
        Stage viewDetails = new Stage();
        viewDetails.setResizable(false);
        viewDetails.initModality(Modality.APPLICATION_MODAL);
        viewDetails.setTitle("Details");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/BookDetails.fxml"));
        Parent root = loader.load();
        BookDetailsController controller = loader.getController();
        controller.setInfo(selectedBook);

        Scene scene = new Scene(root);

        viewDetails.setScene(scene);
        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(0.3));
        delay.setOnFinished(event -> {
            Platform.runLater(viewDetails::showAndWait);
        });
        delay.play();
    }

    @FXML
    void exportOnClick() {
        db.exportToExcel();
    }

    @FXML
    void importOnClick() throws SQLException {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter excelFilter = new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(excelFilter);

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            db.importFromExcel(filePath);
        } else {
            System.out.println("No file selected.");
        }
        refresh();
    }

    @FXML
    void searchBookOnClick(ActionEvent event) throws SQLException {
        String searchText = searchField.getText();
        List<Book> searchedBook = null;

        if (!searchText.isEmpty()) {
            searchedBook = new ArrayList<>();
            if (searchText.matches("-?\\d+(\\.\\d+)?")) {
                Book book = db.searchById(Integer.parseInt(searchText));
                searchedBook.add(book);
            } else {
                searchedBook = db.searchByTitle(searchText);
            }
        }
        table.getItems().clear();

        if (searchedBook != null) {
            table.getItems().addAll(searchedBook);
        } else {
            table.getItems().addAll(db.importFromDB());
        }
    }
}
