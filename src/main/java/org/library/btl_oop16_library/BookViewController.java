package org.library.btl_oop16_library;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookViewController {
    private DatabaseConnector bookDB;
    private BookList bookList;

    @FXML
    private TableColumn<Book, String> authorCol;

    @FXML
    private TableColumn<Book, String> availableCol;

    @FXML
    private TableColumn<Book, Integer> idCol;

    @FXML
    private TableColumn<Book, String> languageCol;

    @FXML
    private Button menuBook;

    @FXML
    private Button menuDashboard;

    @FXML
    private Button menuUser;

    @FXML
    private VBox menuVbox;

    @FXML
    private TableView<Book> table;

    @FXML
    private TableColumn<Book, String> titleCol;

    @FXML
    private TableColumn<Book, String> typeCol;

    @FXML
    void handleMouseClick(MouseEvent event) {

    }

    @FXML
    void addBookButtonOnClick() throws IOException {
        Stage addBookStage = new Stage();
        addBookStage.setResizable(false);
        addBookStage.initModality(Modality.APPLICATION_MODAL);
        addBookStage.setTitle("Add Book");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("addBookDialog.fxml"));
        Parent root = loader.load();

        addBookStage.setScene(new Scene(root));
        addBookStage.showAndWait();

        table.getItems().clear();
        bookList.getBooks().clear();

        bookDB.selectFromDB(bookList);
        table.getItems().addAll(bookList.getBooks());
    }

    @FXML
    void initialize() {
        bookDB = new DatabaseConnector();
        bookList = new BookList();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        languageCol.setCellValueFactory(new PropertyValueFactory<>("language"));
        availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));

        bookDB.selectFromDB(bookList);
        table.getItems().addAll(bookList.getBooks());
    }

    @FXML
    void deleteBookButtonOnClick () throws IOException {
        Stage deleteBookStage = new Stage();
        deleteBookStage.setResizable(false);
        deleteBookStage.initModality(Modality.APPLICATION_MODAL);
        deleteBookStage.setTitle("Delete Book");

        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("deleteBookDialog.fxml"));
        Parent root = loader1.load();
        deleteBookStage.setScene(new Scene(root));
        deleteBookStage.showAndWait();

        table.getItems().clear();
        bookList.getBooks().clear();
        bookDB.selectFromDB(bookList);
        table.getItems().addAll(bookList.getBooks());
    }
}
