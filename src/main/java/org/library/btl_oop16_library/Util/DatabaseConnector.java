package org.library.btl_oop16_library.Util;

import org.library.btl_oop16_library.Model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;

public class DatabaseConnector {

    public static void selectFromDB(BookList bookList) {
        Connection c;
        Statement stmt;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:my.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM book");

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("type"),
                        rs.getString("language"),
                        rs.getInt("available")
                );
                bookList.getBooks().add(book);
            }
            System.out.println("Operation done successfully");
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBook (Book book) {
        Connection c = null;
        ResultSet rs = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:my.db");

            String query = "select count(*) from book where title = '" + book.getTitle() + "'";
            PreparedStatement psmtCount = c.prepareStatement(query);
            rs = psmtCount.executeQuery();

            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }

            if (count != 0) {
                String updateQuery = "update book set available = available + ? where title = ?";
                PreparedStatement psmtUpdate = c.prepareStatement(updateQuery);
                try {
                    psmtUpdate.setInt(1, book.getAvailable());
                    psmtUpdate.setString(2, book.getTitle());
                    psmtUpdate.executeUpdate();
                } catch (SQLException e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                }
            }
            else {
                String insertQuery = "INSERT INTO book (title, author, type, language, available) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement psmtInsert = c.prepareStatement(insertQuery);
                try {
                    psmtInsert.setString(1, book.getTitle());
                    psmtInsert.setString(2, book.getAuthor());
                    psmtInsert.setString(3, book.getType());
                    psmtInsert.setString(4, book.getLanguage());
                    psmtInsert.setInt(5, book.getAvailable());

                    psmtInsert.executeUpdate();
                } catch (Exception e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                if (rs != null) rs.close();
                if (c != null) c.close();
            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    public void deleteBook (Book book) throws SQLException {
        Connection c = null;
        ResultSet rs = null;
        int count = 0;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:my.db");

            int quantity = book.getAvailable();
            String countQuery = "select available from book where title = '" + book.getTitle() + "'";
            PreparedStatement pstmtCount = c.prepareStatement(countQuery);
            try {
                rs = pstmtCount.executeQuery();

                if (rs.next()) {
                    count = rs.getInt(1);
                }

                if (quantity < count) {
                    String updateCountQuery = "update book set available = available - ? where title = ?";
                    PreparedStatement pstmCounttUpdate = c.prepareStatement(updateCountQuery);
                    pstmCounttUpdate.setInt(1, quantity);
                    pstmCounttUpdate.setString(2, book.getTitle());
                    pstmCounttUpdate.executeUpdate();
                }
                else {
                    String deleteQuery = "update book set available = 0 where title = ?";
                    PreparedStatement pstmtdUpdate = c.prepareStatement(deleteQuery);
                    pstmtdUpdate.setString(1, book.getTitle());
                    pstmtdUpdate.executeUpdate();
                }
            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());

            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                if (rs != null) rs.close();
                if (c != null) c.close();
            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }

    }

    public void selectUsersFromDB(UserList userList) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:my.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM user");

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("account"),
                        rs.getString("password"),
                        rs.getString("email")
                );
                userList.getUsers().add(user);
            }
            System.out.println("Operation done successfully");
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean addUserFromDB(User user) {
        Connection c = null;
        ResultSet rs = null;
        boolean success = false;

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);

        try {
            if (!pattern.matcher(user.getEmail()).matches()) {
                System.out.println("Invalid email format: " + user.getEmail());
                ApplicationAlert.wrongEmailPattern();
                return false;
            }

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:my.db");

            String query = "select count(*) from user where account = ?";
            PreparedStatement psmtCount = c.prepareStatement(query);
            psmtCount.setString(1, user.getAccount());
            rs = psmtCount.executeQuery();

            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }

            if (count != 0) {
                System.out.println("User account already exists.");
            } else {
                String insertQuery = "INSERT INTO user (name, account, password, email, isAdmin) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement psmtInsert = c.prepareStatement(insertQuery);

                psmtInsert.setString(1, user.getName());
                psmtInsert.setString(2, user.getAccount());
                psmtInsert.setString(3, user.getPassword());
                psmtInsert.setString(4, user.getEmail());
                psmtInsert.setInt(5, user.isAdmin()? 1:0);
                psmtInsert.executeUpdate();
            }
            success = true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                if (rs != null) rs.close();
                if (c != null) c.close();
            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return success;
    }

    public void deleteUser(User user) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:my.db");

            String deleteQuery = "DELETE FROM user WHERE account = ?";
            PreparedStatement psmtDelete = c.prepareStatement(deleteQuery);
            psmtDelete.setString(1, user.getAccount());
            int rowsAffected = psmtDelete.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("User not found.");
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                if (c != null) c.close();
            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }

        }
    }

    public static User getUser(String username, String password) {
        Connection c = null;
        ResultSet rs = null;
        PreparedStatement psmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:my.db");

            String query = "select * from user where account = ? and password = ?";
            psmt = c.prepareStatement(query);
            psmt.setString(1, username);
            psmt.setString(2, password);
            rs = psmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String account = rs.getString("account");
                String password1 = rs.getString("password");
                String email = rs.getString("email");
                boolean isAdmin = rs.getBoolean("isAdmin");
                return new User(id, name, account, password1, email,isAdmin);
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            try {
                if (c!=null) c.close();
                if (rs != null) rs.close();
                if (psmt != null) psmt.close();
            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return null;
    }

    public static boolean isUserExist(String accountName) {
        boolean exists = false;

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:my.db");
             PreparedStatement psmt = c.prepareStatement("SELECT 1 FROM user WHERE account = ? LIMIT 1")) {
            psmt.setString(1, accountName);
            try (ResultSet rs = psmt.executeQuery()) {
                exists = rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }

    public static List<BookLending> loadBookLendingFromDB() {
        List<BookLending> bookLendings = new ArrayList<BookLending>();

        Connection c = null;
        ResultSet rs = null;
        PreparedStatement psmt = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:my.db");

            String query = "SELECT * FROM bookLending";
            psmt = c.prepareStatement(query);
            rs = psmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("userId");
                int bookId = rs.getInt("bookId");
                int amount = rs.getInt("amount");
                Date startDate = null;
                Date dueDate = null;
                try {
                    String startDateStr = rs.getString("startDate");
                    String dueDateStr = rs.getString("dueDate");

                    if (startDateStr != null) {
                        startDate = new java.sql.Date(sdf.parse(startDateStr).getTime());
                    }
                    if (dueDateStr != null) {
                        dueDate = new java.sql.Date(sdf.parse(dueDateStr).getTime());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String status = rs.getString("status");

                BookLending bookLending = new BookLending(id, userId, bookId, startDate, dueDate, amount);
                bookLendings.add(bookLending);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) rs.close();
                if(c != null) c.close();
                if(psmt != null) psmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return bookLendings;
    }

    public static void addBookLendingToDB(Book book, User user,int amount) throws ClassNotFoundException {
        Connection c = null;
        ResultSet rs = null;
        PreparedStatement psmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:my.db");
            String query = "INSERT INTO bookLending (bookId, userId, amount, startDate, dueDate) VALUES (?, ?, ?, ?, ?)";
            psmt = c.prepareStatement(query);

            psmt.setInt(1, book.getId());
            psmt.setInt(2, user.getId());
            psmt.setInt(3, amount);
            psmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            psmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
            psmt.executeUpdate();

            String updateBookDB = "update book set available = available - ? where id = ?";
            psmt = c.prepareStatement(updateBookDB);
            psmt.setInt(1, amount);
            psmt.setInt(2, book.getId());
            psmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (c != null) c.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}


