package org.library.btl_oop16_library.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Util.DatabaseConnector;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Model.UserList;

import java.io.IOException;

public class UserViewController {
    DatabaseConnector userDB;
    UserList userList;

    @FXML
    private TableColumn<User, String> accountCol;

    @FXML
    private TableColumn<?, ?> actionCol;

    @FXML
    private Button menuCatalog;

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
    void initialize() {
        userDB = new DatabaseConnector();
        userList = new UserList();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        accountCol.setCellValueFactory(new PropertyValueFactory<>("account"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));

        userDB.selectUsersFromDB(userList);
        table.getItems().addAll(userList.getUsers());
    }

    @FXML
    void addUserButtonOnClick(ActionEvent event) throws IOException {
        Stage adduserstage = new Stage();
        adduserstage.setResizable(false);
        adduserstage.initModality(Modality.APPLICATION_MODAL);
        adduserstage.setTitle("Add User");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/AddUserDialog.fxml"));
        try {
            Parent root = fxmlLoader.load();
            adduserstage.setScene(new Scene(root));
            adduserstage.showAndWait();

            table.getItems().clear();
            userList.getUsers().clear();

            userDB.selectUsersFromDB(userList);
            table.getItems().addAll(userList.getUsers());
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

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/DeleteUserDialog.fxml"));

        try {
            Parent root = fxmlLoader.load();
            deleteuserstage.setScene(new Scene(root));
            deleteuserstage.showAndWait();

            table.getItems().clear();
            userList.getUsers().clear();

            userDB.selectUsersFromDB(userList);
            table.getItems().addAll(userList.getUsers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
