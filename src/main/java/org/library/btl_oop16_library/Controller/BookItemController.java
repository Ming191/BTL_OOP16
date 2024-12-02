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

    private ModalPane modalPane;

    public void setCard(Book book, AnchorPane generalPane, String stage) {
        card.setPrefWidth(180);
        ImageView imageView = new ImageView();
        ImageLoader.loadImage(imageView,book.getImgURL(), 100);

        Rectangle clip = new Rectangle(120, 180);
        imageView.setClip(clip);

        StackPane imageContainer = new StackPane(imageView);
        imageContainer.setPrefSize(180, 180);
        imageContainer.setStyle("-fx-alignment: center;");

        Text title = new Text(book.getTitle());
        title.setWrappingWidth(170);
        card.setHeader(imageContainer);
        card.setBody(title);
        card.setFooter(new Label(book.getAuthor()));

        card.getStyleClass().add(Styles.INTERACTIVE);
        card.getBody().getStyleClass().add(Styles.TEXT_BOLD);
        card.getHeader().getStyleClass().add(Styles.TEXT_BOLD);
        card.getFooter().getStyleClass().add(Styles.TEXT_ITALIC);

        AnchorPane mainPane = (AnchorPane) generalPane.getScene().lookup("#rootPane");
        mainPane.getChildren().add(modalPane);
        modalPane.setAlignment(Pos.CENTER);

        card.setOnMouseClicked(event -> {
            try {
                modalPane.show(showBookDetailsModal(book, (AnchorPane) mainPane, stage));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Pane showBookDetailsModal(Book book, AnchorPane mainPane, String stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/BookDetails.fxml"));
        Pane root = fxmlLoader.load();
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: -color-bg-default;");
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(root);

        BookDetailsController bookDetailsController = fxmlLoader.getController();
        bookDetailsController.setInfo(book, stage);
        Button button  = bookDetailsController.getButton2();
        button.setOnAction(event -> {
            modalPane.hide();
            modalPane = new ModalPane();
            modalPane.setPrefSize(1280, 720);
            modalPane.setAlignment(Pos.CENTER);
        });
        return root;
    }

    @FXML
    private void initialize() {
        modalPane = new ModalPane();
        modalPane.setPrefSize(1280, 720);
    }
}
