package org.library.btl_oop16_library.Util;

import javafx.animation.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Transtition {
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
                nextRoot.setOpacity(1); // Ensure nextRoot is fully visible
            });

        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions like loading FXML
        }
    }
}
