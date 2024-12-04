package org.library.btl_oop16_library.controller.dialogs;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.btl_oop16_library.model.Book;
import org.library.btl_oop16_library.utils.database.BookDBConnector;

public class ModifyBookDialogController {

    @FXML
    private TextField authorField;

    @FXML
    private MFXButton cancelButton;

    @FXML
    private MFXButton confirmButton;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField titleField;

    private Book currentBook;

    public void setCurrentBook(Book currentBook) {
        this.currentBook = currentBook;
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            titleField.setText(currentBook.getTitle());
            authorField.setText(currentBook.getAuthor());
            descriptionField.setText(currentBook.getDescription());
            quantityField.setText(String.valueOf(currentBook.getAvailable()));
        });
    }

    @FXML
    void onCancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onConfirmButtonClick(ActionEvent event) {
        String author = authorField.getText();
        String description = descriptionField.getText();
        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String title = titleField.getText();

        currentBook.setAuthor(author);
        currentBook.setDescription(description);
        currentBook.setAvailable(quantity);
        currentBook.setTitle(title);
        try {
            BookDBConnector.getInstance().modifyBook(currentBook);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
}
