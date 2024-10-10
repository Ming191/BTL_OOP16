package org.library.btl_oop16_library;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class LoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private BorderPane rootPane;


    @FXML
    public void SwitchToLoginScene(ActionEvent event) throws IOException {
        Node currentRoot = ((Node) event.getSource()).getScene().getRoot();
        makeFadeOut(currentRoot, "login.fxml",event);
    }

    @FXML
    public void SwitchToSignUpScene(ActionEvent event) throws IOException {
        Node currentRoot = ((Node)(event.getSource())).getScene().getRoot();
        makeFadeOut(currentRoot,"signup.fxml",event);
//        Parent root = FXMLLoader.load(getClass().getResource("signUp.fxml"));
//        stage = (Stage)((Node)(event.getSource())).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
    }

    private void makeFadeOut(Node node, String fxmlFile, ActionEvent event) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setNode(node);
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        fadeTransition.setOnFinished(e -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource(fxmlFile)); // Load new scene from the provided FXML file
                stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        fadeTransition.play();

    }

}