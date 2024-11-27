package org.library.btl_oop16_library.U_Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import org.library.btl_oop16_library.Model.User;
import org.library.btl_oop16_library.Util.DBConnector;
import org.library.btl_oop16_library.Util.SessionManager;

public class U_DashboardController {
    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private PieChart pieChart;

    public AnchorPane getDashboardPane() {
        return dashboardPane;
    }

    @FXML
    private void initialize() {
        pieChartInitialize();
    }

    private void pieChartInitialize() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        User currentUser = SessionManager.getInstance().getCurrentUser();

        int userId = currentUser.getId();

        int returnedCount = DBConnector.getCount("SELECT COUNT(*) FROM bookLoans WHERE status = 'returned' and userId = " + userId);
        System.out.println(returnedCount);
        int notReturnedCount = DBConnector.getCount("SELECT COUNT(*) FROM bookLoans WHERE status = 'not returned' AND userId = " + userId);
        System.out.println(notReturnedCount);
        int preOrderedCount = DBConnector.getCount("SELECT COUNT(*) FROM bookLoans WHERE status = 'pre-ordered' AND userId = " + userId);
        System.out.println(preOrderedCount);

        pieChartData.add(new PieChart.Data("returned", returnedCount));
        pieChartData.add(new PieChart.Data("not returned", notReturnedCount));
        pieChartData.add(new PieChart.Data("pre-ordered", preOrderedCount));

        pieChart.setData(pieChartData);
        pieChart.setTitle("Book Loans Status");
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
    }

}
