package org.library.btl_oop16_library.Controller;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.library.btl_oop16_library.Model.Book;

import java.io.IOException;

public class BookItemController {

    @FXML
    private Card card;

    @FXML
    private AnchorPane rootPane;

    private ModalPane modalPane;

    public void setCard(Book book, AnchorPane mainPane) {
        card.setPrefWidth(200);
        ImageView imageView = new ImageView(new Image(book.getImgURL(), true));
        imageView.setFitWidth(120);
        imageView.setPreserveRatio(true);

        Rectangle clip = new Rectangle(120, 200);
        imageView.setClip(clip);

        StackPane imageContainer = new StackPane(imageView);
        imageContainer.setPrefSize(200, 200);
        imageContainer.setStyle("-fx-alignment: center;");

        card.setHeader(imageContainer);
        card.setBody(new Label(book.getTitle()));
        card.setFooter(new Label(book.getAuthor()));

        card.getStyleClass().add(Styles.INTERACTIVE);
        card.getBody().getStyleClass().add(Styles.TEXT_BOLD);
        card.getHeader().getStyleClass().add(Styles.TEXT_BOLD);
        card.getFooter().getStyleClass().add(Styles.TEXT_ITALIC);

        mainPane.getChildren().add(modalPane);

        card.setOnMouseClicked(event -> {
            try {
                modalPane.show(showBookDetailsModal(book, mainPane));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private AnchorPane showBookDetailsModal(Book book, AnchorPane mainPane) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/BookDetails.fxml"));
        Parent root = fxmlLoader.load();

        BookDetailsController bookDetailsController = fxmlLoader.getController();
        bookDetailsController.setInfo(book);
        Button button  = bookDetailsController.getButton2();
        button.setOnAction(event -> {
            modalPane.hide();
            modalPane = new ModalPane();
            modalPane.setPrefSize(1280, 720);
            mainPane.getChildren().add(modalPane);
        });
        return bookDetailsController.getMainPane();
    }

    @FXML
    void initialize() {
        modalPane = new ModalPane();
        modalPane.setPrefSize(1280, 720);
    }
}
