package org.library.btl_oop16_library.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Model.BookInfoCellFactory;
import org.library.btl_oop16_library.Util.BookDBConnector;
import org.library.btl_oop16_library.Util.GoogleBookAPI;

import javax.swing.text.View;
import java.io.IO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SearchBookDialogController {

    @FXML
    private Button addButton;

    @FXML
    private ListView<Book> bookList;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    private Book selectedBook;

    @FXML
    void onAddButtonClick(ActionEvent event) {
        if (selectedBook != null) {
            try {
                Stage addBookStage = new Stage();
                addBookStage.setResizable(false);
                addBookStage.initModality(Modality.APPLICATION_MODAL);
                addBookStage.setTitle("Add Book");
                System.out.println("Add Book button clicked.");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/AddBookDialog.fxml"));
                Parent root = loader.load();

                addBookStage.setScene(new Scene(root));
                addBookStage.showAndWait();

                AddBookDialogController controller = loader.getController();
                int quantity = controller.getQuantity();

                if (quantity > 0) {
                    try {
                        selectedBook.setAvailable(quantity);
                        BookDBConnector.getInstance().addToDB(selectedBook);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e ) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onSearchButtonClick(ActionEvent event) {
        String searchText = searchField.getText();

        List<Book> books = GoogleBookAPI.searchBooks(searchText);

        bookList.getItems().clear();
        bookList.getItems().addAll(books);
    }

    @FXML
    void initialize() {
        bookList.setCellFactory(new BookInfoCellFactory());
        addButton.setDisable(true);

        bookList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
            @Override
            public void changed(ObservableValue<? extends Book> observableValue, Book oldValue, Book newValue) {
                selectedBook = newValue;
                addButton.setDisable(selectedBook == null);
            }
        });
    }

}

