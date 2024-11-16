package org.library.btl_oop16_library.Model;

public class Copy {
    private int id;
    private int bookId;
    private String status;

    public Copy(int bookId) {
        this.bookId = bookId;
        this.status = "available";
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
