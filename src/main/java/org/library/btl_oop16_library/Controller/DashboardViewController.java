package org.library.btl_oop16_library.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import org.library.btl_oop16_library.Util.DBConnector;

public class DashboardViewController {
    @FXML
    private Label adminCountLabel;

    @FXML
    private Label bookCountLabel;

    @FXML
    private Label userCountLabel;

    @FXML
    private PieChart loanStatusChart;

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
    }
}
