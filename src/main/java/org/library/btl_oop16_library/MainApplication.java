package org.library.btl_oop16_library;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

import static org.library.btl_oop16_library.utils.general.GlobalVariables.*;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Dotenv dotenv = Dotenv.configure()
                .directory("env.env")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        stage.resizableProperty().setValue(Boolean.FALSE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(LOGIN_PATH));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Application.setUserAgentStylesheet(LIGHT_THEME);
        Image favicon = new Image(Objects.requireNonNull(getClass().getResource(ICON_PATH)).toExternalForm())   ;
        stage.getIcons().add(favicon);

        stage.setTitle("2Min Library!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}