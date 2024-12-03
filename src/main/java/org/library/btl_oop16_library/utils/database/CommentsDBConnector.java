package org.library.btl_oop16_library.utils.database;

import org.library.btl_oop16_library.model.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentsDBConnector extends DBConnector<Comment> {

    private static CommentsDBConnector instance;
    private static final Object lock = new Object();
    private CommentsDBConnector() {}

    public static CommentsDBConnector getInstance() {
        if (instance == null) {
            synchronized (lock) {
                instance = new CommentsDBConnector();
            }
        }
        return instance;
    }

    @Override
    public List<Comment> importFromDB() throws SQLException {
        String query = "SELECT * FROM comments ORDER BY id desc";
        List<Comment> comments = new ArrayList<>();


        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String context = rs.getString("context");
                int user_id = rs.getInt("user_id");
                int book_id = rs.getInt("book_id");

                comments.add(new Comment(id,book_id,user_id,context));
            }
        }

        return comments;
    }

    @Override
    public void deleteFromDB(int id) throws SQLException {
        String query = "DELETE FROM comments WHERE id = ?";
        try (Connection conn = DBConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void addToDB(Comment item) throws SQLException {
        String query = "INSERT INTO comments (book_id, user_id, context) VALUES (?, ?, ?)";
        try (Connection conn = DBConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, item.getBookId());
            stmt.setInt(2, item.getUserId());
            stmt.setString(3, item.getContext());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Comment> searchById(int id) {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT * FROM comments WHERE id = ? ORDER BY id desc";

        try (Connection conn = DBConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int book_id = rs.getInt("book_id");
                int user_id = rs.getInt("user_id");
                String context = rs.getString("context");
                comments.add(new Comment(id,book_id,user_id,context));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comments;
    }

    public List<Comment> searchByBookId(int book_id) {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT * FROM comments WHERE book_id = ? ORDER BY id desc";
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, book_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                String context = rs.getString("context");
                comments.add(new Comment(id,book_id,user_id,context));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comments;
    }

    @Override
    public void exportToExcel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void importFromExcel(String filename) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Comment> searchByAttributes(String searchInput, String type) {
        return null;
    }
}
