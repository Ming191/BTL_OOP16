package org.library.btl_oop16_library;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.NordLight;
import atlantafx.base.theme.Styles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
        primaryStage.setWidth(640);
        primaryStage.setHeight(480);
        primaryStage.setMinWidth(320);
        Scene scene = getScene();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static Scene getScene() {
        StackPane root = new StackPane();
        VBox sceneRoot = new VBox();
        ModalPane aboutModalPane = new ModalPane();

        aboutModalPane.setId("aboutModal");
        aboutModalPane.displayProperty().addListener((obs, old, val) -> {
            if (!val) {
                aboutModalPane.setAlignment(Pos.CENTER);
                aboutModalPane.usePredefinedTransitionFactories(null);
            }
        });

        Dialog aboutDialog = new Dialog(300, 300);

        FXMLLoader loader = new FXMLLoader(App.class.getResource("/org/library/btl_oop16_library/view/Settings.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Button aboutDialogOpenBtn = new Button(null, new FontIcon(Material2OutlinedAL.INFO));
        aboutDialogOpenBtn.getStyleClass().addAll(Styles.ROUNDED);
        aboutDialogOpenBtn.setOnAction(evt -> {
            aboutModalPane.show(aboutDialog);
        });

        Button aboutDialogCloseBtn = new Button(null, new FontIcon(Material2OutlinedAL.CLOSE));
        aboutDialogCloseBtn.getStyleClass().addAll(Styles.ROUNDED);
        aboutDialogCloseBtn.setOnAction(evt -> {
            aboutModalPane.hide(true);
        });
        aboutDialog.getChildren().setAll(aboutDialogCloseBtn);

        aboutDialog.getChildren().setAll(pane);

        sceneRoot.getChildren().addAll(aboutDialogOpenBtn);

        root.getChildren().addAll(sceneRoot, aboutModalPane);

        return new Scene(root);
    }

    private static class Dialog extends VBox {

        public Dialog(int width, int height) {
            super();
            setSpacing(10);
            setMinSize(width, height);
            setMaxSize(width, height);
            setStyle("-fx-background-color: -color-bg-default;");
        }
    }
}