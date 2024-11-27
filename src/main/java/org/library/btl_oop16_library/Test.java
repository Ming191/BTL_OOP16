package org.library.btl_oop16_library;

import atlantafx.base.theme.NordLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Test extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserFXMLs/U_MainMenu.fxml"));
        Parent root = loader.load();

        Application.setUserAgentStylesheet(getClass().getResource("/css/nord-light.css").toExternalForm());

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
