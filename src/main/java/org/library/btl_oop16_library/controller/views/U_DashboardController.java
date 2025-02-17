    package org.library.btl_oop16_library.controller.views;

    import eu.iamgio.animated.transition.AnimatedSwitcher;
    import io.github.palexdev.materialfx.controls.MFXButton;
    import javafx.application.Platform;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.chart.PieChart;
    import javafx.scene.control.Label;
    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableView;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.layout.*;
    import org.library.btl_oop16_library.controller.items.BookItemController;
    import org.library.btl_oop16_library.model.Book;
    import org.library.btl_oop16_library.model.User;
    import org.library.btl_oop16_library.utils.database.BookLoanDBConnector;
    import org.library.btl_oop16_library.utils.database.DBConnector;
    import org.library.btl_oop16_library.utils.general.GlobalVariables;
    import org.library.btl_oop16_library.utils.general.SessionManager;
    import org.library.btl_oop16_library.utils.database.UserDBConnector;

    import java.io.IOException;
    import java.util.List;

    import static org.library.btl_oop16_library.utils.general.GlobalVariables.*;

    public class U_DashboardController {

        @FXML
        private TableView<User> adminInfoTable;

        @FXML
        private AnchorPane dashboardPane;

        @FXML
        private PieChart pieChart;

        @FXML
        private VBox aiChatHolder;

        @FXML
        private GridPane cardGrid;

        @FXML
        private VBox chartHolder;

        @FXML
        private TableColumn<User, String> emailCol;

        @FXML
        private TableColumn<User, String> nameCol;

        @FXML
        private TableColumn<User, String> phoneCol;

        @FXML
        private MFXButton toBook;

        @FXML
        private MFXButton toBookLoans;


        @FXML
        private void initialize() {

            pieChartSetup();
            tableSetup();
            Platform.runLater(() -> {
                try {
                    cardSetup();
                    setupAIChatBox();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Scene scene = dashboardPane.getScene();
            });
        }

        private void pieChartSetup() {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            User currentUser = SessionManager.getInstance().getCurrentUser();

            int userId = currentUser.getId();

            int returnedCount = DBConnector.getCount("SELECT COUNT(*) FROM bookLoans WHERE status = 'returned' and userId = " + userId);
            int notReturnedCount = DBConnector.getCount("SELECT COUNT(*) FROM bookLoans WHERE status = 'not returned' AND userId = " + userId);
            int preOrderedCount = DBConnector.getCount("SELECT COUNT(*) FROM bookLoans WHERE status = 'pre-ordered' AND userId = " + userId);

            if(returnedCount != 0 || notReturnedCount != 0 || preOrderedCount != 0) {
                pieChartData.add(new PieChart.Data("returned", returnedCount));
                pieChartData.add(new PieChart.Data("not returned", notReturnedCount));
                pieChartData.add(new PieChart.Data("pre-ordered", preOrderedCount));

                pieChart.setData(pieChartData);
                pieChart.setTitle("Book Loans Status");
                pieChart.setLegendVisible(true);
                pieChart.setLabelsVisible(true);
            } else {
                chartHolder.getChildren().remove(pieChart);
                Label label = new Label("You haven't borrowed any book yet...");
                label.setWrapText(true);
                label.setMaxWidth(360);
                label.getStyleClass().add("label-on-emphasis");
                chartHolder.getChildren().add(label);
            }
        }
        private void tableSetup() {
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

            adminInfoTable.setItems(FXCollections.observableList(UserDBConnector.getInstance().getAdminData()));
        }
        private void cardSetup() throws IOException {
            List<Book> books = BookLoanDBConnector.getInstance().getTop3Books();

            for (int i = 0; i< books.size(); i++) {
                Book book = books.get(i);
                FXMLLoader loader = new FXMLLoader(getClass().getResource(BOOK_ITEM_PATH));
                Parent bookItemRoot = loader.load();
                BookItemController bookItemController = loader.getController();

                bookItemController.setCard(book, dashboardPane, "viewDetails");
                cardGrid.add(bookItemRoot, i, 0);
            }
        }


        @FXML
        void toBookLoansView(ActionEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(SERVICES_VIEW_PATH));
            Pane pane = loader.load();
            Scene scene = dashboardPane.getScene();
            AnimatedSwitcher nodeToFind = (AnimatedSwitcher) scene.lookup("#switcher");
            nodeToFind.setChild(pane);
        }

        @FXML
        void toBookView(ActionEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(BOOK_VIEW_PATH));
            Pane pane = loader.load();
            Scene scene = dashboardPane.getScene();
            AnimatedSwitcher nodeToFind = (AnimatedSwitcher) scene.lookup("#switcher");
            nodeToFind.setChild(pane);
        }

        private void setupAIChatBox() throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(GlobalVariables.AICHATBOX_PATH));
            Pane pane = loader.load();
            aiChatHolder.getChildren().add(pane);
        }
    }
