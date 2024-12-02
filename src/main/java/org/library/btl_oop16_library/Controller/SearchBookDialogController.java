package org.library.btl_oop16_library.Controller;

import atlantafx.base.controls.ModalPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Services.GoogleBookAPI;
import org.library.btl_oop16_library.Util.ImageLoader;

import java.io.IOException;
import java.util.List;

public class SearchBookDialogController {
    @FXML
    public Button backButton;

    @FXML
    private Button addButton;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private AnchorPane searchPane;

    private Book selectedBook;

    public Button getBackButton() {
        return backButton;
    }

    @FXML
    private void onSearchButtonClick(ActionEvent event) throws IOException {
        String searchText = searchField.getText();

        System.out.println("Search text: " + searchText);

        List<Book> books = GoogleBookAPI.searchBooks(searchText);
        mainPane.getChildren().clear();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/BookListView.fxml"));
        Parent root = loader.load();

        BookListViewController controller = loader.getController();
        controller.setGeneralPane(searchPane);

        controller.setBooks(books);
        mainPane.setCenter(root);
    }

    @FXML
    void initialize() {
        ModalPane detailsPane = new ModalPane();
        detailsPane.setId("detailsPane");
        VBox detailsVBox = new VBox();
        detailsVBox.setId("detailsVBox");
        detailsVBox.setPrefSize(1280,720);
        detailsPane.setPrefSize(1280,720);
        searchPane.getChildren().addAll(detailsPane, detailsVBox);
        detailsVBox.toBack();
    }
}

