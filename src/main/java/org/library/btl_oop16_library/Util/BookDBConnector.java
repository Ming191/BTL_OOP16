package org.library.btl_oop16_library.Util;

import org.library.btl_oop16_library.Model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDBConnector extends DBConnector<Book> {
    private static final String TABLE_NAME = "book";

    private static BookDBConnector instance;
    private static final Object lock = new Object();
    private BookDBConnector() {}

    public static BookDBConnector getInstance() {
        if (instance == null) {
            synchronized (lock) {
                instance = new BookDBConnector();
            }
        }
        return instance;
    }

    @Override
    public List<Book> importFromDB() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");

                Book book = new Book(
                        id,
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("language"),
                        rs.getInt("quantity")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            throw new SQLException("Error while importing books from DB");
        }
        return books;
    }

    @Override
    public void deleteFromDB(int id) throws SQLException {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error while deleting book from DB");
        }
    }

    @Override
    public void addToDB(Book item) throws SQLException {
        String checkQuery = "SELECT id FROM " + TABLE_NAME + " WHERE title = ?";
        String updateQuery = "UPDATE " + TABLE_NAME + " SET quantity = quantity + ? WHERE id = ?";
        String insertQuery = "INSERT INTO " + TABLE_NAME + " (title, author, category, language, quantity, imgUrl, rating, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement psCheck = conn.prepareStatement(checkQuery)) {

             psCheck.setString(1, item.getTitle());
             ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                try (PreparedStatement psUpdate = conn.prepareStatement(updateQuery)) {
                    psUpdate.setInt(1, item.getAvailable());
                    psUpdate.setInt(2, rs.getInt("id"));
                    psUpdate.executeUpdate();
                } catch (SQLException e) {
                    throw new SQLException("Error while updating book in DB");
                }
            } else {
                try (PreparedStatement psInsert = conn.prepareStatement(insertQuery)) {
                    psInsert.setString(1, item.getTitle());
                    psInsert.setString(2, item.getAuthor());
                    psInsert.setString(3, item.getCategory());
                    psInsert.setString(4, item.getLanguage());
                    psInsert.setInt(5, item.getAvailable());
                    psInsert.setString(6, item.getImgURL());
                    psInsert.setString(7, item.getRating());
                    psInsert.setString(8, item.getDescription());
                    psInsert.executeUpdate();
                } catch (SQLException e) {
                    throw new SQLException("Error while inserting book in DB");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error while adding book or copy to DB", e);
        }
    }

    @Override
    public Book searchById(int id) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        Book book = null;

        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    book = new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("category"),
                            rs.getString("language"),
                            rs.getInt("quantity")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }

    public int getIdByTitle(String title) throws SQLException {
        String query = "SELECT id FROM " + TABLE_NAME + " WHERE title = ?";
        int id = 0;
        try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, title);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt("id");
                }
            }
        }
        return id;
    }
}
