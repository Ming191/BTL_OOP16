package org.library.btl_oop16_library;

import atlantafx.base.theme.NordLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Util.GlobalVariables;

import java.io.IOException;
import java.util.Objects;

public class Test extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(createContent(), 820, 640);
        stage.setScene(scene);
        stage.show();
    }

    private Region createContent() {
        ImageView imageView = new ImageView();
        Image loadingImage = new Image(GlobalVariables.LOADING_IMG);
        imageView.setImage(loadingImage);

        Image image = new Image("https://www.pragmaticcoding.ca/assets/images/794.png", 700, 0, true, true, true);
        image.progressProperty().addListener(observable -> {
            if (image.getProgress() == 1.0) {
                if (!image.isError()) {
                    imageView.setImage(image);
                } else {
                    System.out.println("Error");
                }
            }
        });
        VBox vBox = new VBox(imageView);
        vBox.setPadding(new Insets(30));
        return vBox;
    }
}
