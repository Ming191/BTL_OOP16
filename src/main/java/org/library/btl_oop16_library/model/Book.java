package org.library.btl_oop16_library.model;

public class Book {

    private int id;
    private String title;
    private String description;
    private String author;
    private String category;
    private String language;
    private int available;
    private String imgURL;
    private String previewURL;
    private String rating;

    public Book(int id,
                String title,
                String description,
                String author,
                String category,
                String language,
                int available,
                String imgURL,
                String rating,
                String previewURL) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.category = category;
        this.language = language;
        this.available = available;
        this.imgURL = imgURL;
        this.rating = rating;
        this.previewURL = previewURL;
    }

    public Book(String title,
                String author,
                String category,
                String language,
                int quantity,
                String imgURL,
                String rating,
                String description,
                String previewURL) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.language = language;
        this.available = quantity;
        this.imgURL = imgURL;
        this.rating = rating;
        this.description = description;
        this.previewURL = previewURL;
    }

    public Book(int id,
                String title,
                String author,
                String category,
                String language,
                int quantity,
                String imgURL,
                String rating,
                String description,
                String previewURL) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.language = language;
        this.available = quantity;
        this.imgURL = imgURL;
        this.rating = rating;
        this.description = description;
        this.previewURL = previewURL;
    }

    public Book(String title,
                String author,
                String category,
                String language,
                String imgURL,
                String rating,
                String description,
                String previewURL) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.language = language;
        this.imgURL = imgURL;
        this.rating = rating;
        this.description = description;
        this.previewURL = previewURL;
    }

    public Book(String title,
                String description,
                String author,
                String category,
                String language,
                int available,
                String imgURL,
                String rating,
                String previewURL) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.category = category;
        this.language = language;
        this.available = available;
        this.imgURL = imgURL;
        this.rating = rating;
        this.previewURL = previewURL;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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


    public String getCategory() {
        return category;
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

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }
}
