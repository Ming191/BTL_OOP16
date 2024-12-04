package org.library.btl_oop16_library.utils.general;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.library.btl_oop16_library.controller.general.AlertController;

import java.io.IOException;

public class ApplicationAlert {
    private static final String ERROR_ICON = "mdal-error";
    private static final String INFORMATION_ICON = "mdal-info";
    private static final String CONFIRMATION_ICON = "mdal-check_box";
    private static final String FAVICON = "/img/logo.png";

    public static boolean showAlert(String title, String message, String iconPath, boolean showCancelButton) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationAlert.class.getResource("/org/library/btl_oop16_library/fxml/general/Alert.fxml"));
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
        showAlert("Invalid Quantity!", "Please provide a valid number.",
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

    public static void overMaxQuantity() {
        showAlert("Can not lending!", "Can not borrowed more than 20 books at a time!", ERROR_ICON, false);
    }

    public static void passwordMismatch() {
        showAlert("Password Mismatch!", "New password and confirmation do not match.", ERROR_ICON, false);
    }

    public static void wrongPassword() {
        showAlert("Invalid Password!", "Please provide the right password!", ERROR_ICON, false);
    }

    public static void exportSuccess() {
        showAlert("Export Success!", "Data has been exported!", INFORMATION_ICON, false);
    }

    public static void importSuccess() {
        showAlert("Import Success!", "Data has been imported!", INFORMATION_ICON, false);
    }

    public static void importFailed() {
        showAlert("Import Failed!", "Data has not been imported!", ERROR_ICON, false);
    }

    public static void exportFailed() {
        showAlert("Export Failed!", "Data has not been exported!", ERROR_ICON, false);
    }

    public static void invalidTimeRange() {
        showAlert("Invalid Time Range!", "Please provide a valid time range.", ERROR_ICON, false);
    }

    public static void deleteSuccess() {
        showAlert("Action success!","Data has been deleted", INFORMATION_ICON, false);
    }

    public static void invalidPhoneNumber() {
        showAlert("Invalid Phone Number!", "Please provide a valid phone number!", ERROR_ICON, false);
    }

    public static void weakPassword() {
        showAlert("Weak Password!", "Please provide a stronger password!", ERROR_ICON, false);
    }

    public static void bookIsReturned() {
        showAlert("This book is returned!", "Please choose another.", ERROR_ICON, false);
    }

    public static void addSuccess() {
        showAlert("Action success!","Data has been added", INFORMATION_ICON, false);
    }

    public static void showFullDetails(String logEntry) {
        showAlert("Activity Details!",logEntry,INFORMATION_ICON, false);
    }

    public static void emailSent() {
        showAlert("Action success!","Email has been sent", INFORMATION_ICON, false);
    }

    public static void emailFailed() {
        showAlert("Can not send emails!","Try again -_-", ERROR_ICON, false);
    }

    public static void preorderSuccess() {
        showAlert("Action success!","You have pre-ordered this book.", INFORMATION_ICON, false);
    }
}
