package org.library.btl_oop16_library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

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
    private Button logOut;

    @FXML
    private Button menuBook;

    @FXML
    private Button menuDashboard;

    @FXML
    private Button menuUser;


    @FXML
    private VBox menuVbox;

    @FXML
    private Button addUserButton;

    @FXML
    private TableColumn<User, String> nameCol;

    @FXML
    private TableColumn<User, String> passwordCol;

    @FXML
    private TableView<User> table;

    @FXML
    void addUserButtonOnClick(ActionEvent event) throws IOException {
        Stage adduserstage = new Stage();
        adduserstage.setResizable(false);
        adduserstage.initModality(Modality.APPLICATION_MODAL);
        adduserstage.setTitle("Add User");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addUserDialog.fxml"));
        try {
            Parent root = fxmlLoader.load();
            adduserstage.setScene(new Scene(root));
            adduserstage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void deleteUserButtonOnClick(ActionEvent event) throws IOException {
        Stage deleteuserstage = new Stage();
        deleteuserstage.setResizable(false);
        deleteuserstage.initModality(Modality.APPLICATION_MODAL);
        deleteuserstage.setTitle("Delete User");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("deleteUserDialog.fxml"));

        try {
            Parent root = fxmlLoader.load();
            deleteuserstage.setScene(new Scene(root));
            deleteuserstage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
