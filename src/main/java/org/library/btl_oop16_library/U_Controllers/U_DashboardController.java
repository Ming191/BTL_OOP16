    package org.library.btl_oop16_library.U_Controllers;

    import atlantafx.base.controls.Card;
    import atlantafx.base.controls.ModalPane;
    import atlantafx.base.theme.Styles;
    import io.github.palexdev.materialfx.controls.MFXButton;
    import io.github.palexdev.materialfx.controls.MFXTableColumn;
    import io.github.palexdev.materialfx.controls.MFXTableView;
    import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
    import io.github.palexdev.materialfx.filter.StringFilter;
    import io.github.palexdev.mfxcore.collections.Grid;
    import javafx.beans.property.SimpleStringProperty;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.geometry.Pos;
    import javafx.scene.Node;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.chart.PieChart;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableView;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.*;
    import javafx.scene.shape.Rectangle;
    import org.library.btl_oop16_library.Controller.BookDetailsController;
    import org.library.btl_oop16_library.Controller.BookItemController;
    import org.library.btl_oop16_library.Controller.ServicesViewController;
    import org.library.btl_oop16_library.Model.Book;
    import org.library.btl_oop16_library.Model.User;
    import org.library.btl_oop16_library.Util.BookLoanDBConnector;
    import org.library.btl_oop16_library.Util.DBConnector;
    import org.library.btl_oop16_library.Util.SessionManager;
    import org.library.btl_oop16_library.Util.UserDBConnector;

    import java.io.IOException;
    import java.util.Comparator;
    import java.util.List;

    public class U_DashboardController {

        @FXML
        private TableView<User> adminInfoTable;

        @FXML
        private AnchorPane dashboardPane;

        @FXML
        private PieChart pieChart;

        @FXML
        private Card quoteCard;

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
            try {
                cardSetup();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/BookItem.fxml"));
                Parent bookItemRoot = loader.load();
                BookItemController bookItemController = loader.getController();

                bookItemController.setCard(book, dashboardPane,false);
                cardGrid.add(bookItemRoot, i, 0);
            }
        }


        @FXML
        void toBookLoansView(ActionEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/ServicesView.fxml"));
            Pane pane = loader.load();
            Scene scene = dashboardPane.getScene();
            BorderPane nodeToFind = (BorderPane) scene.lookup("#mainPane");
            nodeToFind.setCenter(pane);
        }

        @FXML
        void toBookView(ActionEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/BookView.fxml"));
            Pane pane = loader.load();
            Scene scene = dashboardPane.getScene();
            BorderPane nodeToFind = (BorderPane) scene.lookup("#mainPane");
            nodeToFind.setCenter(pane);
        }
    }
