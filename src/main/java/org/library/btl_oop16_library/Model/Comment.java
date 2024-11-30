package org.library.btl_oop16_library.Model;

public class Comment {
    private int id;
    private int bookId;
    private int userId;
    private String context;

    public Comment(int id, int bookId, int userId, String context) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.context = context;
    }

    public Comment(int bookId, int userId, String context) {
        this.bookId = bookId;
        this.userId = userId;
        this.context = context;
    }

    public int getId() {    
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
