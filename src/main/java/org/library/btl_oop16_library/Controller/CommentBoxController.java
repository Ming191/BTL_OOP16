package org.library.btl_oop16_library.Controller;

import atlantafx.base.theme.Styles;
import jakarta.mail.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.poi.xssf.model.StylesTable;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Model.Comment;
import org.library.btl_oop16_library.Util.CommentsDBConnector;
import org.library.btl_oop16_library.Util.SessionManager;

import java.sql.SQLException;

public class CommentBoxController {
    @FXML
    private TextArea commentField;

    @FXML
    private VBox commentHolder;

    @FXML
    private Text username;

    @FXML
    private Button submitButton;

    private Book book;

    private Runnable refreshCallback;

    public void setup(Book book, Runnable refreshCallback) {
        username.setText(SessionManager.getInstance().getCurrentUser().getName());
        this.book = book;
        this.refreshCallback = refreshCallback;
    }

    @FXML
    public void submit(ActionEvent event) throws SQLException {
        if(commentField.getText().isEmpty()) {
            commentField.pseudoClassStateChanged(Styles.STATE_DANGER,true);
            return;
        }
        commentField.pseudoClassStateChanged(Styles.STATE_SUCCESS,true);
        CommentsDBConnector.getInstance().addToDB(new Comment(book.getId(), SessionManager.getInstance().getCurrentUser().getId(), commentField.getText()));
        commentField.clear();
        if(refreshCallback != null) {
            refreshCallback.run();
        }
    }
}
