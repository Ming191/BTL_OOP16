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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Services.ZXingAPI;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.BookDBConnector;
import org.library.btl_oop16_library.Util.ImageLoader;

import java.io.IOException;
import java.sql.SQLException;

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

    public AnchorPane getMainPane() {
        return mainPane;
    }

    public Button getButton2() {
        return button2;
    }

    public Button getButton1() {
        return button1;
    }

    public void setInfo(Book book) {
        author.setText(book.getAuthor());
        title.setText(book.getTitle());
        rating.setText(book.getRating());
        ImageLoader.loadImage(imgHolder, book.getImgURL(), 200);
        qrHolder.setImage(ZXingAPI.toQRCode(book, 100, 100));

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
            seeMoreLink.setVisible(false);
        }

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(30);

        imgHolder.setEffect(dropShadow);

        button1.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/AddBookDialog.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            AddBookDialogController controller = loader.getController();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("ADD");
            stage.setScene(new Scene(root));
            stage.showAndWait();

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

    @FXML
    private void initialize() {

    }

}
