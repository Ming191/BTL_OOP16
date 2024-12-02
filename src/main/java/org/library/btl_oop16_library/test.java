package org.library.btl_oop16_library;

import dev.langchain4j.model.github.GitHubModelsChatModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static dev.langchain4j.model.github.GitHubModelsChatModelName.GPT_4_O_MINI;

public class test {
    public static void main(String[] args) {
        GitHubModelsChatModel model = GitHubModelsChatModel.builder()
                .gitHubToken("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")
                .modelName(GPT_4_O_MINI)
                .logRequestsAndResponses(true)
                .build();

        String dbUrl = "jdbc:sqlite:my.db";
        List<String> bookData = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(dbUrl)) {
            if (connection != null) {
                System.out.println("Connected to the database!");

                String query = "SELECT title, author, category FROM book";
                try (Statement statement = connection.createStatement()) {
                    ResultSet resultSet = statement.executeQuery(query);

                    while (resultSet.next()) {
                        String title = resultSet.getString("title");
                        String author = resultSet.getString("author");
                        String genre = resultSet.getString("category");
                        bookData.add(String.format("Title: %s, Author: %s, Genre: %s", title, author, genre));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return;
        }

        if (bookData.isEmpty()) {
            System.out.println("No books found in the database.");
            return;
        }

        StringBuilder bookDataInput = new StringBuilder("Here is the list of books:\n");
        for (String book : bookData) {
            bookDataInput.append(" - ").append(book).append("\n");
        }

        String userQuery = "Recommend me some books for lgbt or related topics. If there're no books of that type, return no books match my request";
        String prompt = bookDataInput + "\n" + userQuery;

        String aiResponse = model.generate(prompt);
        System.out.println("AI Recommendations:\n" + aiResponse);
    }
}
