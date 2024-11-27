package org.library.btl_oop16_library.Util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.library.btl_oop16_library.Model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                        rs.getString("password"),
                        rs.getString("role")
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
        String insertQuery = "INSERT INTO user (name, email, phoneNumber, address, username, password, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        ActivitiesDBConnector activitiesDB = ActivitiesDBConnector.getInstance();

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement countStmt = connection.prepareStatement(countQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

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
            insertStmt.setString(7, "user");
            insertStmt.executeUpdate();

            activitiesDB.logActivity("User " + user.getName() + " added");

            System.out.println("User added successfully: " + user.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database operation failed: " + e.getMessage());
        }
    }



    public void deleteFromDB(int id) {
        String deleteLoansQuery = "DELETE FROM bookLoans WHERE userId = ?";
        String deleteUserQuery = "DELETE FROM user WHERE id = ?";
        ActivitiesDBConnector activitiesDB = ActivitiesDBConnector.getInstance();

        try (Connection connection = DBConnector.getConnection()) {
            String findUserQuery = "SELECT name FROM user WHERE id = ?";
            String userName = null;

            try (PreparedStatement fetchStmt = connection.prepareStatement(findUserQuery)) {
                fetchStmt.setInt(1, id);
                try (ResultSet rs = fetchStmt.executeQuery()) {
                    if (rs.next()) {
                        userName = rs.getString("name");
                    } else {
                        throw new RuntimeException("User with ID '" + id + "' does not exist.");
                    }
                }
            }

            try (PreparedStatement stmt = connection.prepareStatement(deleteLoansQuery)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = connection.prepareStatement(deleteUserQuery)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    activitiesDB.logActivity("User " + userName + " deleted");
                    System.out.println("User deleted successfully.");
                } else {
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
                        rs.getString("password"),
                        rs.getString("role")
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

    @Override
    public void exportToExcel() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = currentDate.format(formatter);

        String outputFolderPath = "output";
        String outputFilePath = outputFolderPath + File.separator + formattedDate + "_users.xlsx";

        File outputFolder = new File(outputFolderPath);
        if (!outputFolder.exists()) {
            if (!outputFolder.mkdir()) {
                System.err.println("Failed to create output folder: " + outputFolder.getAbsolutePath());
                return;
            }
        }

        String query = "SELECT * FROM user";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Users");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"id", "name", "phoneNumber", "address", "email", "userName", "password", "role"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            int rowIndex = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(rs.getInt("id"));
                row.createCell(1).setCellValue(rs.getString("name"));
                row.createCell(2).setCellValue(rs.getString("phoneNumber"));
                row.createCell(3).setCellValue(rs.getString("address"));
                row.createCell(4).setCellValue(rs.getString("email"));
                row.createCell(5).setCellValue(rs.getString("username"));
                row.createCell(6).setCellValue(rs.getString("password"));
                row.createCell(7).setCellValue(rs.getString("role"));
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                workbook.write(fos);
            }

            workbook.close();
            System.out.println("User data exported to: " + outputFilePath);

        } catch (SQLException e) {
            System.err.println("Database query failed: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Failed to write Excel file: " + e.getMessage());
        }
    }

    @Override
    public void importFromExcel(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("File does not exist: " + filePath);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                int id = (row.getCell(0) != null && row.getCell(0).getCellType() == CellType.NUMERIC)
                        ? (int) row.getCell(0).getNumericCellValue() : -1;
                String name = (row.getCell(1) != null && row.getCell(1).getCellType() == CellType.STRING)
                        ? row.getCell(1).getStringCellValue() : "";
                String email = (row.getCell(2) != null && row.getCell(2).getCellType() == CellType.STRING)
                        ? row.getCell(2).getStringCellValue() : "";
                String phoneNumber = (row.getCell(3) != null && row.getCell(3).getCellType() == CellType.STRING)
                        ? row.getCell(3).getStringCellValue() : "";
                String address = (row.getCell(4) != null && row.getCell(4).getCellType() == CellType.STRING)
                        ? row.getCell(4).getStringCellValue() : "";
                String username = (row.getCell(5) != null && row.getCell(5).getCellType() == CellType.STRING)
                        ? row.getCell(5).getStringCellValue() : "";
                String password = (row.getCell(6) != null && row.getCell(6).getCellType() == CellType.STRING)
                        ? row.getCell(6).getStringCellValue() : "";
                String role = (row.getCell(7) != null && row.getCell(7).getCellType() == CellType.STRING)
                        ? row.getCell(7).getStringCellValue() : "";

                upsertUser(id, name, phoneNumber, address, email, username, password, role);
            }

            System.out.println("Data successfully imported from Excel file: " + filePath);

        } catch (IOException e) {
            System.err.println("Error while reading Excel file: " + e.getMessage());
        }
    }

    private void upsertUser(int id, String name, String phoneNumber, String address,
                            String email, String username, String password, String role) {
        String upsertQuery = """
        INSERT INTO user (id, name, phoneNumber , address, email, username, password, role)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT (id) 
        DO UPDATE SET
            name = EXCLUDED.name,
            phoneNumber = EXCLUDED.phoneNumber,
            address = EXCLUDED.address,
            email = EXCLUDED.email,
            username = EXCLUDED.username,
            password = EXCLUDED.password,
            role = EXCLUDED.role
    """;

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(upsertQuery)) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, phoneNumber);
            ps.setString(5, address);
            ps.setString(6, username);
            ps.setString(7, password);
            ps.setString(8, role);

            ps.executeUpdate();
            System.out.println("User upserted: " + username);
        } catch (SQLException e) {
            System.err.println("Error while upserting user: " + e.getMessage());
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
                        rs.getString("password"),
                        rs.getString("role")
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
                user.setRole(rs.getString("role"));

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


    public boolean updateUserForAdmin(User user) {
        String updateQuery = "UPDATE user SET name = ?, email = ?, phoneNumber = ?, address = ?, username = ?, password = ?, role = ? WHERE id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(updateQuery)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhoneNumber());
            stmt.setString(4, user.getAddress());
            stmt.setString(5, user.getUserName());
            stmt.setString(6, user.getPassword());
            stmt.setString(7, user.getRole());
            stmt.setInt(8, user.getId());

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

    public boolean updatePassword(int userId, String newPassword) {
        String updateQuery = "UPDATE user SET password = ? WHERE id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(updateQuery)) {

            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Password updated successfully for User ID: " + userId);
                return true;
            } else {
                System.out.println("No user found with ID: " + userId);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update password: " + e.getMessage());
        }
    }

    public static boolean updateUserInfor(User user) {
        String query = "UPDATE user SET name = ?, email = ?, address = ?, phoneNumber = ? WHERE id = ?";

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getAddress());
            preparedStatement.setString(4, user.getPhoneNumber());
            preparedStatement.setInt(5, user.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<User> getAdminData() {
        String query = "SELECT name, email, phoneNumber FROM user WHERE role = 'admin'";
        List<User> users = new ArrayList<>();

        try (Connection connection = DBConnector.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                User user = new User();
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get admin data " + e.getMessage());
        }

        return users;
    }

}


