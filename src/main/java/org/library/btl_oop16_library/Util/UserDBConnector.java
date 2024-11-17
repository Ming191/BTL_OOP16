package org.library.btl_oop16_library.Util;

import org.library.btl_oop16_library.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserDBConnector extends DBConnector<User> {
    private static UserDBConnector instance;

    private static final Object lock = new Object();

    private UserDBConnector() {};

    public static UserDBConnector getInstance() {
        if (instance == null) {
            synchronized (lock) {
                instance = new UserDBConnector();
            }
        }
        return instance;
    }

    public List<User> importFromDB() {
        String query = "select * from User";
        List<User> users = new ArrayList<User>();

        try (Connection con = DBConnector.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("address"),
                        rs.getString("userName"),
                        rs.getString("password")
                );
                users.add(user);
            }
            System.out.println("Users imported successfully");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void addToDB(User user) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (!pattern.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + user.getEmail());
        }

        String countQuery = "SELECT COUNT(*) FROM user WHERE email = ?";
        String insertQuery = "INSERT INTO user (name, email, phoneNumber, address, username, password) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement countStmt = connection.prepareStatement(countQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

            countStmt.setString(1, user.getEmail());
            ResultSet rs = countStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new RuntimeException("User with email '" + user.getEmail() + "' already exists.");
            }

            insertStmt.setString(1, user.getName());
            insertStmt.setString(2, user.getEmail());
            insertStmt.setString(3, user.getPhoneNumber());
            insertStmt.setString(4, user.getAddress());
            insertStmt.setString(5, user.getUserName());
            insertStmt.setString(6, user.getPassword());
            insertStmt.executeUpdate();

            System.out.println("User added successfully: " + user.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed: " + e.getMessage());
        }
    }


    public void deleteFromDB(int id) {
        String deleteLoansQuery = "DELETE FROM bookLoans WHERE userId = ?";
        String deleteUserQuery = "DELETE FROM user WHERE id = ?";

        try (Connection connection = DBConnector.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(deleteLoansQuery)) {
                stmt.setInt(1, id);
                int loansDeleted = stmt.executeUpdate();
                if (loansDeleted > 0) {
                    System.out.println(loansDeleted + " book loan(s) deleted.");
                }
            }

            try (PreparedStatement stmt = connection.prepareStatement(deleteUserQuery)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("User deleted successfully.");
                } else {
                    System.out.println("User not found.");
                    throw new RuntimeException("User with ID '" + id + "' does not exist.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete user: " + e.getMessage());
        }
    }


    public User searchById(int id) {
        String query = "SELECT * FROM user WHERE id = ?";
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("address"),
                        rs.getString("userName"),
                        rs.getString("password")
                );
            } else {
                System.out.println("User with ID " + id + " not found.");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to search user by ID: " + e.getMessage());
        }
    }

    public List<User> searchByName(String name) {
        String query = "SELECT * FROM user WHERE user.name LIKE ?";
        List<User> users = new ArrayList<>();

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("address"),
                        rs.getString("userName"),
                        rs.getString("password")
                );
                users.add(user);
            }

            if (users.isEmpty()) {
                System.out.println("No users found with name: " + name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to search users by name: " + e.getMessage());
        }

        return users;
    }

    public User getUser(String userName, String password) {
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, userName);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setAddress(rs.getString("address"));
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("password"));

                return user;
            } else {
                System.out.println("Invalid username or password.");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }


    public boolean updateUser(User user) {
        String updateQuery = "UPDATE user SET name = ?, email = ?, phoneNumber = ?, address = ?, username = ?, password = ? WHERE id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(updateQuery)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhoneNumber());
            stmt.setString(4, user.getAddress());
            stmt.setString(5, user.getUserName());
            stmt.setString(6, user.getPassword());
            stmt.setInt(7, user.getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User updated successfully: " + user.getName());
                return true;
            } else {
                System.out.println("No user found with ID: " + user.getId());
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update user: " + e.getMessage());
        }
    }

    public static boolean isAlreadyExist(String userName) {
        String query = "SELECT 1 FROM user WHERE username = ? LIMIT 1";
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, userName);
            try (ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.err.println("Failed to check account existence: " + e.getMessage());
            throw new RuntimeException("Failed to check account existence.", e);
        }
    }


}


