package org.library.btl_oop16_library;

import atlantafx.base.theme.NordLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Controller.BookDetailsController;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Util.GoogleBookAPI;
import org.library.btl_oop16_library.Controller.BookListViewController;

import java.io.IOException;
import java.util.List;

public class Test extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/SearchBookDialog.fxml"));
        Parent root = loader.load();


        Scene scene = new Scene(root);
        scene.getStylesheets().add(new NordLight().getUserAgentStylesheet());
        stage.setScene(scene);
        stage.setTitle("Tooi");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
