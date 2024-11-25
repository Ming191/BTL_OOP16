package org.library.btl_oop16_library.Util;

import org.library.btl_oop16_library.Model.Activity;
import org.sqlite.core.DB;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ActivitiesDBConnector extends DBConnector<Activity> {

    private static ActivitiesDBConnector instance;
    private static final Object lock = new Object();
    public ActivitiesDBConnector() {

    }

    public static ActivitiesDBConnector getInstance() {
        if (instance == null) {
            synchronized (lock) {
                instance = new ActivitiesDBConnector();
            }
        }
        return instance;
    }

    @Override
    public List<Activity> importFromDB() throws SQLException {
        List<Activity> activities = new ArrayList<>();
        String query = "SELECT * FROM activities ORDER BY timestamp DESC";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                activities.add(new Activity(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getTimestamp("timestamp")
                ));
            }
        }
        return activities;
    }

    @Override
    public void deleteFromDB(int id) throws SQLException {
        String query = "DELETE FROM activities WHERE id = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void addToDB(Activity activity) throws SQLException {

    }

    @Override
    public Activity searchById(int id) {
        return null;
    }

    public List<Activity> searchByTimeRange(Timestamp startDateTime, Timestamp endDateTime) {
        List<Activity> activities = new ArrayList<>();
        String query = "SELECT * FROM activities WHERE timestamp BETWEEN ? AND ? ORDER BY timestamp DESC";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, startDateTime);
            stmt.setTimestamp(2, endDateTime);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    activities.add(new Activity(
                            rs.getInt("id"),
                            rs.getString("description"),
                            rs.getTimestamp("timestamp")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activities;
    }

    @Override
    public void exportToExcel() {

    }

    @Override
    public void importFromExcel(String filename) {

    }

    public void logActivity(String description) {
        String query = "INSERT INTO activities (description) VALUES (?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, description);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}