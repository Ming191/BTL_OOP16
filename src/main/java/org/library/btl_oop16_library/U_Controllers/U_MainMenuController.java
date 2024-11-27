package org.library.btl_oop16_library.U_Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IO;
import java.io.IOException;


public class U_MainMenuController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button bookListBtn;

    @FXML
    private Button bookLoansBtn;

    @FXML
    private void initialize() throws IOException {
        dashboardSceneLoader();
    }

    private void dashboardSceneLoader() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/DashboardView.fxml"));
        Pane pane = loader.load();
        mainPane.setCenter(pane);
    }
}
