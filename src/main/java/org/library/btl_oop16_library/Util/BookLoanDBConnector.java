package org.library.btl_oop16_library.Util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.library.btl_oop16_library.Model.BookLoans;
import org.library.btl_oop16_library.Model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public List<BookLoans> importFromDB() {
        List<BookLoans> bookLoans = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String query =  "select bookLoans.*, user.name, book.title\n" +
                        "from bookLoans\n" +
                        "join user  on bookLoans.userId = user.id\n" +
                        "join book on bookLoans.bookId = book.id";
        try (Connection con = DBConnector.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String userName = rs.getString("name");
                String bookTitle = rs.getString("title");
                int amount = rs.getInt("amount");
                String status = rs.getString("status");
                try {
                    Date startDate = df.parse(rs.getString("startDate"));
                    Date dueDate = df.parse(rs.getString("dueDate"));

                    BookLoans bookLoan = new BookLoans(id, userName, bookTitle, startDate, dueDate, amount, status);
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

    public List<BookLoans> importFromDBForUser(User user) {
        List<BookLoans> bookLoans = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String query =  "select bookLoans.*, user.name, book.title\n" +
                        "from bookLoans\n" +
                        "join user  on bookLoans.userId = user.id\n" +
                        "join book on bookLoans.bookId = book.id\n" +
                        "where userId= ?";
        try (Connection con = DBConnector.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String userName = rs.getString("name");
                String bookTitle = rs.getString("title");
                int amount = rs.getInt("amount");
                String status = rs.getString("status");
                try {
                    Date startDate = df.parse(rs.getString("startDate"));
                    Date dueDate = df.parse(rs.getString("dueDate"));

                    BookLoans bookLoan = new BookLoans(id, userName, bookTitle, startDate, dueDate, amount, status);
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
        String query = "insert into bookLoans (userId, bookId, amount, startDate, dueDate, status)" +
                        " values (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnector.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, bookLoan.getUserId());
            ps.setInt(2, bookLoan.getBookId());
            ps.setInt(3, bookLoan.getAmount());
            ps.setString(4, df.format(bookLoan.getStartDate()));
            ps.setString(5, df.format(bookLoan.getDueDate()));
            ps.setString(6, bookLoan.getStatus());
            ps.executeUpdate();

            String updateBook = "update book set quantity = quantity - ? WHERE id = ?";
            ps = con.prepareStatement(updateBook);
            ps.setInt(1, bookLoan.getAmount());
            ps.setInt(2, bookLoan.getBookId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BookLoans searchById(int id) {
        String searchQuery = "select * from bookLoans\n" +
                        "join user on bookLoans.userId = user.id\n" +
                        "join book on bookLoans.bookId = book.id\n" +
                        "where bookLoans.id = ?";
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try (Connection con = DBConnector.getConnection();
            PreparedStatement pst = con.prepareStatement(searchQuery)){
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int Id = rs.getInt("id");
                String userName = rs.getString("name");
                String bookTitle = rs.getString("title");
                int amount = rs.getInt("amount");
                String status = rs.getString("status");
                try {
                    Date startDate = df.parse(rs.getString("startDate"));
                    Date dueDate = df.parse(rs.getString("dueDate"));

                    return  new BookLoans(Id, userName, bookTitle, startDate, dueDate, amount, status);
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
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = currentDate.format(formatter);

        String outputFolderPath = "output";
        String outputFilePath = outputFolderPath + File.separator + formattedDate + "_bookLoan.xlsx";

        File outputFolder = new File(outputFolderPath);
        if (!outputFolder.exists()) {
            if (!outputFolder.mkdir()) {
                System.err.println("Failed to create output folder: " + outputFolder.getAbsolutePath());
                return;
            }
        }

        String query =  "select * from bookLoans\n" +
                        "join user on bookLoans.userId = user.id\n" +
                        "join book on bookLoans.bookId = book.id";
        try (Connection conn = getConnection()){
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("BookLoans");

            int columnCount = rs.getMetaData().getColumnCount();
            Row headerRow = sheet.createRow(0);
            for (int i = 1; i <= columnCount; i++) {
                Cell cell = headerRow.createCell(i - 1);
                cell.setCellValue(rs.getMetaData().getColumnName(i));
            }

            int rowNum = 1;
            while (rs.next()) {
                Row dataRow = sheet.createRow(rowNum++);
                for (int i = 1; i <= columnCount; i++) {
                    Cell cell = dataRow.createCell(i - 1);
                    String columnName = rs.getMetaData().getColumnName(i);

                    if ("amount".equalsIgnoreCase(columnName)) {
                        int quantity = rs.getInt(i);
                        cell.setCellValue(quantity);
                    } else {
                        cell.setCellValue(rs.getString(i));
                    }
                }
            }

            for (int i = 0; i < columnCount; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(outputFilePath)) {
                workbook.write(fileOut);
            }

            workbook.close();

            System.out.println("Data successfully exported to: " + outputFilePath);

        } catch (SQLException e) {
            System.err.println("Error while accessing database: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error while writing Excel file: " + e.getMessage());
        }
    }

    @Override
    public void importFromExcel(String filename) {
        File file = new File(filename);

        if (!file.exists()) {
            System.err.println("File does not exist: " + filename);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            String deleteQuery = "DELETE FROM bookLoans";
            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
                ps.executeUpdate();
                System.out.println("All existing data deleted from table 'bookLoans'.");
            } catch (SQLException e) {
                System.err.println("Error while deleting old data: " + e.getMessage());
                return;
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                int id = row.getCell(0) != null ? (int) row.getCell(0).getNumericCellValue() : -1;
                int userId = row.getCell(1) != null ? (int) row.getCell(1).getNumericCellValue() : -1;
                int amount = row.getCell(2) != null ? (int) row.getCell(2).getNumericCellValue() : -1;
                String startDate = row.getCell(3) != null ? (String) row.getCell(3).getStringCellValue() : "";
                String dueDate = row.getCell(4) != null ? (String) row.getCell(4).getStringCellValue() : "";
                int bookId = row.getCell(5) != null ? (int) row.getCell(5).getNumericCellValue() : -1;
                String status = row.getCell(6) != null ? (String) row.getCell(6).getStringCellValue() : "";

                insertBookLoanToDB(id,userId, amount, startDate, dueDate, bookId, status);
            }

            System.out.println("Data successfully imported from Excel file: " + filename);

        } catch (IOException e) {
            System.err.println("Error while reading Excel file: " + e.getMessage());
        }
    }

    private void insertBookLoanToDB(int id, int userId, int amount, String startDate, String dueDate, int bookId, String status) {
        String query = "insert into bookLoans values(?,?,?,?,?,?,?)";
        try (Connection con = DBConnector.getConnection()){
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.setInt(2,userId);
            ps.setInt(3,amount);
            ps.setString(4,startDate);
            ps.setString(5,dueDate);
            ps.setInt(6,bookId);
            ps.setString(7,status);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while inserting book loan: " + e.getMessage());
        }
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

    public List<User> selectOverdue() {
        List<User> users = new ArrayList<>();
        String query = "select * from bookLoans where status like('overdued')";

        try (Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
