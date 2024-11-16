package org.library.btl_oop16_library.Util;

import org.library.btl_oop16_library.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserDBConnector  {
    public UserDBConnector() {
        super();
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
                        rs.getString("phone"),
                        rs.getString("address")
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

    public static boolean addToDB(User user) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (!pattern.matcher(user.getEmail()).matches()) {
            System.out.println("Invalid email format: " + user.getEmail());
            return false;
        }

        String countQuery = "SELECT COUNT(*) FROM user WHERE email = ?";
        String insertQuery = "INSERT INTO user (name, email, phone, address) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement countStmt = connection.prepareStatement(countQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

            countStmt.setString(1, user.getEmail());
            ResultSet rs = countStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("User with email '" + user.getEmail() + "' already exists.");
                return false;
            }

            // Insert new user into the database
            insertStmt.setString(1, user.getName());
            insertStmt.setString(2, user.getEmail());
            insertStmt.setString(3, user.getPhone());
            insertStmt.setString(4, user.getAddress());
            insertStmt.executeUpdate();

            System.out.println("User added successfully: " + user.getName());
            return true;
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            return false;
        }
    }

    public void deleteFromDB(User user) {
        String deleteQuery = "DELETE FROM user WHERE id = ?";
        try (Connection connection = DBConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {

            stmt.setInt(1, user.getId());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("User not found.");
                throw new RuntimeException("User with ID '" + user.getId() + "' does not exist.");
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
                        rs.getString("phone"),
                        rs.getString("address")
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
        String query = "SELECT * FROM user WHERE name LIKE ?";
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
                        rs.getString("phone"),
                        rs.getString("address")
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

    public boolean updateUser(User user) {
        String updateQuery = "UPDATE user SET name = ?, email = ?, phone = ?, address = ? WHERE id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(updateQuery)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getAddress());
            stmt.setInt(5, user.getId());

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
}


