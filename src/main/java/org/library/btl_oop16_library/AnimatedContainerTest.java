package org.library.btl_oop16_library;

import eu.iamgio.animated.transition.AnimatedThemeSwitcher;
import eu.iamgio.animated.transition.animations.clip.CircleClipOut;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AnimatedContainerTest extends Application {

    // Whether the current theme is 'light' (if false = 'dark')
    private boolean isLight = true;

    public void start(Stage primaryStage) {
        // Setup scene
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/css/nord-light.css").toExternalForm());

        VBox box = new VBox();
        box.getStyleClass().add("central-box");
        root.getChildren().add(box);

        Region icon = new Region();
        icon.getStyleClass().add("icon");
        box.getChildren().add(icon);

        Label title = new Label("Theme switch!");
        title.getStyleClass().add("title");
        box.getChildren().add(title);

        Label subtitle = new Label("Animated lets you create cool transitions.\nTry it out!");
        subtitle.getStyleClass().add("subtitle");
        box.getChildren().add(subtitle);

        // Add a button to switch themes
        Button switchButton = new Button("Switch Theme");
        switchButton.setOnAction(e -> switchTheme(scene));
        box.getChildren().add(switchButton);

        // Setup theme
        AnimatedThemeSwitcher themeSwitcher = new AnimatedThemeSwitcher(scene, new CircleClipOut());
        themeSwitcher.init();

        // Show
        primaryStage.setTitle("AnimatedThemeSwitcher");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to switch the theme
    private void switchTheme(Scene scene) {
        isLight = !isLight; // Toggle theme state
        scene.getStylesheets().set(0, getClass().getResource("/css/nord-" + (isLight ? "light" : "dark") + ".css").toExternalForm());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
