package org.library.btl_oop16_library.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.library.btl_oop16_library.Model.Activity;
import org.library.btl_oop16_library.Util.ActivitiesDBConnector;
import org.library.btl_oop16_library.Util.DBConnector;

import java.sql.SQLException;
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

        pieChartData.add(new PieChart.Data("returned", returnedCount));
        pieChartData.add(new PieChart.Data("not returned", notReturnedCount));
        pieChartData.add(new PieChart.Data("pre-ordered", preOrderedCount));

        loanStatusChart.setData(pieChartData);
        loanStatusChart.setTitle("Book Loans Status");
        loanStatusChart.setLegendVisible(true);
        loanStatusChart.setLabelsVisible(true);

        loadActivities();
    }

    private void loadActivities() {
        activityList = FXCollections.observableArrayList();
        ActivitiesDBConnector activitiesDB = new ActivitiesDBConnector();

        try {
            List<Activity> activities = activitiesDB.importFromDB();
            for (Activity activity : activities) {
                activityList.add(activity.getDescription() + " at " + activity.getTimestamp());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        activityListView.setItems(activityList);
    }
}
