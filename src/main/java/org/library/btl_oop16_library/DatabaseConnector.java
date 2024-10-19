package org.library.btl_oop16_library;

import java.sql.*;

public class DatabaseConnector {

    public void selectFromDB(BookList bookList) {
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
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String type = rs.getString("type");
                String language = rs.getString("language");
                int available = rs.getInt("available");
                Book book = new Book(id, title, author, type, language, available);
                bookList.getBooks().add(book);
            }

            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
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
                    String deleteQuery = "delete from book where title = '" + book.getTitle() + "'";
                    PreparedStatement pstmtdUpdate = c.prepareStatement(deleteQuery);
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
}


