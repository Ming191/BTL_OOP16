package org.library.btl_oop16_library.utils.general;

import javafx.scene.text.Text;

public class TextTruncator {
    public static Text createTruncatedText(String originalText, int maxLength) {
        if (originalText.length() > maxLength) {
            originalText = originalText.substring(0, maxLength) + "...";
        }
        return new Text(originalText);
    }
}
