package org.library.btl_oop16_library.controller.views;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.library.btl_oop16_library.controller.dialogs.UpdateUserDialogForAdminController;
import org.library.btl_oop16_library.model.User;
import org.library.btl_oop16_library.services.ExcelAPI;
import org.library.btl_oop16_library.utils.general.ApplicationAlert;
import org.library.btl_oop16_library.utils.database.UserDBConnector;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserViewController {
    UserDBConnector userDB = UserDBConnector.getInstance();

    @FXML
    private TableColumn<User, String> addressCol;


    @FXML
    private TableColumn<User, String> emailCol;


    @FXML
    private TableColumn<User, Integer> idCol;


    @FXML
    private VBox menuVbox;

    @FXML
    private MFXButton addUserButton;

    @FXML
    private MFXButton deleteUserButton;

    @FXML
    private MFXButton updateUserButton;

    @FXML
    private TextField searchField;

    @FXML
    private TableColumn<User, String> nameCol;

    @FXML
    private TableColumn<User, String> phoneCol;

    @FXML
    private ChoiceBox<String> typeSearchBox;

    @FXML
    private TableView<User> table;

    private User selectedUser;

    private static final UserDBConnector db = UserDBConnector.getInstance();

    private PauseTransition searchPause;


    @FXML
    private void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        table.getItems().setAll(userDB.importFromDB());
        searchPause = new PauseTransition(Duration.millis(500));
        searchPause.setOnFinished(event -> {
            realTimeSearch(searchField.getText());
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchPause.stop();
            searchPause.playFromStart();
        });

        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observableValue, User oldValue, User newValue) {
                selectedUser = newValue;
                updateUserButton.setDisable(selectedUser == null);
            }
        });

        deleteUserButton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());

        typeSearchBox.getItems().addAll("id", "name", "email", "phoneNumber");
        typeSearchBox.setValue("name");
    }

    private void realTimeSearch(String searchInput) {
        table.getItems().clear();
        List<User> userList = new ArrayList<>();
        String selectedType = typeSearchBox.getValue();

        switch (selectedType) {
            case "id":
                try {
                    int id = Integer.parseInt(searchInput);
                    userList = UserDBConnector.getInstance().searchById(id);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
            case "name":
                userList = UserDBConnector.getInstance().searchByAttributes(searchInput, "name");
                break;
            case "email":
                userList = UserDBConnector.getInstance().searchByAttributes(searchInput, "email");
                break;
            case "phoneNumber":
                userList = UserDBConnector.getInstance().searchByAttributes(searchInput, "phoneNumber");
                break;
            default:
                System.out.println("Invalid search type selected.");
        }

        if (!userList.isEmpty()) {
            table.getItems().addAll(userList);
        }

    }


    @FXML
    private void addUserButtonOnClick(ActionEvent event) throws IOException {
        Stage addUserStage = new Stage();
        addUserStage.setResizable(false);
        addUserStage.initModality(Modality.APPLICATION_MODAL);
        addUserStage.setTitle("Add User");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/fxml/dialogs/AddUserDialog.fxml"));
        try {
            Parent root = fxmlLoader.load();
            addUserStage.setScene(new Scene(root));
            Image favicon = new Image(getClass().getResource("/img/logo.png").toExternalForm())   ;
            addUserStage.getIcons().add(favicon);
            addUserStage.showAndWait();

            table.getItems().setAll(userDB.importFromDB());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void deleteUserButtonOnClick(ActionEvent event) throws IOException {
        User selectedUser = table.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            ApplicationAlert.missingInformation();
            return;
        }

        boolean result = ApplicationAlert.areYouSureAboutThat();
        if (result) {
            UserDBConnector.getInstance().deleteFromDB(selectedUser.getId());
            table.getItems().remove(selectedUser);
            ApplicationAlert.deleteSuccess();
        }
    }


    @FXML
    private void updateUserButtonOnClick(ActionEvent event) throws IOException {
        Stage updateUserStage = new Stage();
        updateUserStage.setResizable(false);
        updateUserStage.initModality(Modality.APPLICATION_MODAL);
        updateUserStage.setTitle("Update User");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/fxml/dialogs/UpdateUserDialogForAdmin.fxml"));

        try {
            Parent root = fxmlLoader.load();
            UpdateUserDialogForAdminController controller = fxmlLoader.getController();
            controller.setSelectedUser(selectedUser);
            updateUserStage.setScene(new Scene(root));
            Image favicon = new Image(getClass().getResource("/img/logo.png").toExternalForm())   ;
            updateUserStage.getIcons().add(favicon);
            updateUserStage.showAndWait();

            table.getItems().setAll(userDB.importFromDB());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @FXML
    void exportOnClick(ActionEvent event) {
        db.exportToExcel();
    }

    void refresh() throws SQLException {
        table.getItems().setAll(db.importFromDB());
    }

    @FXML
    void importOnClick(ActionEvent event) throws SQLException {
        ExcelAPI.importExcel(UserDBConnector.getInstance());
        refresh();
    }

}
