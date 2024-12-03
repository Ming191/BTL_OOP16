//package org.library.btl_oop16_library;
//
//import javafx.application.Application;
//import javafx.concurrent.Task;
//import javafx.scene.Scene;
//import javafx.scene.control.ProgressIndicator;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.StackPane;
//import javafx.stage.Stage;
//
//public class TEST2 extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        // Create a placeholder (loading indicator)
//        ProgressIndicator loadingIndicator = new ProgressIndicator();
//
//        // ImageView to display the image
//        ImageView imageView = new ImageView();
//        imageView.setFitWidth(300);
//        imageView.setFitHeight(200);
//        imageView.setPreserveRatio(true);
//
//        // Container to hold the loading indicator and the image
//        StackPane stackPane = new StackPane(loadingIndicator, imageView);
//
//        // Task to load the image asynchronously
//        Task<Image> loadImageTask = new Task<>() {
//            @Override
//            protected Image call() {
//                // Simulate network delay
//                try {
//                    Thread.sleep(2000); // 2-second delay
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    }
//                    return new Image("https://media.licdn.com/dms/image/v2/D4D12AQGgMCavCcCbEg/article-cover_image-shrink_600_2000/article-cover_image-shrink_600_2000/0/1711086616356?e=2147483647&v=beta&t=1N65UooE_qsxL1jn9bwI0x1CFp7-czZxArntN8jCIps");
//            }
//        };
//
//        // On successful load, display the image and hide the loading indicator
//        loadImageTask.setOnSucceeded(e -> {
//            imageView.setImage(loadImageTask.getValue());
//            loadingIndicator.setVisible(false);
//
//            // Optional: Add a fade-in effect for the loaded image
//            imageView.setOpacity(0);
//            imageView.setVisible(true);
//            imageView.setOpacity(1); // You can use a FadeTransition for smooth effect
//        });
//
//        // Start the image loading task in a separate thread
//        new Thread(loadImageTask).start();
//
//        // Set up the scene and stage
//        Scene scene = new Scene(stackPane, 400, 300);
//        primaryStage.setTitle("Image Loading Effect");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
