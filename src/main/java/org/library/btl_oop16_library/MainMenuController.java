package org.library.btl_oop16_library;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class MainMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button menuBook;

    @FXML
    private Button menuDashboard;

    @FXML
    private Button menuUser;

    @FXML
    private VBox menuVbox;

    @FXML
    void handleMouseClick(MouseEvent event) {
        for (Node button : menuVbox.getChildren().filtered(node -> node instanceof Button)) {
            button.getStyleClass().remove("selected");
        }

        Button selectedButton = (Button) event.getSource();
        selectedButton.getStyleClass().add("selected");
    }

    @FXML
    void initialize() {
        assert menuBook != null : "fx:id=\"menuBook\" was not injected: check your FXML file 'mainMenu.fxml'.";
        assert menuDashboard != null : "fx:id=\"menuDashboard\" was not injected: check your FXML file 'mainMenu.fxml'.";
        assert menuUser != null : "fx:id=\"menuUser\" was not injected: check your FXML file 'mainMenu.fxml'.";
        assert menuVbox != null : "fx:id=\"menuVbox\" was not injected: check your FXML file 'mainMenu.fxml'.";

    }



}
