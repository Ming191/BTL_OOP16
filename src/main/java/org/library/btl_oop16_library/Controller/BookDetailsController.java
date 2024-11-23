package org.library.btl_oop16_library.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.library.btl_oop16_library.Model.Book;

public class BookDetailsController {
    @FXML
    private Label authorsLabel;

    @FXML
    private Label categoriesLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private ImageView imgHolder;

    @FXML
    private ImageView qrCodeHolder;

    @FXML
    private Label ratingLabel;

    @FXML
    private GridPane rootPane;

    @FXML
    private Label titleLabel;

    public void loadBook(Book currentBook) {
        titleLabel.setText(currentBook.getTitle());
        authorsLabel.setText(currentBook.getAuthor());
        categoriesLabel.setText(currentBook.getCategory());
        descriptionLabel.setText(currentBook.getDescription());
        ratingLabel.setText(currentBook.getRating());
        imgHolder.setImage(new Image(currentBook.getImgURL(),true));
    }

    @FXML
    void initialize() {
    }
}
