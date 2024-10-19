package org.library.btl_oop16_library;

import java.util.ArrayList;
import java.util.List;

public class BookList {
    List<Book> books;

    BookList() {
        books = new ArrayList<Book>();
    }

    public void viewBook() {
        for (Book book : books) {
            System.out.println(book.getTitle());
        }
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
