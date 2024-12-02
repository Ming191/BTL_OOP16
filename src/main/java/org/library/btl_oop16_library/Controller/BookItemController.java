package org.library.btl_oop16_library.Controller;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Util.ImageLoader;

import java.io.IOException;

public class BookItemController {

    @FXML
    private Card card;

    @FXML
    private AnchorPane cardPane;

    public void setCard(Book book, AnchorPane generalPane, String stage) {
        setupCardDimensions();
        setupImage(book);
        setupTitle(book);
        setupFooter(book);
        setupCardEvent(book, generalPane, stage);
    }

    private void setupCardDimensions() {
        card.setPrefWidth(180);
    }

    private void setupImage(Book book) {
        ImageView imageView = new ImageView();
        ImageLoader.loadImage(imageView, book.getImgURL(), 100);

        Rectangle clip = new Rectangle(120, 180);
        imageView.setClip(clip);

        StackPane imageContainer = new StackPane(imageView);
        imageContainer.setPrefSize(180, 180);
        imageContainer.setStyle("-fx-alignment: center;");
        card.setHeader(imageContainer);
    }

    private void setupTitle(Book book) {
        Label title = new Label(book.getTitle());
        card.setBody(title);
    }

    private void setupFooter(Book book) {
        card.setFooter(new Label(book.getAuthor()));
        card.getStyleClass().add(Styles.INTERACTIVE);
        card.getBody().getStyleClass().add(Styles.TEXT_BOLD);
        card.getHeader().getStyleClass().add(Styles.TEXT_BOLD);
        card.getFooter().getStyleClass().add(Styles.TEXT_ITALIC);
    }

    private void setupCardEvent(Book book, AnchorPane generalPane, String stage) {
        card.setOnMouseClicked(event -> {
            ModalPane detailsPane = (ModalPane) generalPane.getScene().lookup("#detailsPane");
            VBox detailsVBox = (VBox) generalPane.getScene().lookup("#detailsVBox");
            try {
                detailsVBox.getChildren().clear();
                detailsVBox.getChildren().add(showBookDetailsModal(book, detailsPane, "addBook"));
                detailsPane.show(detailsVBox);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Pane showBookDetailsModal(Book book, ModalPane modalPane, String stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/BookDetails.fxml"));
        Pane root = fxmlLoader.load();
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: -color-bg-default;");
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(root);

        BookDetailsController bookDetailsController = fxmlLoader.getController();
        bookDetailsController.setInfo(book, stage);
        Button button = bookDetailsController.getButton2();
        button.setOnAction(event -> {
            modalPane.hide();
        });

        return root;
    }
}
