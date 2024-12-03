package org.library.btl_oop16_library.factory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.library.btl_oop16_library.controller.views.AddBookDialogController;

import java.io.IOException;

public class DialogFactory {
    public static AddBookDialogController createAddBookDialog(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(DialogFactory.class.getResource(fxmlPath));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load dialog FXML: " + fxmlPath, e);
        }

        AddBookDialogController controller = loader.getController();
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add Book");
        stage.setScene(new Scene(root));
        Image favicon = new Image(DialogFactory.class.getResource("/img/logo.png").toExternalForm());
        stage.getIcons().add(favicon);
        stage.showAndWait();

        return controller;
    }
}
