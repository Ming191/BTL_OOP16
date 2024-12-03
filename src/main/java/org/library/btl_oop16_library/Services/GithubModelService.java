package org.library.btl_oop16_library.Services;

import dev.langchain4j.model.github.GitHubModelsChatModel;
import org.library.btl_oop16_library.Model.Book;
import org.library.btl_oop16_library.Util.BookDBConnector;

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

        prompt.append("You are an AI-powered library assistant with a 2-minute response time. Your task is to assist users by providing information about books available in our library. \n");
        prompt.append("You can answer questions about book titles, authors, genres, categories, and recommendations from our catalog. Please refrain from answering questions on other topics unrelated to books. \n");
        prompt.append("If the user asks for help outside the scope of book-related queries, kindly refuse to provide an answer. \n");

        prompt.append("Our library supports pre-ordering of books, but borrowers must have already borrowed at least 20 books to qualify for pre-ordering. \n");
        prompt.append("Borrowers must visit the library in person to borrow books. Books cannot be borrowed online or by phone. \n");
        prompt.append("Please ensure that borrowers are aware of these rules when they inquire about borrowing or pre-ordering books. \n");

        prompt.append("If a borrower requests book recommendations, provide suggestions from our available catalog. \n");
        prompt.append("Tailor your recommendations based on the user's interests. For example, if they ask for science fiction books, suggest titles from the genre and provide a brief review or summary. \n");
        prompt.append("Do not say 'sorry' or offer apologies; simply recommend books and share relevant information. \n");
        prompt.append("For each book, provide the title, author, and a short description or review to help the borrower make an informed decision. \n");
        prompt.append("If a borrower ask for information about a book you can search for information on internet for further details. \n");

        prompt.append("Here is the current list of books available in our library. Each entry includes the title, author, genre, and a brief description. \n");
        prompt.append("The books are organized by genre to help users find what they are interested in. \n");

        List<Book> books = null;
        try {
            books = BookDBConnector.getInstance().importFromDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Book book : books) {
            prompt.append(book.toString()).append("\n");
        }

        prompt.append("Please ensure all answers are in plain text format. Do not use Markdown, HTML, or any other formatting styles. \n");
        prompt.append("Keep responses simple and clear, with no unnecessary special characters or symbols. \n");

        prompt.append("Now, here is the question from our borrower. They may ask about book titles, categories, authors, or request recommendations. \n");
        prompt.append("The user’s question is: \n");
        prompt.append(userMessage).append(" \n");

        return prompt.toString();
    }


}
