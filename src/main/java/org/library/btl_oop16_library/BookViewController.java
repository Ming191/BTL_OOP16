/**
 * Sample Skeleton for 'bookView.fxml' Controller Class
 */

package org.library.btl_oop16_library;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class BookViewController {
    private Library library;

    public void setLibrary(Library library) {
        this.library = library;
        initialize();
    }

    @FXML // fx:id="authorCol"
    private TableColumn<Book, String> authorCol; // Value injected by FXMLLoader

    @FXML // fx:id="availableCol"
    private TableColumn<Book, String> availableCol; // Value injected by FXMLLoader

    @FXML // fx:id="idCol"
    private TableColumn<Book, Integer> idCol; // Value injected by FXMLLoader

    @FXML // fx:id="languageCol"
    private TableColumn<Book, String> languageCol; // Value injected by FXMLLoader

    @FXML // fx:id="menuBook"
    private Button menuBook; // Value injected by FXMLLoader

    @FXML // fx:id="menuDashboard"
    private Button menuDashboard; // Value injected by FXMLLoader

    @FXML // fx:id="menuUser"
    private Button menuUser; // Value injected by FXMLLoader

    @FXML // fx:id="menuVbox"
    private VBox menuVbox; // Value injected by FXMLLoader

    @FXML // fx:id="table"
    private TableView<Book> table; // Value injected by FXMLLoader

    @FXML // fx:id="titleCol"
    private TableColumn<Book, String> titleCol; // Value injected by FXMLLoader

    @FXML // fx:id="typeCol"
    private TableColumn<Book, String> typeCol; // Value injected by FXMLLoader

    @FXML
    void handleMouseClick(MouseEvent event) {

    }

    void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        languageCol.setCellValueFactory(new PropertyValueFactory<>("language"));
        availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));
        table.getItems().addAll(library.getBooks());
    }

}
