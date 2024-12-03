package org.library.btl_oop16_library.controller.items;

import io.github.palexdev.mfxcore.controls.Text;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.library.btl_oop16_library.model.Comment;
import org.library.btl_oop16_library.utils.database.UserDBConnector;

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
