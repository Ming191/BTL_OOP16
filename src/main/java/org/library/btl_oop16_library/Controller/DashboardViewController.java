package org.library.btl_oop16_library.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import org.kordamp.ikonli.javafx.FontIcon;
import org.library.btl_oop16_library.Model.Activity;
import org.library.btl_oop16_library.Util.ActivitiesDBConnector;
import org.library.btl_oop16_library.Util.ApplicationAlert;
import org.library.btl_oop16_library.Util.DBConnector;
import org.library.btl_oop16_library.Util.UserDBConnector;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private StackedBarChart<String, Number> stackedBarChart;

    @FXML
    private CategoryAxis horizontalAxis;

    @FXML
    private NumberAxis verticalAxis;

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
        int notReturnedCount = DBConnector.getCount("SELECT COUNT(*) FROM bookLoans WHERE status = 'not returned'");
        int preOrderedCount = DBConnector.getCount("SELECT COUNT(*) FROM bookLoans WHERE status = 'pre-ordered'");

        pieChartData.add(new PieChart.Data("Returned", returnedCount));
        pieChartData.add(new PieChart.Data("Not Returned", notReturnedCount));
        pieChartData.add(new PieChart.Data("Pre-ordered", preOrderedCount));

        loanStatusChart.setData(pieChartData);
        loanStatusChart.setTitle("Book loans status");
        loanStatusChart.setLegendVisible(true);
        loanStatusChart.setLabelsVisible(true);

        loadActivities();

        UserDBConnector userDB = UserDBConnector.getInstance();

        icon.setOnMouseClicked(event -> {
            searchActivitiesByDate();
        });

        loadStackedBarChartData();
    }

    private void loadStackedBarChartData() {
        stackedBarChart.setTitle("Activities Overview");
        horizontalAxis.setLabel("Date");
        verticalAxis.setLabel("Count");
        verticalAxis.setTickUnit(5);

        XYChart.Series<String, Number> newUserSeries = new XYChart.Series<>();
        newUserSeries.setName("New Users");

        XYChart.Series<String, Number> lendingSeries = new XYChart.Series<>();
        lendingSeries.setName("Lending Count");

        XYChart.Series<String, Number> newBookSeries = new XYChart.Series<>();
        newBookSeries.setName("New Books");

        String query = """
                SELECT strftime('%Y/%m/%d', timestamp) AS date, 
                       COUNT(CASE WHEN description LIKE 'User % added' THEN 1 END) AS newUsersAdded, 
                       COUNT(CASE WHEN description LIKE 'User % borrowed%' THEN 1 END) AS lendingCount,
                       COUNT(CASE WHEN description LIKE '%new book%' THEN 1 END) AS newBooksAdded
                FROM activities 
                GROUP BY strftime('%Y/%m/%d', timestamp) 
                ORDER BY date
                """;

        try (Connection con = DBConnector.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String date = rs.getString("date");
                int newUsersAdded = rs.getInt("newUsersAdded");
                int lendingCount = rs.getInt("lendingCount");
                int newBooksAdded = rs.getInt("newBooksAdded");

                newUserSeries.getData().add(new XYChart.Data<>(date, newUsersAdded));
                lendingSeries.getData().add(new XYChart.Data<>(date, lendingCount));
                newBookSeries.getData().add(new XYChart.Data<>(date, newBooksAdded));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        stackedBarChart.getData().clear();
        stackedBarChart.getData().addAll(newUserSeries, lendingSeries, newBookSeries);
    }


    private void loadActivities() {
        activityList = FXCollections.observableArrayList();
        ActivitiesDBConnector activitiesDB = ActivitiesDBConnector.getInstance();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            List<Activity> activities = activitiesDB.importFromDB();
            for (Activity activity : activities) {
                String formattedTimestamp = activity.getTimestamp().format(formatter);
                String fullDescription = activity.getDescription() + " at " + formattedTimestamp;
                String shortenedDescription = fullDescription.length() > 46 ? fullDescription.substring(0, 46) + "..." : fullDescription;

                activityList.add(shortenedDescription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        activityListView.setItems(activityList);

        activityListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                String selectedActivity = activityListView.getSelectionModel().getSelectedItem();
                if (selectedActivity != null) {
                    String fullDescription = getFullDescription(selectedActivity);

                    ApplicationAlert.showFullDetails(fullDescription);
                }
            }
        });
    }


    private String getFullDescription(String shortenedDescription) {
        ActivitiesDBConnector activitiesDB = ActivitiesDBConnector.getInstance();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            List<Activity> activities = activitiesDB.importFromDB();
            for (Activity activity : activities) {
                String formattedTimestamp = activity.getTimestamp().format(formatter);
                String fullDescription = activity.getDescription() + " at " + formattedTimestamp;

                if (shortenedDescription.equals(fullDescription.length() > 46 ? fullDescription.substring(0, 46) + "..." : fullDescription)) {
                    return fullDescription;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String truncateLogEntry(String logEntry, int limit) {
        if (logEntry.length() > limit) {
            return logEntry.substring(0, limit) + "...";
        }
        return logEntry;
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

        ActivitiesDBConnector activitiesDB = ActivitiesDBConnector.getInstance();
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
