package org.library.btl_oop16_library;

public class Book {
    private String title;
    private String author;
    private String type;
    private String language;
    private String available;

    public String getAuthor() {
        return author;
    }

    public String getAvailable() {
        return available;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
