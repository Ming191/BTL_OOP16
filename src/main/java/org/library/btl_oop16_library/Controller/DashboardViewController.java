package org.library.btl_oop16_library.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import org.kordamp.ikonli.javafx.FontIcon;
import org.library.btl_oop16_library.Model.Activity;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Util.ActivitiesDBConnector;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.DBConnector;
import org.library.btl_oop16_library.Util.UserDBConnector;

import java.awt.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class DashboardViewController {
    @FXML
    private Label adminCountLabel;

    @FXML
    private Label bookCountLabel;

    @FXML
    private Label userCountLabel;

    @FXML
    private PieChart loanStatusChart;

    @FXML
    private ListView<String> activityListView;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TableColumn<User, String> adminEmailCol;

    @FXML
    private TableColumn<User, String> adminNameCol;

    @FXML
    private TableView<User> adminInforTableView;

    @FXML
    private TableColumn<User, String> adminPhoneNumberCol;

    @FXML
    private FontIcon icon;

    private ObservableList<String> activityList;

    public void initialize() {
        int totalUsers = DBConnector.getCount("SELECT COUNT(*) FROM user");
        int totalAdmins = DBConnector.getCount("SELECT COUNT(*) FROM user WHERE role = 'admin'");
        int totalBooks = DBConnector.getCount("SELECT SUM(quantity) FROM book");

        userCountLabel.setText(String.valueOf(totalUsers));
        adminCountLabel.setText(String.valueOf(totalAdmins));
        bookCountLabel.setText(String.valueOf(totalBooks));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        int returnedCount = DBConnector.getCount("SELECT COUNT(*) FROM bookLoans WHERE status = 'returned'");
        System.out.println(returnedCount);
        int notReturnedCount = DBConnector.getCount("SELECT COUNT(*) FROM bookLoans WHERE status = 'not returned'");
        System.out.println(notReturnedCount);
        int preOrderedCount = DBConnector.getCount("SELECT COUNT(*) FROM bookLoans WHERE status = 'pre-ordered'");
        System.out.println(preOrderedCount);

        pieChartData.add(new PieChart.Data("returned", returnedCount));
        pieChartData.add(new PieChart.Data("not returned", notReturnedCount));
        pieChartData.add(new PieChart.Data("pre-ordered", preOrderedCount));

        loanStatusChart.setData(pieChartData);
        loanStatusChart.setTitle("Book Loans Status");
        loanStatusChart.setLegendVisible(true);
        loanStatusChart.setLabelsVisible(true);

        loadActivities();

        UserDBConnector userDB = UserDBConnector.getInstance();
        adminNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        adminEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        adminPhoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        adminInforTableView.getItems().setAll(userDB.getAdminData());

        icon.setOnMouseClicked(event -> {
            searchActivitiesByDate();
        });
    }

    private void loadActivities() {
        activityList = FXCollections.observableArrayList();
        ActivitiesDBConnector activitiesDB = new ActivitiesDBConnector();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            List<Activity> activities = activitiesDB.importFromDB();
            for (Activity activity : activities) {
                String formattedTimestamp = activity.getTimestamp().format(formatter);
                activityList.add(activity.getDescription() + " at " + formattedTimestamp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        activityListView.setItems(activityList);
    }

    private void searchActivitiesByDate() {
        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            System.out.println("Please select both start and end dates.");
            ApplicationAlert.invalidTimeRange();
            return;
        }

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate.isAfter(endDate)) {
            ApplicationAlert.invalidTimeRange();
            return;
        }

        LocalDateTime startTimestamp = startDate.atStartOfDay();
        LocalDateTime endTimestamp = endDate.atTime(23, 59, 59);

        ActivitiesDBConnector activitiesDB = new ActivitiesDBConnector();
        try {
            List<Activity> activities = activitiesDB.searchByTimeRange(startTimestamp, endTimestamp);
            activityList.clear();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (Activity activity : activities) {
                String formattedTimestamp = activity.getTimestamp().format(formatter);
                activityList.add(activity.getDescription() + " at " + formattedTimestamp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
