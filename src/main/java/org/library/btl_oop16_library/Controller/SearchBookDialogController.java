package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Util.BookDBConnector;
import org.library.btl_oop16_library.Services.GoogleBookAPI;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SearchBookDialogController {

    @FXML
    private Button addButton;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private AnchorPane rootPane;

    private Book selectedBook;


    @FXML
    void onSearchButtonClick(ActionEvent event) throws IOException {
        String searchText = searchField.getText();

        System.out.println("Search text: " + searchText);

        List<Book> books = GoogleBookAPI.searchBooks(searchText);
        mainPane.getChildren().clear();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/BookListView.fxml"));
        Parent root = loader.load();

        BookListViewController controller = loader.getController();
        controller.setGeneralPane(rootPane);

        controller.setBooks(books);
        mainPane.setCenter(root);
    }


    @FXML
    void initialize() {
        addButton.setVisible(false);
    }
}

