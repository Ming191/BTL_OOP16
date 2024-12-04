package org.library.btl_oop16_library.services;

import dev.langchain4j.model.github.GitHubModelsChatModel;
import org.library.btl_oop16_library.model.Book;
import org.library.btl_oop16_library.model.BookLoans;
import org.library.btl_oop16_library.model.Comment;
import org.library.btl_oop16_library.utils.database.BookDBConnector;
import org.library.btl_oop16_library.utils.database.BookLoanDBConnector;
import org.library.btl_oop16_library.utils.database.CommentsDBConnector;
import org.library.btl_oop16_library.utils.general.SessionManager;

import java.sql.SQLException;
import java.util.List;

import static dev.langchain4j.model.github.GitHubModelsChatModelName.GPT_4_O_MINI;

public class GithubModelService {
    private static final GitHubModelsChatModel model = GitHubModelsChatModel.builder()
            .gitHubToken(ENV.getInstance().get("GITHUB_APIKEY"))
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
                .append("Refuse to answer questions unrelated to books or the library.");

        prompt.append("Library Policies:\n")
                .append("Borrowers can pre-order books if they have borrowed more than 20 books in the past.\n")
                .append("Borrowers must visit the library in person to borrow books (but can pre-order online). Online or phone borrowing is not allowed.\n")
                .append("Ensure all responses about borrowing or pre-ordering highlight these policies.\n");

        prompt.append("Recommendation Guidelines:\n")
                .append("Provide book recommendations based on user interests or genres.\n")
                .append("Include the book title, author, and a brief description or review.\n");

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

        prompt.append("Popular Books or most borrowed books in library:\n")
                .append("Here are the top 3 most borrowed books in our library:\n");
        List<Book> popularBooks = BookLoanDBConnector.getInstance().getTop3Books();
        for (Book book : popularBooks) {
            prompt.append(book.toString()).append("\n");
        }

        prompt.append("Borrowing History:\n")
                .append("- Borrowers can ask for their borrowing history\n")
                .append("- Include the title, start date, due date, and status of each borrowed book.\n")
                .append("- Example:\n")
                .append("  - Title: \"Dune\"\n")
                .append("    Start Date: 2024-01-01\n")
                .append("    Due Date: 2024-01-14\n")
                .append("    Status: Returned\n\n");
        List<BookLoans> borrowingHistory = BookLoanDBConnector.getInstance().importFromDB(SessionManager.getInstance().getCurrentUser());
        prompt.append("User Borrowing History:\n");
        if (borrowingHistory.isEmpty()) {
            prompt.append("The user has no borrowing history.\n");
        } else {
            prompt.append("Here is the borrowing history of the current user:\n");
            for (BookLoans loan : borrowingHistory) {
                prompt.append(String.format("- Book Title: \"%s\", Amount: %d, Start Date: %s, Due Date: %s, Status: %s\n",
                        loan.getBookTitle(), loan.getAmount(), loan.getStartDate(), loan.getDueDate(), loan.getStatus()));
            }
        }

        prompt.append("Asking about comments of a book:\n")
                .append("Borrowers can ask about comments(review) of a book:\n")
                .append("- Include the name of the commentor, the context of the comments\n")
                .append("-Example:\n")
                .append("- Commented by \"LocBeo\"\n")
                .append("    Context: \"Wow wow wow\"\n\n");
        try {
            List<Book> books = BookDBConnector.getInstance().importFromDB();
            for (Book book : books) {
                prompt.append(book.toString()).append("\n");
                List<Comment> comments = CommentsDBConnector.getInstance().searchByBookId(book.getId());
                if (!comments.isEmpty()) {
                    prompt.append("Comments for this book:\n");
                    for (Comment comment : comments) {
                        prompt.append(String.format("- Commented by \"%s\"\n  Context: \"%s\"\n\n", SessionManager.getInstance().getCurrentUser().getName(), comment.getContext()));
                    }
                } else {
                    prompt.append("No comments available for this book.\n");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch books from the database.", e);
        }

        prompt.append("Also response about how to use the library app\n")
                .append("The mainmenu contains Dashboard, Services, Books, Settings, LogOut\n")
                .append("DashBoard contains this chat box, admin information, some charts about library\n")
                .append("Services contains information about this user bookloan\n")
                .append("Books contains information about library book and can view book details by click a book then click view details\n")
                .append("Settings contains change theme button (just by click to change theme), change password button (then enter info to change), update information button(then enter info).\n")
                .append("In Services and Books also contains search box to search by the type the user choose by the choice box(type search like title, id,...) nearby")
                .append("So answer like: \n")
                .append("To change theme of the app:\n")
                .append("Step 1: Click Settings")
                .append("Step 2: Click Change Theme");

        prompt.append("Use this catalog to answer user queries. Match their interests to relevant titles.");

        prompt.append("Formatting:\n")
                .append("Responses should be in 'plain text' with no special characters, Markdown, or HTML.\n")
                .append("REMEMBER DONT ANSWER IN MARKDOWN FORMAT. ")
                .append("Keep answers concise and avoid unnecessary details.\n");

        prompt.append("User Query:\n")
                .append("Here is the question from the borrower:\n")
                .append(userMessage).append("\n");

        return prompt.toString();
    }
}
