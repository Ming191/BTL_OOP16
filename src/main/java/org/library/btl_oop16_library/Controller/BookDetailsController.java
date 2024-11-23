package org.library.btl_oop16_library.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Util.ZXingAPI;


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

    private boolean isSet = false;

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }

    public void loadBook(Book currentBook) {
        if(!isSet) {
            titleLabel.setText(currentBook.getTitle());
            authorsLabel.setText(currentBook.getAuthor());
            categoriesLabel.setText(currentBook.getCategory());
            descriptionLabel.setText(currentBook.getDescription());
            ratingLabel.setText(currentBook.getRating());
            if (currentBook.getImgURL() != null && !currentBook.getImgURL().isEmpty()) {
                imgHolder.setImage(new Image(currentBook.getImgURL(), true));
            } else {
                imgHolder.setImage(new Image(getClass().getResource("/img/defBookCover.png").toString(),true));
            }
            if (currentBook.getPreviewURL() != null && !currentBook.getPreviewURL().isEmpty()) {
                qrCodeHolder.setImage(ZXingAPI.toQRCode(currentBook, 200, 200));
            }
            isSet = true;
        }
    }

    @FXML
    void initialize() {
    }
}
