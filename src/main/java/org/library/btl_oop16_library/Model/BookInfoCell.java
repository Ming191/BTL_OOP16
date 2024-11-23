package org.library.btl_oop16_library.Model;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.library.btl_oop16_library.Util.Transition;

import java.io.IOException;

public class BookInfoCell extends ListCell<Book> {

    @FXML
    private Label authorLabel;

    @FXML
    private GridPane rootPane;

    @FXML
    private Label categoriesLabel;

    @FXML
    private Label ratingLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private ImageView imgHolder;

    public BookInfoCell() {
        loadFXML();
    }

    private void loadFXML() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/BookInfoCell.fxml"));
        loader.setController(this);
        try {
            rootPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Book book, boolean empty) {
        super.updateItem(book, empty);

        if (empty || book == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        setItem(book);

        authorLabel.setText(book.getAuthor());
        categoriesLabel.setText(book.getCategory());
        ratingLabel.setText(book.getRating());
        titleLabel.setText(book.getTitle());
        if (book.getImgURL() != null && !book.getImgURL().isEmpty()) {
            imgHolder.setImage(new javafx.scene.image.Image(book.getImgURL(),true));
        }
        setGraphic(rootPane);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        Transition.cellAnimation(rootPane, getIndex());
    }
}

