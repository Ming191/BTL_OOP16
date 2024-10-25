package org.library.btl_oop16_library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.resizableProperty().setValue(Boolean.FALSE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("bookView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        String css = this.getClass().getResource("/style.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("2Min Library!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}