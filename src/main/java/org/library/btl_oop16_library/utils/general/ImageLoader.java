package org.library.btl_oop16_library.utils.general;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class ImageLoader {
    public static void loadImage(ImageView imgHolder, StackPane stackPane, String URL, int width) {
        imgHolder.setPreserveRatio(true);
        imgHolder.setFitWidth(width);
        ProgressIndicator loadingIndicator = new ProgressIndicator();
        if(URL.isEmpty()) {
            imgHolder.setImage(new Image(GlobalVariables.DEFAULT_IMG));
            return;
        }
        stackPane.getChildren().add(loadingIndicator);
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() {
                return new Image(URL);
            }
        };

        loadImageTask.setOnSucceeded(e -> {
            imgHolder.setImage(loadImageTask.getValue());
            loadingIndicator.setVisible(false);
        });

        new Thread(loadImageTask).start();
    }
}
