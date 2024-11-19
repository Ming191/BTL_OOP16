package org.library.btl_oop16_library.Util;

import org.library.btl_oop16_library.Controller.CatalogViewController;
import org.library.btl_oop16_library.Controller.MainMenuController;
import org.library.btl_oop16_library.Model.BookLoans;
import org.library.btl_oop16_library.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BookLoanDBConnector extends DBConnector<BookLoans> {
    @Override
    public List<BookLoans> importFromDB() throws SQLException {
        List<BookLoans> bookLoans = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String query = "SELECT * FROM bookLoans";
        try (Connection con = DBConnector.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("userId");
                int bookId = rs.getInt("bookId");
                int amount = rs.getInt("amount");
                String status = rs.getString("status");
                try {
                    Date startDate = df.parse(rs.getString("startDate"));
                    Date dueDate = df.parse(rs.getString("dueDate"));

                    BookLoans bookLoan = new BookLoans(id, userId, bookId, startDate, dueDate, amount, status);
                    bookLoans.add(bookLoan);
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookLoans;
    }

    public List<BookLoans> importFromDBForUser(User user) throws SQLException {
        List<BookLoans> bookLoans = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String query = "SELECT * FROM bookLoans where userId = ?";
        try (Connection con = DBConnector.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("userId");
                int bookId = rs.getInt("bookId");
                int amount = rs.getInt("amount");
                String status = rs.getString("status");
                try {
                    Date startDate = df.parse(rs.getString("startDate"));
                    Date dueDate = df.parse(rs.getString("dueDate"));

                    BookLoans bookLoan = new BookLoans(id, userId, bookId, startDate, dueDate, amount, status);
                    bookLoans.add(bookLoan);
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookLoans;
    }

    @Override
    public void deleteFromDB(int id) throws SQLException {
        try (Connection con = DBConnector.getConnection()) {
            String query = "select * from bookLoans where id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            BookLoans bookLoan = null;
            while (rs.next()) {
                int Id = rs.getInt("id");
                int userId = rs.getInt("userId");
                int bookId = rs.getInt("bookId");
                int amount = rs.getInt("amount");
                bookLoan = new BookLoans(id, userId, bookId, null, null, amount, null);
            }

            String deleteBookLoan = "DELETE FROM bookLoans WHERE id = ?";
            ps = con.prepareStatement(deleteBookLoan);
            ps.setInt(1, id);
            ps.executeUpdate();

            String updateBook = "update book set quantity = quantity + ? where id = ?";
            ps = con.prepareStatement(updateBook);
            ps.setInt(1, bookLoan.getAmount());
            ps.setInt(2, bookLoan.getBookId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addToDB(BookLoans bookLoan) throws SQLException {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String query = "select quantity from book where id = ?";

        try (Connection con = DBConnector.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, bookLoan.getBookId());
            ResultSet rs = ps.executeQuery();
            int quantity = 0;
            if (rs.next()) {
                 quantity = rs.getInt("quantity");
            }

            if (bookLoan.getAmount() <= quantity) {
                String addBookLoan = "INSERT INTO bookLoans(userId, amount, startDate, dueDate, bookId, status)"
                                    + " VALUES (?,?,?,?,?,?)";
                PreparedStatement pst = con.prepareStatement(addBookLoan);
                //pst.setInt(1, bookLoan.getId());
                pst.setInt(1, bookLoan.getUserId());
                pst.setInt(2, bookLoan.getAmount());
                pst.setString(3, df.format(bookLoan.getStartDate()));
                pst.setString(4, df.format(bookLoan.getDueDate()));
                pst.setInt(5, bookLoan.getBookId());
                pst.setString(6, "chua tra");
                pst.executeUpdate();

                String updateBookQuantity = "UPDATE book SET quantity = quantity - ? WHERE id = ?";
                pst = con.prepareStatement(updateBookQuantity);
                pst.setInt(1, bookLoan.getAmount());
                pst.setInt(2, bookLoan.getBookId());
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BookLoans searchById(int id) {
        String searchQuery = "SELECT * FROM book WHERE id = ?";
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try (Connection con = DBConnector.getConnection();
            PreparedStatement pst = con.prepareStatement(searchQuery)){
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int Id = rs.getInt("id");
                int userId = rs.getInt("userId");
                int bookId = rs.getInt("bookId");
                int amount = rs.getInt("amount");
                String status = rs.getString("status");
                try {
                    Date startDate = df.parse(rs.getString("startDate"));
                    Date dueDate = df.parse(rs.getString("dueDate"));

                    BookLoans bookLoan = new BookLoans(Id, userId, bookId, startDate, dueDate, amount, status);
                    return bookLoan;
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
