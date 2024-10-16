package org.library.btl_oop16_library;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    public static void main(String[] args) {

        String url = "jdbc:sqlite:my.db";

        var names = new String[] {"Raw Materials", "Semifinished Goods", "Finished Goods"};
        var capacities = new int[] {3000,4000,5000};

        String sql = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";

        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {

            for(int i = 0; i < 3; i++){
                pstmt.setString(1, names[i]);
                pstmt.setDouble(2, capacities[i]);
                pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
