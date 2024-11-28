package org.library.btl_oop16_library.Controller;

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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.BookDBConnector;
import org.library.btl_oop16_library.Util.UserDBConnector;

import java.io.File;
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
    private Button addUserButton;

    @FXML
    private Button deleteUserButton;

    @FXML
    private Button updateUserButton;

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
        List<User> usersByName = UserDBConnector.getInstance().searchByName(searchInput);
        List<User> usersById = new ArrayList<>();
        try {
            int id = Integer.parseInt(searchInput);
            usersById = UserDBConnector.getInstance().searchById(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (!usersById.isEmpty()) {
            table.getItems().addAll(usersById);
        }

        if (!usersByName.isEmpty()) {
            table.getItems().addAll(usersByName);
        }
    }


    @FXML
    private void addUserButtonOnClick(ActionEvent event) throws IOException {
        Stage addUserStage = new Stage();
        addUserStage.setResizable(false);
        addUserStage.initModality(Modality.APPLICATION_MODAL);
        addUserStage.setTitle("Add User");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/AddUserDialog.fxml"));
        try {
            Parent root = fxmlLoader.load();
            addUserStage.setScene(new Scene(root));
            Image favicon = new Image(getClass().getResource("/img/logo_2min.png").toExternalForm())   ;
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

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/UpdateUserDialogForAdmin.fxml"));

        try {
            Parent root = fxmlLoader.load();
            UpdateUserDialogForAdminController controller = fxmlLoader.getController();
            controller.setSelectedUser(selectedUser);
            updateUserStage.setScene(new Scene(root));
            Image favicon = new Image(getClass().getResource("/img/logo_2min.png").toExternalForm())   ;
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
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter excelFilter = new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(excelFilter);

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            db.importFromExcel(filePath);
        } else {
            System.out.println("No file selected.");
        }
        refresh();
    }

}
