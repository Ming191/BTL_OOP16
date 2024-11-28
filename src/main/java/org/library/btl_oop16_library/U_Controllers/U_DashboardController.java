    package org.library.btl_oop16_library.U_Controllers;

    import atlantafx.base.controls.Card;
    import atlantafx.base.controls.ModalPane;
    import atlantafx.base.theme.Styles;
    import io.github.palexdev.materialfx.controls.MFXTableColumn;
    import io.github.palexdev.materialfx.controls.MFXTableView;
    import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
    import io.github.palexdev.materialfx.filter.StringFilter;
    import io.github.palexdev.mfxcore.collections.Grid;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.geometry.Pos;
    import javafx.scene.Parent;
    import javafx.scene.chart.PieChart;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.AnchorPane;
    import javafx.scene.layout.GridPane;
    import javafx.scene.layout.StackPane;
    import javafx.scene.shape.Rectangle;
    import org.library.btl_oop16_library.Controller.BookDetailsController;
    import org.library.btl_oop16_library.Controller.BookItemController;
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
        private MFXTableView<User> adminInfoTable;

        @FXML
        private AnchorPane dashboardPane;

        @FXML
        private PieChart pieChart;

        @FXML
        private Card quoteCard;

        @FXML
        private GridPane cardGrid;

        @FXML
        private void initialize() {
            pieChartSetup();
            tableSetup();
            try {
                cardSetup();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            adminInfoTable.autosizeColumnsOnInitialization();
        }

        private void pieChartSetup() {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            User currentUser = SessionManager.getInstance().getCurrentUser();

            int userId = currentUser.getId();

            int returnedCount = DBConnector.getCount("SELECT COUNT(*) FROM bookLoans WHERE status = 'returned' and userId = " + userId);
            int notReturnedCount = DBConnector.getCount("SELECT COUNT(*) FROM bookLoans WHERE status = 'not returned' AND userId = " + userId);
            int preOrderedCount = DBConnector.getCount("SELECT COUNT(*) FROM bookLoans WHERE status = 'pre-ordered' AND userId = " + userId);

            pieChartData.add(new PieChart.Data("returned", returnedCount));
            pieChartData.add(new PieChart.Data("not returned", notReturnedCount));
            pieChartData.add(new PieChart.Data("pre-ordered", preOrderedCount));

            pieChart.setData(pieChartData);
            pieChart.setTitle("Book Loans Status");
            pieChart.setLegendVisible(true);
            pieChart.setLabelsVisible(true);
        }
        private void tableSetup() {
            MFXTableColumn<User> nameColumn = new MFXTableColumn<>("Name", true, Comparator.comparing(User::getName));
            MFXTableColumn<User> emailColumn = new MFXTableColumn<>("Email", true, Comparator.comparing(User::getEmail));
            MFXTableColumn<User> phoneColumn = new MFXTableColumn<>("Phone Number", true, Comparator.comparing(User::getPhoneNumber));

            nameColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getName));
            emailColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getEmail));
            phoneColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getPhoneNumber));
            adminInfoTable.getTableColumns().addAll(nameColumn, emailColumn, phoneColumn);
            adminInfoTable.getFilters().addAll(
                    new StringFilter<>("Name", User::getName),
                    new StringFilter<>("Email", User::getEmail),
                    new StringFilter<>("PhoneNumber", User::getPhoneNumber)
            );
            adminInfoTable.getItems().addAll(UserDBConnector.getAdminData());
        }
        private void cardSetup() throws IOException {
            List<Book> books = BookLoanDBConnector.getInstance().getTop3Books();

            for (int i = 0; i< books.size(); i++) {
                Book book = books.get(i);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/library/btl_oop16_library/view/BookItem.fxml"));
                Parent bookItemRoot = loader.load();
                BookItemController bookItemController = loader.getController();
                bookItemController.setCard(book, dashboardPane);
                cardGrid.add(bookItemRoot, i, 0);
            }
        }
    }
