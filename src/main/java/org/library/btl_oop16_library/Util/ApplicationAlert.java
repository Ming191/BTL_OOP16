package org.library.btl_oop16_library.Util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.library.btl_oop16_library.Controller.AlertController;

import java.io.IOException;

public class ApplicationAlert {
    private static final String ERROR_ICON = "mdal-error";
    private static final String INFORMATION_ICON = "mdal-info";
    private static final String CONFIRMATION_ICON = "mdal-check_box";
    private static final String FAVICON = "/img/logo_2min.png";

    public static boolean showAlert(String title, String message, String iconPath, boolean showCancelButton) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationAlert.class.getResource("/org/library/btl_oop16_library/view/Alert.fxml"));
            Parent root = loader.load();

            AlertController controller = loader.getController();
            controller.setTitle(title);
            controller.setMessage(message);
            controller.setIcon(iconPath);
            controller.setShowCancelButton(showCancelButton);

            Stage stage = new Stage();
            stage.setTitle("2Min Library");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image(ApplicationAlert.class.getResourceAsStream(FAVICON)));
            stage.showAndWait();

            return controller.isConfirmed();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void emptyAccountOrPassword() {
        showAlert("Empty account or password!",
                "Please fulfill account and password!",
                ERROR_ICON, false);
    }

    public static void wrongUsernameOrPassword() {
        showAlert("Wrong username or password!",
                "Please enter a valid username or password!",
                ERROR_ICON, false);
    }

    public static void userAlreadyExists() {
        showAlert("User Already Exists!",
                "Please enter a unique username!",
                ERROR_ICON, false);
    }

    public static void notFound() {
        showAlert("Not Found!", "The requested ID does not exist!",
                ERROR_ICON, false);
    }

    public static void updateSuccess() {
        showAlert("Update Success!", "Your changes have been saved!",
                INFORMATION_ICON, false);
    }

    public static void missingInformation() {
        showAlert("Missing Information!", "Please provide all necessary information.",
                ERROR_ICON, false);
    }

    public static void wrongEmailPattern() {
        showAlert("Invalid Email!", "Please use a valid email address.",
                ERROR_ICON, false);
    }

    public static void invalidQuantity() {
        showAlert("Invalid Quantity!", "Please provide a positive number.",
                ERROR_ICON, false);
    }

    public static boolean areYouSureAboutThat() {
        return showAlert("Confirmation Needed", "Are you sure about that?", CONFIRMATION_ICON, true);
    }

    public static void signUpSuccess() {
        showAlert("Sign Up Success!", "Your account has been created!", INFORMATION_ICON, false);
    }

    public static void canNotLendBook() {
        showAlert("Can Not Preorder!", "Your borrowed book is less than 20!", ERROR_ICON, false);
    }

    public static void passwordMismatch() {
        showAlert("Password Mismatch!", "New password and confirmation do not match.", ERROR_ICON, false);
    }

    public static void wrongPassword() {
        showAlert("Invalid Password!", "Please provide the right password!", ERROR_ICON, false);
    }

}
