package org.library.btl_oop16_library.Factory;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.library.btl_oop16_library.Controller.CommentBoxController;
import org.library.btl_oop16_library.Controller.CommentItemController;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Model.Comment;

import java.io.IOException;

import static org.library.btl_oop16_library.Util.GlobalVariables.COMMENT_BOX_PATH;
import static org.library.btl_oop16_library.Util.GlobalVariables.COMMENT_ITEM_PATH;

public class CommentPaneFactory {
    public static Pane createCommentPane(Comment comment) {
        FXMLLoader loader = new FXMLLoader(CommentPaneFactory.class.getResource(COMMENT_ITEM_PATH));
        Pane pane;
        try {
            pane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load comment pane FXML.", e);
        }
        CommentItemController controller = loader.getController();
        controller.commentItemSetup(comment);
        return pane;
    }

    public static Pane createCommentBox(Book book, Runnable refreshCallback) {
        FXMLLoader loader = new FXMLLoader(CommentPaneFactory.class.getResource(COMMENT_BOX_PATH));
        Pane pane;
        try {
            pane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load comment box FXML.", e);
        }
        CommentBoxController controller = loader.getController();
        controller.setup(book, refreshCallback);
        return pane;
    }
}
