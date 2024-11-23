package org.library.btl_oop16_library.Util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.library.btl_oop16_library.Model.Book;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
        String insertQuery = "INSERT INTO " + TABLE_NAME + " (title, author, category, language, quantity, imgUrl, rating, description, previewURL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
                    psInsert.setString(9,item.getPreviewURL());
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
                            rs.getString("description"),
                            rs.getString("author"),
                            rs.getString("category"),
                            rs.getString("language"),
                            rs.getInt("quantity"),
                            rs.getString("imgURL"),
                            rs.getString("rating"),
                            rs.getString("previewURL")
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
    public void importFromExcel(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("File does not exist: " + filePath);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            String deleteQuery = "DELETE FROM book";
            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
                ps.executeUpdate();
                System.out.println("All existing data deleted from table 'book'.");
            } catch (SQLException e) {
                System.err.println("Error while deleting old data: " + e.getMessage());
                return;
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                int id = row.getCell(0) != null ? (int) row.getCell(0).getNumericCellValue() : -1;
                String title = row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "";
                String author = row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "";
                String category = row.getCell(3) != null ? row.getCell(3).getStringCellValue() : "";
                String language = row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "";
                int quantity = row.getCell(5) != null ? (int) row.getCell(5).getNumericCellValue() : 0;
                String imgURL = row.getCell(6) != null ? row.getCell(6).getStringCellValue() : "";
                String rating = row.getCell(7) != null ? row.getCell(7).getStringCellValue() : "";
                String description = row.getCell(8) != null ? row.getCell(8).getStringCellValue() : "";
                String previewURL = row.getCell(9) != null ? row.getCell(9).getStringCellValue() : "";


                insertBookToDB(id,title,author,category,language,quantity,imgURL,rating,description,previewURL);
            }

            System.out.println("Data successfully imported from Excel file: " + filePath);

        } catch (IOException e) {
            System.err.println("Error while reading Excel file: " + e.getMessage());
        }
    }

    private void insertBookToDB(int id, String title, String author, String category, String language, int quantity, String imgURL, String rating, String description, String previewURL) {
        String query = "INSERT INTO book (id, title, author, category, language, quantity, imgUrl, rating, description, previewURL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
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

        } catch (SQLException e) {
            System.err.println("Error while inserting book data into DB: " + e.getMessage());
        }
    }
}
