package org.library.btl_oop16_library;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Transtition {
    public static void fadeTransition(Stage stage, Scene currentScene, Scene nextScene) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), currentScene.getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            stage.setScene(nextScene);
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), nextScene.getRoot());
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }
}
