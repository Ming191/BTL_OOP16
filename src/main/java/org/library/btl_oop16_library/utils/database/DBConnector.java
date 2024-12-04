package org.library.btl_oop16_library.utils.database;

import java.sql.*;
import java.util.List;

public abstract class DBConnector<T> {
    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection("jdbc:sqlite:my.db");
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys=ON");
            }
        }
        return conn;
    }

    public static int getCount(String query) {
        int count = 0;
        try (Connection connection = getConnection();
             PreparedStatement pstm = connection.prepareStatement(query);
             ResultSet rs = pstm.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return count;
    }


    public abstract List<T> importFromDB() throws SQLException;

    public abstract void deleteFromDB(int id) throws SQLException;

    public abstract void addToDB(T item) throws SQLException;

    public abstract List<T> searchById(int id);

    public abstract List<T> searchByAttributes(String searchInput, String type);

    public abstract void exportToExcel();

    public abstract void importFromExcel(String filename);
}
