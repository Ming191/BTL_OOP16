//package org.library.btl_oop16_library;
//
//import javafx.animation.RotateTransition;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.StackPane;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//
//public class TEST2 extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        // Create two scenes
//        StackPane root1 = new StackPane();
//        root1.setStyle("-fx-background-color: #3498db;");
//        Scene scene1 = new Scene(root1, 400, 300);
//
//        StackPane root2 = new StackPane();
//        root2.setStyle("-fx-background-color: #e74c3c;");
//        Scene scene2 = new Scene(root2, 400, 300);
//
//        // Add some content to the scenes
//        root1.getChildren().add(new javafx.scene.control.Label("Scene 1"));
//        root2.getChildren().add(new javafx.scene.control.Label("Scene 2"));
//
//        // Set up a RotateTransition (Spin Effect)
//        RotateTransition rotateOut = new RotateTransition(Duration.seconds(0.5), root1);
//        rotateOut.setFromAngle(0);
//        rotateOut.setToAngle(360);
//
//        RotateTransition rotateIn = new RotateTransition(Duration.seconds(0.5), root2);
//        rotateIn.setFromAngle(-360);
//        rotateIn.setToAngle(0);
//
//        rotateOut.setOnFinished(event -> {
//            primaryStage.setScene(scene2);
//            rotateIn.play();
//        });
//
//        Button btnChangeScene = new Button("Change Scene");
//        btnChangeScene.setOnAction(event -> {
//            rotateOut.play();
//        });
//
//        root1.getChildren().add(btnChangeScene);
//
//        primaryStage.setScene(scene1);
//        primaryStage.setTitle("Scene Change with Spin Effect");
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
