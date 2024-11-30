package org.library.btl_oop16_library.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.library.btl_oop16_library.Util.SessionManager;

public class CommentBoxController {
    @FXML
    private TextField commentField;

    @FXML
    private VBox commentHolder;

    @FXML
    private Text username;

    @FXML
    private Button submitButton;

    public void setup() {
        username.setText(SessionManager.getInstance().getCurrentUser().getName());
    }
}
