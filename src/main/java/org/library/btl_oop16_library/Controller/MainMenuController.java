package org.library.btl_oop16_library.Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.SessionManager;
import org.library.btl_oop16_library.Util.Transition;

public class MainMenuController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Button logOutButton;

    @FXML
    private Button menuBook;

    @FXML
    private Label clockField;

    @FXML
    private Button menuCatalog;

    @FXML
    private Button menuDashboard;

    @FXML
    private Button menuUser;

    @FXML
    private VBox menuVbox;

    @FXML
    private Button menuSettings;

    private void initializeRoleBasedAccess() {
        menuUser.setVisible(SessionManager.getInstance().getCurrentUser().getRole().equals("admin"));
    }

    private void startClock() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy");

        LocalDateTime now = LocalDateTime.now();
        clockField.setText(now.format(formatter));

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime current = LocalDateTime.now();
            clockField.setText(current.format(formatter));
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void switchToBook(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/BookView.fxml"));
        Pane pane = loader.load();
        BookViewController bookViewController = loader.getController();
        mainPane.setCenter(pane);
    }

    @FXML
    private void switchToCatalog(ActionEvent event) throws IOException  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/ServicesView.fxml"));
        Pane pane = loader.load();
        ServicesViewController servicesViewController = loader.getController();
        mainPane.setCenter(pane);
    }

    @FXML
    private void switchToDashboard(ActionEvent event) throws IOException {
        FXMLLoader loader = null;
        if(SessionManager.getInstance().getCurrentUser().getRole().equals("admin")) {
            loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/DashboardView.fxml"));
        } else {
            loader = new FXMLLoader(getClass().getResource("/UserFXMLs/U_Dashboard.fxml"));

        }
        Pane pane = loader.load();
        mainPane.setCenter(pane);
    }

    @FXML
    private void switchToUser(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane pane = loader.load(getClass().getResource("/org/library/btl_oop16_library/view/UserView.fxml"));
        mainPane.setCenter(pane);
    }

    @FXML
    private void logOut(ActionEvent event) throws IOException {
        boolean result = ApplicationAlert.areYouSureAboutThat();
        if (result) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/Login.fxml"));
            Scene loginScene = new Scene(loader.load());
            Transition.fadeTransition((Stage) logOutButton.getScene().getWindow(), logOutButton.getScene(), loginScene);
        }
        return;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        for (Node button : menuVbox.getChildren().filtered(node -> node instanceof Button)) {
            button.getStyleClass().remove("selected");
        }

        Button selectedButton = (Button) event.getSource();
        selectedButton.getStyleClass().add("selected");
    }

    @FXML
    public void switchToSettings(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/Settings.fxml"));
            Pane pane = loader.load();
            mainPane.setCenter(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        startClock();
        FXMLLoader loader = null;
        if(SessionManager.getInstance().getCurrentUser().getRole().equals("admin")) {
             loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/DashboardView.fxml"));
        } else {
             loader = new FXMLLoader(getClass().getResource("/UserFXMLs/U_Dashboard.fxml"));
        }

        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mainPane.setCenter(pane);
        initializeRoleBasedAccess();
    }


}
