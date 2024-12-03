package org.library.btl_oop16_library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.library.btl_oop16_library.utils.general.SessionManager;
import org.library.btl_oop16_library.utils.database.UserDBConnector;

import java.io.IOException;

import static org.library.btl_oop16_library.utils.general.GlobalVariables.*;

public class TEST2 extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        stage.resizableProperty().setValue(Boolean.FALSE);
        SessionManager.getInstance().setCurrentUser(UserDBConnector.getInstance().getUser("minh","minh"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/fxml/views/AIChat.fxml"));
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