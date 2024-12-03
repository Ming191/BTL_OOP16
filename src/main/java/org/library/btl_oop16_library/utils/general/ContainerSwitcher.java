package org.library.btl_oop16_library.utils.general;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class ContainerSwitcher {
    public static void switchView(Node root, String fxmlPath, String parentID) {
        FXMLLoader fxmlLoader = new FXMLLoader(ContainerSwitcher.class.getResource(fxmlPath));
        Node target = null;
        try {
            target = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Node container = root.getScene().lookup("#" + parentID);
        if (container instanceof Pane) {
            Pane pane = (Pane) container;
            pane.getChildren().setAll(target);
            fitParentToTarget(pane, target);
        }
    }
    private static void fitParentToTarget(Pane parent, Node target) {
        parent.setPrefWidth(target.prefWidth(-1));
        parent.setPrefHeight(target.prefHeight(-1));

        target.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            parent.setPrefWidth(newBounds.getWidth());
            parent.setPrefHeight(newBounds.getHeight());
        });
    }

}
