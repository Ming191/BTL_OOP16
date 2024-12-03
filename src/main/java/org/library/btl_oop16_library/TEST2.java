package org.library.btl_oop16_library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Util.SessionManager;
import org.library.btl_oop16_library.Util.UserDBConnector;

import java.io.IOException;

import static org.library.btl_oop16_library.Util.GlobalVariables.*;

public class TEST2 extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        stage.resizableProperty().setValue(Boolean.FALSE);
        SessionManager.getInstance().setCurrentUser(UserDBConnector.getInstance().getUser("minh","minh"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserFXMLs/AIChat.fxml"));
        Parent root = loader.load();


        Scene scene = new Scene(root);
        Application.setUserAgentStylesheet(LIGHT_THEME);
        Image favicon = new Image(getClass().getResource("/img/logo.png").toExternalForm())   ;
        stage.getIcons().add(favicon);

        stage.setTitle("2Min Library!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}