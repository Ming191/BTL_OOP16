package org.library.btl_oop16_library.controller.views;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.library.btl_oop16_library.services.GithubModelService;
import org.library.btl_oop16_library.utils.general.Animation;

public class AIChatController {

    @FXML
    private VBox chatBox;

    @FXML
    private TextField promptArea;

    @FXML
    private Button submitButton;

    private void setupSubmitButton() {
        submitButton.setOnAction(event -> {
            String userMessage = promptArea.getText().trim();
            if (!userMessage.isEmpty()) {
                addMessage(chatBox, userMessage, Pos.CENTER_RIGHT);
                promptArea.clear();

                Task<String> aiTask = new Task<>() {
                    @Override
                    protected String call() {
                        return GithubModelService.generateAns(GithubModelService.makePrompt(userMessage));
                    }
                };

                aiTask.setOnRunning(workerEvent -> Animation.startTypingAnimation(chatBox));

                aiTask.setOnSucceeded(workerEvent -> {
                    Animation.stopTypingAnimation(chatBox);

                    String aiResponse = aiTask.getValue();
                    displayAIResponseCharacterByCharacter(chatBox, aiResponse);
                });

                aiTask.setOnFailed(workerEvent -> {
                    Animation.stopTypingAnimation(chatBox);
                    addMessage(chatBox, "[Error generating response]", Pos.CENTER_LEFT);
                });

                new Thread(aiTask).start();
            }
        });
    }

    private void addMessage(VBox chatArea, String message, Pos alignment) {
        Text textMessage = new Text(message);
        textMessage.setStyle("-fx-fill: -color-base-0");
        TextFlow textFlow = new TextFlow(textMessage);
        HBox messageContainer = new HBox(textFlow);
        messageContainer.setAlignment(alignment);
        messageContainer.setPadding(new Insets(5));
        messageContainer.setStyle("-fx-background-color: -color-accent-4; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 10;");
        Platform.runLater(() -> chatArea.getChildren().add(messageContainer));
    }

    private void displayAIResponseCharacterByCharacter(VBox chatArea, String aiResponse) {
        TextFlow aiTextFlow = new TextFlow();
        HBox messageContainer = new HBox(aiTextFlow);
        messageContainer.setAlignment(Pos.CENTER_LEFT);
        messageContainer.setPadding(new Insets(5));
        messageContainer.setStyle("-fx-background-color: -color-bg-inset; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 10;");

        Platform.runLater(() -> chatArea.getChildren().add(messageContainer));

        Task<Void> typingTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                StringBuilder currentText = new StringBuilder();
                for (char c : aiResponse.toCharArray()) {
                    currentText.append(c);
                    Platform.runLater(() -> {
                        aiTextFlow.getChildren().clear();
                        aiTextFlow.getChildren().add(new Text(currentText.toString()));
                    });
                    Thread.sleep(25);
                }
                return null;
            }
        };
        new Thread(typingTask).start();
    }

    @FXML
    private void initialize() {
        setupSubmitButton();
        chatBox.setSpacing(10.0);
    }
}
