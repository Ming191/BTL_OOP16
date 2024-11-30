package org.library.btl_oop16_library.Controller;

import io.github.palexdev.mfxcore.controls.Text;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Model.Comment;
import org.library.btl_oop16_library.Util.UserDBConnector;

public class CommentItemController {
    @FXML
    private VBox commentHolder;

    @FXML
    private AnchorPane commentItem;

    @FXML
    private Text context;

    @FXML
    private Text username;

    public void commentItemSetup(Comment comment) {
        username.setText(UserDBConnector.getInstance().getUserByID(comment.getUserId()).getName());
        context.setText(comment.getContext());
    }
}
