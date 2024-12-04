package org.library.btl_oop16_library.utils.database;

import org.library.btl_oop16_library.model.Activity;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ActivitiesDBConnector extends DBConnector<Activity> {

    private static ActivitiesDBConnector instance;
    private ActivitiesDBConnector() {

    }

    public static ActivitiesDBConnector getInstance() {
        if (instance == null) {
            instance = new ActivitiesDBConnector();
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
                        rs.getTimestamp("timestamp").toLocalDateTime()
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
    public List<Activity> searchById(int id) {
        return null;
    }

    public List<Activity> searchByTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Activity> activities = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String query = "SELECT * FROM activities WHERE timestamp BETWEEN ? AND ? ORDER BY timestamp DESC";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, startDateTime.format(formatter));
            stmt.setString(2, endDateTime.format(formatter));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    activities.add(new Activity(
                            rs.getInt("id"),
                            rs.getString("description"),
                            rs.getTimestamp("timestamp").toLocalDateTime()
                    ));
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return activities;
    }

    @Override
    public void exportToExcel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void importFromExcel(String filename) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Activity> searchByAttributes(String searchInput, String type) {
        return null;
    }

    public void logActivity(String description) {
        String query = "INSERT INTO activities (description) VALUES (?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, description);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}