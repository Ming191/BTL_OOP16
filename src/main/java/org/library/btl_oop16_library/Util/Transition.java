package org.library.btl_oop16_library.Util;

import eu.iamgio.animated.binding.Animated;
import eu.iamgio.animated.binding.property.animation.AnimationProperty;
import eu.iamgio.animated.common.Curve;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import static org.library.btl_oop16_library.Util.GlobalVariables.LOADING_IMG;

public class Transition {
    public static void fadeTransition(Stage stage, Scene currentScene, Scene nextScene) {
        try {
            Parent nextRoot = nextScene.getRoot();
            nextRoot.setOpacity(0);
            stage.setScene(nextScene);

            nextRoot.setDisable(true);

            Timeline timeline = new Timeline();

            KeyValue fadeOutValue = new KeyValue(currentScene.getRoot().opacityProperty(), 0);
            KeyFrame fadeOutKeyFrame = new KeyFrame(Duration.seconds(0.25), fadeOutValue);

            KeyValue fadeInValue = new KeyValue(nextRoot.opacityProperty(), 1);
            KeyFrame fadeInKeyFrame = new KeyFrame(Duration.seconds(0.25), fadeInValue);

            timeline.getKeyFrames().addAll(fadeOutKeyFrame, fadeInKeyFrame);

            timeline.play();

            timeline.setOnFinished(event -> {
                stage.setScene(nextScene);
                nextRoot.setOpacity(1);
                nextRoot.setDisable(false);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cellAnimation(Node node) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), node);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        PauseTransition pauseTransition = new PauseTransition(Duration.millis( 200));

        SequentialTransition sequentialTransition = new SequentialTransition(pauseTransition, fadeTransition);
        sequentialTransition.play();
    }

    public static void switchScene(Pane root, String fxmlPath) {
        Stage stage = (Stage)root.getScene().getWindow();
        ImageView loadingGif = new ImageView(LOADING_IMG);
        loadingGif.setPreserveRatio(true);
        loadingGif.setStyle("-fx-alignment: center;");
        loadingGif.setFitWidth(50);

        root.getChildren().add(loadingGif);

        new Thread(() -> {
            try {
                Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(Transition.class.getResource(fxmlPath));
                        Parent newRoot = loader.load();
                        Scene newScene = new Scene(newRoot);
                        fadeTransition(stage,root.getScene(),newScene);
                        stage.setScene(newScene);
                        root.getChildren().remove(loadingGif);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e ) {
                e.printStackTrace();
            }
        }).start();
    }
}
