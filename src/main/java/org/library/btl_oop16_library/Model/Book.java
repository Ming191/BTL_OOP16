package org.library.btl_oop16_library.Model;

public class Book {

    private int id;
    private String title;
    private String author;
    private String type;
    private String language;
    private int available;

    public Book() {
    }
    public Book(String title, int available) {
        this.title = title;
        this.available = available;
    }

    public Book(String title, String author, String type, String language, int available) {
        this.id = -1;
        this.title = title;
        this.author = author;
        this.type = type;
        this.language = language;
        this.available = available;
    }

    public Book(int id, String title, String author, String type, String language, int available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.type = type;
        this.language = language;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public int getAvailable() {
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

    public void setAvailable(int available) {
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
