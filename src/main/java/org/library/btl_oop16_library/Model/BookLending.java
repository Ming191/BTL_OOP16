package org.library.btl_oop16_library.Model;

import java.util.Date;

public class BookLending {
    private int id;
    private User user;
    private Book book;
    private int amount;
    private Date startDate;
    private Date dueDate;

    public BookLending(int id, User user, Book book, Date startDate, Date dueDate, int amount) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public int getAmount() {
        return amount;
    }
}
