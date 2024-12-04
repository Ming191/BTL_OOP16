package org.library.btl_oop16_library.utils.database;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.library.btl_oop16_library.model.Book;
import org.library.btl_oop16_library.utils.general.ApplicationAlert;
import org.library.btl_oop16_library.utils.general.SessionManager;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookDBConnector extends DBConnector<Book> {
    private static final String TABLE_NAME = "book";

    private static BookDBConnector instance;
    private BookDBConnector() {}

    public static BookDBConnector getInstance() {
        if (instance == null) {
            instance = new BookDBConnector();
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
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("language"),
                        rs.getInt("quantity"),
                        rs.getString("imgURL"),
                        rs.getString("rating"),
                        rs.getString("previewURL")
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
        String getBookQuery = "SELECT title FROM book WHERE id = ?";
        String deleteBookQuery = "DELETE FROM book WHERE id = ?";

        String bookName = null;
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement getStmt = conn.prepareStatement(getBookQuery)) {
            getStmt.setInt(1, id);
            try (ResultSet rs = getStmt.executeQuery()) {
                if (rs.next()) {
                    bookName = rs.getString("title");
                }
            }
        }

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteBookQuery)) {
            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();
        }

        if (bookName != null) {
            ActivitiesDBConnector activitiesDB = ActivitiesDBConnector.getInstance();
            String adminName = SessionManager.getInstance().getCurrentUser().getName();
            activitiesDB.logActivity("Admin " + adminName + " deleted book: " + bookName);
        }
        else {
            System.out.println("Book not found, nothing to delete.");
        }

    }

    @Override
    public void addToDB(Book item) throws SQLException {
        String checkQuery = "SELECT id, quantity FROM " + TABLE_NAME + " WHERE title = ?";
        String updateQuery = "UPDATE " + TABLE_NAME + " SET quantity = quantity + ? WHERE id = ?";
        String insertQuery = "INSERT INTO " + TABLE_NAME + " (title, author, category, language, quantity, imgUrl, rating, description, previewURL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement psCheck = conn.prepareStatement(checkQuery)) {

            psCheck.setString(1, item.getTitle());
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                int existingQuantity = rs.getInt("quantity");
                int bookId = rs.getInt("id");
                int newQuantity = item.getAvailable();

                try (PreparedStatement psUpdate = conn.prepareStatement(updateQuery)) {
                    psUpdate.setInt(1, newQuantity);
                    psUpdate.setInt(2, bookId);
                    psUpdate.executeUpdate();
                }

                ActivitiesDBConnector activitiesDB = ActivitiesDBConnector.getInstance();
                String adminName = SessionManager.getInstance().getCurrentUser().getName();
                activitiesDB.logActivity("Admin " + adminName + " added " + newQuantity + " copies of book: '" + item.getTitle());
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
                    psInsert.setString(9, item.getPreviewURL());
                    psInsert.executeUpdate();
                }

                ActivitiesDBConnector activitiesDB = ActivitiesDBConnector.getInstance();
                String adminName = SessionManager.getInstance().getCurrentUser().getName();
                activitiesDB.logActivity(  "Admin " + adminName + " added " + item.getAvailable() + " copies of new book: '" + item.getTitle());
            }
        } catch (SQLException e) {
            throw new SQLException("Error while adding book or copy to DB", e);
        }
    }

    public void modifyBook(Book item) throws SQLException {
        String query = """
                update book
                set title = ?, author = ?, description = ?, quantity = quantity + ?
                where id = ?
                """;
        try (Connection conn = getConnection();
        PreparedStatement psUpdate = conn.prepareStatement(query)) {
            psUpdate.setString(1, item.getTitle());
            psUpdate.setString(2, item.getAuthor());
            psUpdate.setString(3, item.getDescription());
            psUpdate.setInt(4, item.getAvailable());
            psUpdate.setInt(5, item.getId());
            psUpdate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Book> searchById(int id) {
        String query = "SELECT * FROM book  WHERE id = ? OR CAST(id AS TEXT) LIKE ?";
        List<Book> bookList = new ArrayList<>();

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.setString(2, id + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("language"),
                        rs.getInt("quantity"),
                        rs.getString("imgURL"),
                        rs.getString("rating"),
                        rs.getString("description"),
                        rs.getString("previewURL")
                );
                bookList.add(book);
            }

            if (bookList.isEmpty()) {
                System.out.println("No books found with ID " + id + " or IDs starting with " + id + ".");
            } else {
                System.out.println("Found " + bookList.size() + " books with ID " + id + " or IDs starting with " + id + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to search books by ID: " + e.getMessage());
        }
        return bookList;
    }

    public List<Book> searchByAttributes(String value, String type) {
        String query = "SELECT * FROM book WHERE book." + type + " LIKE ?";
        List<Book> bookList = new ArrayList<>();

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + value + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("language"),
                        rs.getInt("quantity"),
                        rs.getString("imgURL"),
                        rs.getString("rating"),
                        rs.getString("description"),
                        rs.getString("previewURL")
                );
                bookList.add(book);
            }

            if (bookList.isEmpty()) {
                System.out.println("No books found with " + type + ": " + value);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to search books by " + type + ": " + e.getMessage());
        }

        return bookList;
    }


    @Override
    public void exportToExcel() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = currentDate.format(formatter);

        String outputFolderPath = "output";
        String outputFilePath = outputFolderPath + File.separator + formattedDate + "_book.xlsx";

        File outputFolder = new File(outputFolderPath);
        if (!outputFolder.exists()) {
            if (!outputFolder.mkdir()) {
                System.err.println("Failed to create output folder: " + outputFolder.getAbsolutePath());
                return;
            }
        }

        String query = "SELECT * FROM " + TABLE_NAME;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Books");

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

                    if ("quantity".equalsIgnoreCase(columnName)) {
                        int quantity = rs.getInt(i);
                        cell.setCellValue(quantity);
                    } else if("id".equalsIgnoreCase(columnName)) {
                        int id = rs.getInt(i);
                        cell.setCellValue(id);
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
            ApplicationAlert.exportSuccess();

        } catch (SQLException e) {
            System.err.println("Error while accessing database: " + e.getMessage());
            ApplicationAlert.exportFailed();
        } catch (IOException e) {
            System.err.println("Error while writing Excel file: " + e.getMessage());
            ApplicationAlert.exportFailed();
        }
    }

    @Override
    public void importFromExcel(String filePath) {
        File file = new File(filePath);
        ActivitiesDBConnector activitiesDB = ActivitiesDBConnector.getInstance();

        if (!file.exists()) {
            System.err.println("File does not exist: " + filePath);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                int id = (row.getCell(0) != null && row.getCell(0).getCellType() == CellType.NUMERIC)
                        ? (int) row.getCell(0).getNumericCellValue() : -1;
                String title = (row.getCell(1) != null && row.getCell(1).getCellType() == CellType.STRING)
                        ? row.getCell(1).getStringCellValue() : "";
                String author = (row.getCell(2) != null && row.getCell(2).getCellType() == CellType.STRING)
                        ? row.getCell(2).getStringCellValue() : "";
                String category = (row.getCell(3) != null && row.getCell(3).getCellType() == CellType.STRING)
                        ? row.getCell(3).getStringCellValue() : "";
                String language = (row.getCell(4) != null && row.getCell(4).getCellType() == CellType.STRING)
                        ? row.getCell(4).getStringCellValue() : "";
                int quantity = (row.getCell(5) != null && row.getCell(5).getCellType() == CellType.NUMERIC)
                        ? (int)row.getCell(5).getNumericCellValue() : -1;
                String imgURL = (row.getCell(6) != null && row.getCell(6).getCellType() == CellType.STRING)
                        ? row.getCell(6).getStringCellValue() : "";
                String rating = (row.getCell(7) != null && row.getCell(7).getCellType() == CellType.STRING)
                        ? row.getCell(7).getStringCellValue() : "";
                String description = (row.getCell(8) != null && row.getCell(8).getCellType() == CellType.STRING)
                        ? row.getCell(8).getStringCellValue() : "";
                String previewURL = (row.getCell(9) != null && row.getCell(9).getCellType() == CellType.STRING)
                        ? row.getCell(9).getStringCellValue() : "";

                upsertBook(id, title, author, category, language, quantity, imgURL, rating, description, previewURL);
            }
            System.out.println("Data successfully imported from Excel file: " + filePath);
            String adminName = SessionManager.getInstance().getCurrentUser().getName();
            activitiesDB.logActivity("Admin " + adminName + " imported file: " + new File(filePath).getName() + " to book table");

            ApplicationAlert.importSuccess();

        } catch (IOException e) {
            System.err.println("Error while reading Excel file: " + e.getMessage());
            ApplicationAlert.importFailed();
        }
    }

    private void upsertBook(int id, String title, String author, String category,
                            String language, int quantity, String imgURL, String rating, String description, String previewURL) {
        String upsertQuery = """
        INSERT INTO book (id, title, author , category, language, quantity, imgURL, rating, description, previewURL)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT (id) 
        DO UPDATE SET
            title = EXCLUDED.title,
            author = EXCLUDED.author,
            category = EXCLUDED.category,
            language = EXCLUDED.language,
            quantity = EXCLUDED.quantity,
            imgURL = EXCLUDED.imgURL,
            rating = EXCLUDED.rating,
            description = EXCLUDED.description,
            previewURL = EXCLUDED.previewURL
    """;

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(upsertQuery)) {
            ps.setInt(1, id);
            ps.setString(2, title);
            ps.setString(3, author);
            ps.setString(4, category);
            ps.setString(5, language);
            ps.setInt(6, quantity);
            ps.setString(7, imgURL);
            ps.setString(8, rating);
            ps.setString(9, description);
            ps.setString(10, previewURL);

            ps.executeUpdate();
            System.out.println("Book upserted: " + title);
        } catch (SQLException e) {
            System.err.println("Error while upserting book: " + e.getMessage());
        }
    }
}
