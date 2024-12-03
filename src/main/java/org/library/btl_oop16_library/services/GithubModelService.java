package org.library.btl_oop16_library.services;

import dev.langchain4j.model.github.GitHubModelsChatModel;
import org.library.btl_oop16_library.model.Book;
import org.library.btl_oop16_library.utils.database.BookDBConnector;

import java.sql.SQLException;
import java.util.List;

import static dev.langchain4j.model.github.GitHubModelsChatModelName.GPT_4_O_MINI;

public class GithubModelService {
    private static final GitHubModelsChatModel model = GitHubModelsChatModel.builder()
            .gitHubToken("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")
            .modelName(GPT_4_O_MINI)
            .logRequestsAndResponses(true)
            .build();

    public static String generateAns(String prompt) {
        return model.generate(prompt);
    }

    public static String makePrompt(String userMessage) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are an AI-powered library assistant for a library named 2-min library ")
                .append("Your role is to assist borrowers by providing accurate and helpful information about our library’s book catalog. Follow these instructions carefully:\n")
                .append("Your Role and Scope:\n")
                .append("Answer questions only related to books, including titles, authors, genres, categories, and recommendations.\n")
                .append("Refuse to answer questions unrelated to books or the library. Simply respond with: \"I'm here to assist with book-related queries.\n");

        prompt.append("Library Policies:\n")
                .append("Borrowers can pre-order books if they have borrowed more than 20 books in the past.\n")
                .append("Borrowers must visit the library in person to borrow books. Online or phone borrowing is not allowed.\n")
                .append("Ensure all responses about borrowing or pre-ordering highlight these policies.\n");

        prompt.append("Recommendation Guidelines:\n")
                .append("Provide book recommendations based on user interests or genres.\n")
                .append("Include the book title, author, and a brief description or review.\n")
                .append("Example:\n")
                .append("      *Title*: \"Dune\"\n")
                .append("      *Author*: Frank Herbert\n")
                .append("      *Description*: A classic science fiction novel about politics, religion, and ecology on a desert planet.\n\n");

        prompt.append("Catalog Information:\n")
                .append("The library catalog includes the following books:\n");
        try {
            List<Book> books = BookDBConnector.getInstance().importFromDB();
            for (Book book : books) {
                prompt.append(book.toString()).append("\n");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch books from the database.", e);
        }
        prompt.append("Use this catalog to answer user queries. Match their interests to relevant titles.");

        prompt.append("Formatting:\n")
                .append("Responses should be in plain text with no special characters, Markdown, or HTML.\n")
                .append("Keep answers concise and avoid unnecessary details.\n");

        prompt.append("User Query:\n")
                .append("Here is the question from the borrower:\n")
                .append(userMessage).append("\n");

        return prompt.toString();
    }
}
