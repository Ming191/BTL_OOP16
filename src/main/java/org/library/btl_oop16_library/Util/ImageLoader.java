package org.library.btl_oop16_library.Util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageLoader {
    public static void loadImage(ImageView imgHolder, String URL, int width) {
        imgHolder.setPreserveRatio(true);
        if(URL.isEmpty()) {
            imgHolder.setImage(new Image(GlobalVariables.DEFAULT_IMG));
            return;
        }
        imgHolder.setOpacity(0.5);
        imgHolder.setFitWidth(50);
        Image image = new Image(URL,true);
        image.progressProperty().addListener(observable -> {
            if (image.getProgress() == 1.0) {
                if (!image.isError()) {
                    imgHolder.setOpacity(1);
                    imgHolder.setFitWidth(width);
                    imgHolder.setImage(image);
                } else {
                    imgHolder.setImage(new Image(GlobalVariables.DEFAULT_IMG));
                }
            }
        });
    }
}
