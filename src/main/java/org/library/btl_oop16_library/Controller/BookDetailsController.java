package org.library.btl_oop16_library.Controller;

import atlantafx.base.controls.ModalPane;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Util.ZXingAPI;

public class BookDetailsController {

    @FXML
    private Label author;

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Text description;

    @FXML
    private ImageView imgHolder;

    @FXML
    private ImageView qrHolder;

    @FXML
    private Label rating;

    @FXML
    private Label title;

    @FXML
    private AnchorPane mainPane;

    public AnchorPane getMainPane() {
        return mainPane;
    }

    public Button getButton2() {
        return button2;
    }

    public void setInfo(Book book) {
        author.setText(book.getAuthor());
        title.setText(book.getTitle());
        description.setText(book.getDescription());
        rating.setText(book.getRating());
        imgHolder.setImage(new Image(book.getImgURL(), true));
        qrHolder.setImage(ZXingAPI.toQRCode(book, 100, 100));

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(30);

        imgHolder.setEffect(dropShadow);
    }

    private static class Dialog extends VBox {
        public Dialog(int width, int height) {
            super();
            setSpacing(20);
            setAlignment(Pos.CENTER);
            setMinSize(width, height);
            setMaxSize(width, height);
            setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 20;");
        }
    }
}
