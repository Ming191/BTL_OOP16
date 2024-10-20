package org.library.btl_oop16_library;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class UserViewController {

    @FXML
    private TableColumn<User, String> accountCol;

    @FXML
    private TableColumn<?, ?> actionCol;

    @FXML
    private TableColumn<User, String> emailCol;

    @FXML
    private TableColumn<User, Integer> idCol;

    @FXML
    private ButtonBar menuBook;

    @FXML
    private ButtonBar menuBook1;

    @FXML
    private ButtonBar menuDashboard;

    @FXML
    private ButtonBar menuUser;

    @FXML
    private VBox menuVbox;

    @FXML
    private ButtonBar addUserButton;

    @FXML
    private TableColumn<User, String> nameCol;

    @FXML
    private TableColumn<User, String> passwordCol;

    @FXML
    private TableView<User> table;

}
