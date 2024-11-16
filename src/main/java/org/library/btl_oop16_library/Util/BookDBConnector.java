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
    private static final String AUTHOR_TABLE_NAME = "author";

    private static BookDBConnector instance;
    private static final Object lock = new Object();

    private BookDBConnector() {}

    public static BookDBConnector getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new BookDBConnector();
                }
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
                        getAuthorNameById(rs.getInt("authorId"),conn,ps),
                        rs.getString("type"),
                        rs.getString("language"),
                        countAvailable(id, conn, ps)
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
        try (Connection conn = getConnection();
             PreparedStatement psCheck = conn.prepareStatement(checkQuery)) {

             psCheck.setString(1, item.getTitle());
             ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                int bookId = rs.getInt("id");
                addCopyToDB(bookId, conn);
            } else {
                if(getAuthorIdByName(item.getAuthor(), conn) == -1) {
                    addAuthorToDB(item.getAuthor(), conn);
                }
                addBookToDB(item, conn);
                addCopyToDB(item.getId(), conn);
            }
        } catch (SQLException e) {
            throw new SQLException("Error while adding book or copy to DB", e);
        }
    }

    @Override
    public Book searchByName(String name) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE title = ?";
        Book book = null;

//        try(Connection conn = getConnection();
//            PreparedStatement ps = conn.prepareStatement(query)) {
//            ps.setString(1, name);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    book = new Book(
//                            rs.getInt("id"),
//                            rs.getString("title"),
//                            getAuthorNameById(rs.getInt("authorId"), conn, ps),
//                            rs.getString("type"),
//                            rs.getString("language"),
//                            countAvailable()
//                    );
//                }
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
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
                            getAuthorNameById(rs.getInt("authorId"), conn, ps),
                            rs.getString("type"),
                            rs.getString("language"),
                            countAvailable(id, conn, ps)
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }

    public int countById(int id, Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + COPIES_TABLE_NAME + " WHERE bookId = ?";
        int count = 0;
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        }
        return count;
    }

    public String getAuthorNameById(int authorId, Connection conn, PreparedStatement ps) throws SQLException {
        String query = "SELECT authorName FROM " + AUTHOR_TABLE_NAME + " WHERE id = ?";
        ps = conn.prepareStatement(query);
        ps.setInt(1, authorId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString(1);
            }
        }
        return "UNKNOWN";
    }

    public int getAuthorIdByName(String authorName, Connection conn) throws SQLException {
        String query = "SELECT id FROM " + AUTHOR_TABLE_NAME + " WHERE authorName = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, authorName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public int countAvailable(int id , Connection conn, PreparedStatement ps) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + COPIES_TABLE_NAME + " WHERE bookId = ? AND status = 'available'";
        int count = 0;
        ps = conn.prepareStatement(query);
        ps.setInt(1, id);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        }
        return count;
    }

    public void addCopyToDB(int bookId, Connection conn) throws SQLException {
        String query = "INSERT INTO " + COPIES_TABLE_NAME + " (bookId, status) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, bookId);
            ps.setString(2, "available");
            ps.executeUpdate();
        }
    }

    public void addBookToDB(Book item, Connection conn) throws SQLException {
        String query = "INSERT INTO " + TABLE_NAME + " (title, authorId, type, language) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, item.getTitle());
            ps.setInt(2,getAuthorIdByName(item.getAuthor(), conn));
            ps.setString(3, item.getType());
            ps.setString(4, item.getLanguage());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error while adding book to DB");
        }

        try (PreparedStatement tmp_ps = conn.prepareStatement("SELECT last_insert_rowid()");
             ResultSet rs = tmp_ps.executeQuery()) {
            if (rs.next()) {
                item.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new SQLException("Error while adding book to DB");
        }
    }

    public int getIdByName(String title) throws SQLException {
        String query = "SELECT id FROM " + TABLE_NAME + " WHERE title = ?";
        try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, title);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public void addAuthorToDB(String authorName, Connection conn) throws SQLException {
        String query = "INSERT INTO " + AUTHOR_TABLE_NAME + " (authorName) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, authorName);
            ps.executeUpdate();
        }
    }
}
