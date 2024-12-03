package org.library.btl_oop16_library.utils.general;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Motion {
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

    public static void switchScene(Pane root1, Pane root2) {
        Stage stage = (Stage)root1.getScene().getWindow();

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefSize(100,100);

        fadeTransition(stage, root1.getScene(), new Scene(root2));
    }

    public static void startTypingAnimation(VBox chatArea) {
        Text typingText = new Text("AI is typing");

        HBox typingContainer = new HBox(new TextFlow(typingText));
        typingContainer.setAlignment(Pos.CENTER_LEFT);
        typingContainer.setPadding(new Insets(5));

        Platform.runLater(() -> chatArea.getChildren().add(typingContainer));

        Timeline typingAnimation = new Timeline(new KeyFrame(Duration.millis(500), event -> {
            String currentText = typingText.getText();
            if (currentText.endsWith("...")) {
                typingText.setText("AI is typing");
            } else {
                typingText.setText(currentText + ".");
            }
        }));

        typingAnimation.setCycleCount(Timeline.INDEFINITE);
        typingAnimation.play();
    }

    public static void stopTypingAnimation(VBox chatArea) {
        Platform.runLater(() -> {
            if (!chatArea.getChildren().isEmpty()) {
                chatArea.getChildren().removeLast();
            }
        });
    }
}
