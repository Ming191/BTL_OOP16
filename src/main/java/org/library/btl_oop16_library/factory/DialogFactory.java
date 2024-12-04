package org.library.btl_oop16_library.factory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.library.btl_oop16_library.controller.dialogs.AddBookDialogController;

import java.io.IOException;

import static org.library.btl_oop16_library.utils.general.GlobalVariables.ICON_PATH;

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
        Image favicon = new Image(DialogFactory.class.getResource(ICON_PATH).toExternalForm());
        stage.getIcons().add(favicon);
        stage.showAndWait();

        return controller;
    }
}
