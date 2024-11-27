package org.library.btl_oop16_library.Controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import atlantafx.base.controls.ModalPane;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.BookDBConnector;
import org.library.btl_oop16_library.Util.SessionManager;

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

    private ModalPane modalPane;

    private AnchorPane detailsPane;

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
        }
    }


    @FXML
    private void addBookButtonOnClick() throws IOException, SQLException {
        Stage addBookStage = new Stage();
        addBookStage.setHeight(720.0);
        addBookStage.setWidth(1060);
        addBookStage.setResizable(false);
        addBookStage.initModality(Modality.APPLICATION_MODAL);
        addBookStage.setTitle("Add Book");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/SearchBookDialog.fxml"));
        Parent root = loader.load();

        Image favicon = new Image(getClass().getResource("/img/logo_2min.png").toExternalForm())   ;
        addBookStage.getIcons().add(favicon);

        addBookStage.setScene(new Scene(root));
        addBookStage.showAndWait();

        refresh();
    }

    @FXML
    private void initialize() throws SQLException {
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

        modalPane = new ModalPane();

        modalPane.displayProperty().addListener((obs, old, val) -> {
            if (!val) {
                modalPane.setAlignment(Pos.CENTER);
                modalPane.usePredefinedTransitionFactories(null);
            }
        });

        rootPane.getChildren().add(modalPane);
        modalPane.setPrefSize(1060,720);

        viewDetailsButton.setOnAction(event -> {
            try {
                setupModalPane();
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
            modalPane.show(detailsPane);
        });

        initializeRoleBasedAccess();
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

    private void setupModalPane () throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/BookDetails.fxml"));
        Parent root = loader.load();
        BookDetailsController controller = loader.getController();
        controller.getButton2().setOnAction(event -> {
            modalPane.hide();
        });
        controller.setInfo(selectedBook);
        controller.getButton1().setVisible(false);
        detailsPane = controller.getMainPane();
    }

    @FXML
    private void exportOnClick() {
        db.exportToExcel();
    }

    @FXML
    private void importOnClick() throws SQLException {
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
    private void searchBookOnClick(ActionEvent event) throws SQLException {
        String searchText = searchField.getText();
        List<Book> searchedBook = null;

        if (!searchText.isEmpty()) {
            searchedBook = db.searchBookFromDB(searchText);
        }
        table.getItems().clear();

        if (searchedBook != null) {
            table.getItems().addAll(searchedBook);
        } else {
            table.getItems().addAll(db.importFromDB());
        }
    }
}
