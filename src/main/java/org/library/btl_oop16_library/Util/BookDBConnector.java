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
    private static final String COPIES_TABLE_NAME = "copies";

    @Override
    public List<Book> importFromDB() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("authorId"),
                        rs.getString("type"),
                        rs.getString("language"),
                        countById(rs.getInt("id")));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @Override
    public void addToDB(Book item) throws SQLException {
        String query = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, item.getId());
            ps.setString(2, item.getTitle());
            ps.setInt(3, item.getAuthorId());
            ps.setString(4, item.getType());
            ps.setString(5, item.getLanguage());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Book searchByName(String name) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE title = ?";
        Book book = null;

        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    book = new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getInt("authorId"),
                            rs.getString("type"),
                            rs.getString("language"),
                            countById(rs.getInt("id")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
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
                            rs.getInt("authorId"),
                            rs.getString("type"),
                            rs.getString("language"),
                            countById(rs.getInt("id")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public int countById(int id) {
        String query = "SELECT COUNT(*) FROM " + COPIES_TABLE_NAME + " WHERE bookId = ?";
        int count = 0;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    @Override
    public int countByName(String title) {
        String query = "SELECT id FROM " + TABLE_NAME + " WHERE title = ?";
        int id = -1;
        int count = 0;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setString(1, title);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                    count = countById(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return count;
    }
}
