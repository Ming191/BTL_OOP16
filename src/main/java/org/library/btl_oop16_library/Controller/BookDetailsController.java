package org.library.btl_oop16_library.Controller;

import io.github.palexdev.mfxcore.controls.Label;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Model.Comment;
import org.library.btl_oop16_library.Services.ZXingAPI;
import org.library.btl_oop16_library.Util.*;
import org.library.btl_oop16_library.Factory.CommentPaneFactory;
import org.library.btl_oop16_library.Factory.DialogFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.library.btl_oop16_library.Util.GlobalVariables.COMMENT_ITEM_PATH;

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

    @FXML
    private VBox descBox;

    @FXML
    private VBox contentHolder;

    private VBox commentsHolder;

    public AnchorPane getMainPane() {
        return mainPane;
    }

    public Button getButton2() {
        return button2;
    }

    public Button getButton1() {
        return button1;
    }

    private Book currentBook;

    public void setInfo(Book book) {
        this.currentBook = book; // Save the current book for refresh operations
        author.setText(book.getAuthor());
        title.setText(book.getTitle());
        rating.setText(book.getRating());
        ImageLoader.loadImage(imgHolder, book.getImgURL(), 200);
        qrHolder.setImage(ZXingAPI.toQRCode(book, 100, 100));

        setupDescription(book);
        setupButtons(book);
        commentsHolder = new VBox();
        Runnable refreshCallback = this::refreshComments;
        contentHolder.getChildren().add(CommentPaneFactory.createCommentBox(book, refreshCallback));
        contentHolder.getChildren().add(commentsHolder);
        commentsSetup(book);
    }

    private void setupDescription(Book book) {
        Hyperlink seeMoreLink = new Hyperlink("See More");
        if (book.getDescription().length() > 300) {
            String truncatedText = book.getDescription().substring(0, 200) + "... ";
            description.setText(truncatedText);
            seeMoreLink.setOnAction(event -> {
                description.setText(book.getDescription());
                seeMoreLink.setVisible(false);
            });
            descBox.getChildren().add(seeMoreLink);
        } else {
            description.setText(book.getDescription());
        }

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(30);

        imgHolder.setEffect(dropShadow);
    }

    private void setupButtons(Book book) {
        button1.setOnAction(actionEvent -> {
            AddBookDialogController controller = DialogFactory.createAddBookDialog(
                    "/org/library/btl_oop16_library/view/AddBookDialog.fxml"
            );

            int quantity = controller.getQuantity();

            if (quantity < 0) {
                ApplicationAlert.invalidQuantity();
                return;
            }

            book.setAvailable(quantity);
            try {
                BookDBConnector.getInstance().addToDB(book);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void commentsSetup(Book book) {
        commentsHolder.getChildren().clear();
        List<Comment> comments = CommentsDBConnector.getInstance().searchByBookId(book.getId());
        for (Comment comment : comments) {
            commentsHolder.getChildren().add(CommentPaneFactory.createCommentPane(comment));
        }
    }
    private void refreshComments() {
        commentsSetup(currentBook);
    }

}
