package org.library.btl_oop16_library.controller.items;

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
import org.library.btl_oop16_library.controller.views.BookDetailsController;
import org.library.btl_oop16_library.model.Book;
import org.library.btl_oop16_library.utils.general.ImageLoader;

import java.io.IOException;

import static org.library.btl_oop16_library.utils.general.GlobalVariables.BOOK_DETAILS_PATH;

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

        Rectangle clip = new Rectangle(120, 180);
        imageView.setClip(clip);

        StackPane imageContainer = new StackPane(imageView);
        ImageLoader.loadImage(imageView,imageContainer, book.getImgURL(), 100);
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
        if ("addBook".equals(stage)) {
            card.setOnMouseClicked(event -> showModal(book,
                    generalPane,
                    "addBook",
                    "detailsPane",
                    "detailsVBox"));
        } else {
            card.setOnMouseClicked(event -> showModal(book,
                    generalPane,
                    "viewDetails", "bookContent",
                    "bookContentVBox"));
        }
    }

    private void showModal(Book book, AnchorPane generalPane, String stage, String modalPaneId, String modalVBoxId) {
        ModalPane modalPane = (ModalPane) generalPane.getScene().lookup("#" + modalPaneId);
        VBox modalVBox = (VBox) generalPane.getScene().lookup("#" + modalVBoxId);
        try {
            modalVBox.getChildren().clear();
            modalVBox.getChildren().add(setupModalPane(book, modalPane, stage));
            modalPane.show(modalVBox);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private Pane setupModalPane(Book book, ModalPane modalPane, String stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(BOOK_DETAILS_PATH));
        Pane root = fxmlLoader.load();
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: -color-bg-default;");
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(root);

        BookDetailsController bookDetailsController = fxmlLoader.getController();
        bookDetailsController.setInfo(book, stage);
        Button button = bookDetailsController.getBackButton();
        button.setOnAction(event -> modalPane.hide());

        return root;
    }
}
