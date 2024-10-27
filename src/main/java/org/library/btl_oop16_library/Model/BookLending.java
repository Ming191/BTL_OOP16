package org.library.btl_oop16_library.Model;

import java.sql.Date;

public class BookLending {
    private int id;
    private int bookId;
    private int userId;
    private int amount;
    private Date startDate;
    private Date dueDate;
    private String status;

    public BookLending(int id, int userId, int bookId, Date startDate, Date dueDate, int amount) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.amount = amount;
    }

    public BookLending(int userId, int bookId, Date startDate, Date dueDate, int amount) {
        this.id = -1;
        this.userId = userId;
        this.bookId = bookId;
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

    public void setAmount(int amount) {
        this.amount = amount;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
