package org.library.btl_oop16_library.controller.views;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import atlantafx.base.controls.ModalPane;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.library.btl_oop16_library.controller.dialogs.SearchBookDialogController;
import org.library.btl_oop16_library.model.Book;
import org.library.btl_oop16_library.services.ExcelAPI;
import org.library.btl_oop16_library.utils.database.BookDBConnector;
import org.library.btl_oop16_library.utils.general.ApplicationAlert;
import org.library.btl_oop16_library.utils.general.SessionManager;

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
    private AnchorPane rootPane;

    @FXML
    private MFXButton addBookButton;

    @FXML
    private MFXButton deleteBookButton;

    @FXML
    private TableView<Book> table;

    @FXML
    private TableColumn<Book, String> titleCol;

    @FXML
    private TableColumn<Book, String> category;

    @FXML
    private TextField searchField;

    @FXML
    private MFXButton viewDetailsButton;

    @FXML
    private MFXButton exportButton;

    @FXML
    private MFXButton importButton;

    @FXML
    private Button updateButton;

    @FXML
    private ChoiceBox<String> typeSearchBox;

    private PauseTransition searchPause;

    private void initializeRoleBasedAccess() {
        if (!"admin".equalsIgnoreCase(SessionManager.getInstance().getCurrentUser().getRole())) {
            addBookButton.setDisable(true);
            deleteBookButton.setDisable(true);
            addBookButton.setVisible(false);
            deleteBookButton.setVisible(false);
            importButton.setDisable(true);
            exportButton.setDisable(true);
            importButton.setVisible(false);
            exportButton.setVisible(false);
        } else {
            deleteBookButton.setDisable(true);
            viewDetailsButton.setDisable(true);
        }
    }

    private void setupViewDetailsButton() {
        viewDetailsButton.setOnAction(event -> {
            VBox contentHolder = (VBox) rootPane.getScene().lookup("#bookContentVbox");
            ModalPane modalPane = (ModalPane) rootPane.getScene().lookup("#bookContent");
            try {
                contentHolder.getChildren().clear();
                contentHolder.getChildren().addAll(getBookDetailsPane(modalPane));
                modalPane.show(contentHolder);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setupAddBookButton() {
        addBookButton.setOnAction(event -> {
            VBox contentHolder = (VBox) rootPane.getScene().lookup("#bookContentVbox");
            ModalPane modalPane = (ModalPane) rootPane.getScene().lookup("#bookContent");
            try {
                contentHolder.getChildren().clear();
                contentHolder.getChildren().addAll(getAddBookPane(modalPane));
                modalPane.show(contentHolder);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }



    @FXML
    private void initialize() throws SQLException {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        languageCol.setCellValueFactory(new PropertyValueFactory<>("language"));
        availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));

        table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            selectedBook = newValue;
            deleteBookButton.setDisable(selectedBook == null);
            viewDetailsButton.setDisable(selectedBook == null);
        });
        loadBook();
        initializeRoleBasedAccess();
        setupViewDetailsButton();
        setupAddBookButton();

        searchPause = new PauseTransition(Duration.millis(500));
        searchPause.setOnFinished(event -> realTimeSearch(searchField.getText()));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchPause.stop();
            searchPause.playFromStart();
        });

        typeSearchBox.getItems().addAll("id", "title", "author", "category");
        typeSearchBox.setValue("title");
    }

    private void refresh() throws SQLException {
        table.getItems().setAll(db.importFromDB());
    }

    private void loadBook() throws SQLException {
        table.getItems().addAll(db.importFromDB());
    }

    @FXML
    private void deleteBookButtonOnClick () throws SQLException {
        selectedBook = table.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            return;
        }
        boolean result = ApplicationAlert.areYouSureAboutThat();
        if (result) {
            db.deleteFromDB(selectedBook.getId());
            ApplicationAlert.deleteSuccess();
        }
        refresh();
    }

    private Pane getBookDetailsPane(ModalPane modalPane) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/fxml/views/BookDetails.fxml"));
        Pane root = loader.load();
        BookDetailsController controller = loader.getController();
        controller.getButton2().setOnAction(event -> {
            modalPane.hide();
            root.getChildren().clear();
        });
        controller.setInfo(selectedBook, "viewDetails");
        return root;
    }

    private Pane getAddBookPane(ModalPane modalPane) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/fxml/dialogs/SearchBookDialog.fxml"));
        Pane root = loader.load();
        SearchBookDialogController controller = loader.getController();
        controller.getBackButton().setOnAction(event -> {
            modalPane.hide();
            root.getChildren().clear();
        });
        return root;
    }

    @FXML
    private void exportOnClick() {
        db.exportToExcel();
    }

    @FXML
    private void importOnClick() throws SQLException {
        ExcelAPI.importExcel(BookDBConnector.getInstance());
        refresh();
    }

    private void realTimeSearch(String searchInput) {
        table.getItems().clear();
        List<Book> bookList = new ArrayList<>();
        String selectedType = typeSearchBox.getValue();
        switch (selectedType) {
            case "id":
                try {
                    int id = Integer.parseInt(searchInput);
                    bookList = BookDBConnector.getInstance().searchById(id);
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                }
                break;
            case "title":
                bookList = BookDBConnector.getInstance().searchByAttributes(searchInput, "title");
                break;
            case "author":
                bookList = BookDBConnector.getInstance().searchByAttributes(searchInput, "author");
                break;
            case "category":
                bookList = BookDBConnector.getInstance().searchByAttributes(searchInput, "category");
                break;
            default:
                System.out.println("Invalid search type selected.");
        }

        if (!bookList.isEmpty()) {
            table.getItems().addAll(bookList);
        }

    }

    @FXML
    private void updateBookButtonOnClick() throws SQLException, IOException {
        selectedBook = table.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            return;
        }
        Stage updateBook = new Stage();
        updateBook.setResizable(false);
        updateBook.initModality(Modality.APPLICATION_MODAL);
        updateBook.setTitle("Update Book");
        System.out.println("updateBook on clicked");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/fxml/dialogs/UpdateBookDialog.fxml"));
        Pane root = loader.load();
        updateBook.setScene(new Scene(root));
        Image favicon = new Image(getClass().getResource("/img/logo.png").toExternalForm());
        updateBook.getIcons().add(favicon);
        updateBook.showAndWait();

        AddBookDialogController controller = loader.getController();
        int quantity = controller.getQuantity();
        selectedBook.setAvailable(quantity);
        db.addToDB(selectedBook);
        refresh();
    }
}
