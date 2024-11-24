package org.library.btl_oop16_library.Util;

import org.library.btl_oop16_library.Model.BookLoans;
import org.library.btl_oop16_library.Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BookLoanDBConnector extends DBConnector<BookLoans> {
    private static BookLoanDBConnector instance;
    private static final Object lock = new Object();

    private BookLoanDBConnector() {}

    public static BookLoanDBConnector getInstance() {
        if (instance == null) {
            synchronized (lock) {
                instance = new BookLoanDBConnector();
            }
        }
        return instance;
    }

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
            String query = "SELECT bl.id, bl.userId, bl.bookId, bl.amount, u.name AS userName, b.title AS bookTitle " +
                    "FROM bookLoans bl " +
                    "JOIN user u ON bl.userId = u.id " +
                    "JOIN book b ON bl.bookId = b.id " +
                    "WHERE bl.id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            String userName = null;
            String bookTitle = null;
            int amount = 0;
            int bookId = 0;

            if (rs.next()) {
                userName = rs.getString("userName");
                bookTitle = rs.getString("bookTitle");
                amount = rs.getInt("amount");
                bookId = rs.getInt("bookId");
            }

            String deleteBookLoan = "UPDATE bookLoans SET status = 'returned' WHERE id = ?";
            ps = con.prepareStatement(deleteBookLoan);
            ps.setInt(1, id);
            ps.executeUpdate();

            String updateBook = "UPDATE book SET quantity = quantity + ? WHERE id = ?";
            ps = con.prepareStatement(updateBook);
            ps.setInt(1, amount);
            ps.setInt(2, bookId);
            ps.executeUpdate();

            if (userName != null && bookTitle != null) {
                ActivitiesDBConnector activitiesDB = new ActivitiesDBConnector();
                activitiesDB.logActivity(userName + " returned '" + bookTitle + "'");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addToDB(BookLoans bookLoan) throws SQLException {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String query = "SELECT quantity, title FROM book WHERE id = ?";
        String userQuery = "SELECT name FROM user WHERE id = ?";

        try (Connection con = DBConnector.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, bookLoan.getBookId());
            ResultSet rs = ps.executeQuery();

            int quantity = 0;
            String bookTitle = null;

            if (rs.next()) {
                quantity = rs.getInt("quantity");
                bookTitle = rs.getString("title");
            }

            ps = con.prepareStatement(userQuery);
            ps.setInt(1, bookLoan.getUserId());
            rs = ps.executeQuery();

            String userName = null;
            if (rs.next()) {
                userName = rs.getString("name");
            }

            if (bookLoan.getAmount() <= quantity) {
                String addBookLoan = "INSERT INTO bookLoans(userId, amount, startDate, dueDate, bookId, status)"
                        + " VALUES (?,?,?,?,?,?)";
                PreparedStatement pst = con.prepareStatement(addBookLoan);
                pst.setInt(1, bookLoan.getUserId());
                pst.setInt(2, bookLoan.getAmount());
                pst.setString(3, df.format(bookLoan.getStartDate()));
                pst.setString(4, df.format(bookLoan.getDueDate()));
                pst.setInt(5, bookLoan.getBookId());
                pst.setString(6, bookLoan.getStatus());
                pst.executeUpdate();

                String updateBookQuantity = "UPDATE book SET quantity = quantity - ? WHERE id = ?";
                pst = con.prepareStatement(updateBookQuantity);
                pst.setInt(1, bookLoan.getAmount());
                pst.setInt(2, bookLoan.getBookId());
                pst.executeUpdate();

                if (userName != null && bookTitle != null) {
                    ActivitiesDBConnector activitiesDB = new ActivitiesDBConnector();
                    activitiesDB.logActivity("User " + userName + " borrowed " + bookLoan.getAmount() + " of '" + bookTitle + "'.");
                }
            } else {
                System.out.println("Insufficient quantity for the book.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public BookLoans searchById(int id) {
        String searchQuery = "SELECT * FROM bookLoans WHERE id = ?";
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

                    return  new BookLoans(Id, userId, bookId, startDate, dueDate, amount, status);
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void exportToExcel() {

    }

    @Override
    public void importFromExcel(String filename) {

    }

    public void updateBookLoan() {
       String updateStatus = "update bookLoans set status = 'overdued'"
                            + " WHERE STRFTIME('%Y-%m-%d', SUBSTR(dueDate, 7, 4) || '-'\n"
                            + "|| SUBSTR(dueDate, 4, 2) || '-'\n"
                            + "|| SUBSTR(dueDate, 1, 2)) < DATE ('now')";
       try (Connection con = DBConnector.getConnection()) {
           PreparedStatement ps = con.prepareStatement(updateStatus);
           ps.executeUpdate();
       } catch (SQLException e) {
           e.printStackTrace();
       }
    }

    public boolean canLendBook(User user, int limit) {
        String query = "select ifnull(sum(amount),0) as quantity from bookLoans "
                     + "where status not in('returned','pre-ordered') and userId = ?";
        try (Connection con = DBConnector.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            int lendQuantity = 0;
            if (rs.next()) {
                lendQuantity = rs.getInt("quantity");
            }
            if (lendQuantity < limit) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean canPreorderBook(User user) {
        String query = "select ifnull(sum(amount),0) as quantity from bookLoans "
                     + "where status not like('pre-ordered') and userId = ?";
        try (Connection con = DBConnector.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            int lendQuantity = 0;
            if (rs.next()) {
                lendQuantity = rs.getInt("quantity");
            }
            if (lendQuantity > 20) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
