package org.library.btl_oop16_library.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public abstract class DBConnector<T> {
    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection("jdbc:sqlite:my.db");
        }
        return conn;
    }

    public abstract List<T> importFromDB() throws SQLException;

    public abstract void exportToDB(List<T> itemList) throws SQLException;

    public abstract void deleteFromDB(int id) throws SQLException;

    public abstract void addToDB(T item) throws SQLException;
}