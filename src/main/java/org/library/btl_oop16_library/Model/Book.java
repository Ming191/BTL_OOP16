package org.library.btl_oop16_library.Model;

public class Book {

    private int id;
    private String title;
    private int authorId;
    private String type;
    private String language;
    private int quantity;

    public Book() {
    }

    public Book(String title, int authorId, String type, String language) {
        this.id = -1;
        this.title = title;
        this.authorId = authorId;
        this.type = type;
        this.language = language;
    }

    public Book(int id, String title, int authorId, String type, String language, int quantity) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.type = type;
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
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

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
