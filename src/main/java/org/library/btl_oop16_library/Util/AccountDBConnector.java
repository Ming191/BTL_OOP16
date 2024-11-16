package org.library.btl_oop16_library.Util;

import org.library.btl_oop16_library.Model.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDBConnector {
    public AccountDBConnector() {
        super();
    }

    public List<Account> importFromDB() {
        List<Account> accountList = new ArrayList<>();
        String query = "SELECT * FROM account";

        try (Connection con = DBConnector.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs= stmt.executeQuery(query)) {

            while (rs.next()) {
                Account account = new Account(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("isAdmin")
                );
                accountList.add(account);
            }
        } catch (SQLException e) {
            System.err.println("Failed to import accounts: " + e.getMessage());
            throw new RuntimeException("Failed to import accounts.", e);
        }

        return accountList;
    }

    public static boolean addToDB(Account account) {
        String insertQuery = "INSERT INTO account (username, password, isAdmin) VALUES (?, ?, ?)";
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(insertQuery)) {

            stmt.setString(1, account.getUserName());
            stmt.setString(2, account.getPassword());
            stmt.setBoolean(3, account.isAdmin());
            stmt.executeUpdate();

            System.out.println("Account added successfully: " + account.getUserName());
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to add account: " + e.getMessage());
            return false;
        }
    }


    public static boolean isAccountExist(String userName) {
        String query = "SELECT 1 FROM account WHERE username = ? LIMIT 1";
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, userName);
            try (ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next(); // Returns true if an account exists
            }
        } catch (SQLException e) {
            System.err.println("Failed to check account existence: " + e.getMessage());
            throw new RuntimeException("Failed to check account existence.", e);
        }
    }

    public static Account getAccount(String username, String password) {
        String query = "SELECT * FROM account WHERE username = ? AND password = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String accountUsername = rs.getString("username");
                    String accountPassword = rs.getString("password");
                    boolean isAdmin = rs.getBoolean("isAdmin");

                    return new Account(accountUsername, accountPassword, isAdmin);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return null;
        }
    }
}
