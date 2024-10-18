package org.library.btl_oop16_library;

import java.sql.*;

public class DatabaseConnector {
    private void openDB() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:my.db");

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public void selectFromDB(Library library) {
        Connection c = null;
        Statement stmt = null;
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
                String available = rs.getString("available");
                Book book = new Book(id, title, author, type, language, available);
                library.getBooks().add(book);
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

    public void add_book (Library library, Book book) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c =  DriverManager.getConnection("jdbc:sqlite:my.db");

            stmt = c.createStatement();

            String q = "select count(*) from book where title = '" + book.getTitle() + "'";
            ResultSet rs = stmt.executeQuery(q);
            int count = rs.getInt(1);
            if (count != 0) {
                String q1 = "update book set quantity = quantity + 1 where title = '" + book.getTitle() + "'";
                stmt.executeUpdate(q1);
            }
            else {
                String query = "INSERT INTO book (id, title, author, type, language, available) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = c.prepareStatement(query);
                try {
                    pstmt.setInt(1, book.getId());
                    pstmt.setString(2, book.getTitle());
                    pstmt.setString(3, book.getAuthor());
                    pstmt.setString(4, book.getType());
                    pstmt.setString(5, book.getLanguage());
                    pstmt.setString(6, book.getAvailable());

                    pstmt.executeUpdate();
                    //rs = stmt.executeQuery(query);
                } catch (Exception e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                }
                library.getBooks().add(book);
            }
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }


}
