package org.library.btl_oop16_library.Util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageLoader {
    public static void loadImage(ImageView imgHolder, String URL) {
        if(URL.isEmpty()) {
            imgHolder.setImage(new Image(GlobalVariables.DEFAULT_IMG));
            return;
        }
        imgHolder.setImage(new Image(GlobalVariables.LOADING_IMG));

        Image image = new Image(URL,true);
        image.progressProperty().addListener(observable -> {
            if (image.getProgress() == 1.0) {
                if (!image.isError()) {
                    imgHolder.setImage(image);
                } else {
                    imgHolder.setImage(new Image(GlobalVariables.DEFAULT_IMG));
                }
            }
        });
    }
}
