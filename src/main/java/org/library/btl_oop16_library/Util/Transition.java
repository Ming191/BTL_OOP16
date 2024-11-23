package org.library.btl_oop16_library.Util;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Transition {
    public static void fadeTransition(Stage stage, Scene currentScene, Scene nextScene) {
        try {
            // Load the next scene's root from FXML
            Parent nextRoot = nextScene.getRoot();
            nextRoot.setOpacity(0);
            stage.setScene(nextScene);

            Timeline timeline = new Timeline();

            KeyValue fadeOutValue = new KeyValue(currentScene.getRoot().opacityProperty(), 0);
            KeyFrame fadeOutKeyFrame = new KeyFrame(Duration.seconds(1), fadeOutValue);

            KeyValue fadeInValue = new KeyValue(nextRoot.opacityProperty(), 1);
            KeyFrame fadeInKeyFrame = new KeyFrame(Duration.seconds(1), fadeInValue);

            timeline.getKeyFrames().addAll(fadeOutKeyFrame, fadeInKeyFrame);

            timeline.play();

            timeline.setOnFinished(event -> {
                stage.setScene(nextScene);
                nextRoot.setOpacity(1);
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
}
