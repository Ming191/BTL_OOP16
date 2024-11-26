package org.library.btl_oop16_library.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import org.library.btl_oop16_library.Model.Book;

import java.io.IOException;
import java.util.List;

public class BookListViewController {

    @FXML
    private FlowPane flowPane;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane generalPane;

    public void setGeneralPane(AnchorPane generalPane) {
        this.generalPane = generalPane;
    }

    public void setBooks(List<Book> books) throws IOException {
        flowPane.getChildren().clear();
        flowPane.setHgap(15);
        flowPane.setVgap(15);

        int itemsPerRow = 4;

        for (Book book : books) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/BookItem.fxml"));
            Region pane = loader.load();
            pane.prefWidthProperty().bind(flowPane.widthProperty().divide(4).subtract(flowPane.getHgap()));
            pane.setPrefHeight(300);
            BookItemController controller = loader.getController();
            controller.setCard(book, generalPane);
            flowPane.getChildren().add(pane);
        }
    }

    @FXML
    public void initialize() {
        flowPane.prefWidthProperty().bind(scrollPane.widthProperty());
        flowPane.prefHeightProperty().bind(scrollPane.heightProperty());
    }
}
