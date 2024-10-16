package org.library.btl_oop16_library;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {


    public boolean isConnectionValid() {
        String url = "jdbc:mysql://localhost:3306/LibraryManagement";
        String user = "root";
        String password = "Minh1901";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Connection successful!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }
        return false;
    }
}
