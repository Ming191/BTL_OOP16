package org.library.btl_oop16_library;

import atlantafx.base.theme.NordLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        stage.resizableProperty().setValue(Boolean.FALSE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/Login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
        Image favicon = new Image(getClass().getResource("/img/logo_2min.png").toExternalForm())   ;
        stage.getIcons().add(favicon);

        stage.setTitle("2Min Library!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}