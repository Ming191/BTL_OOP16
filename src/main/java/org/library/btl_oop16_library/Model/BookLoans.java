package org.library.btl_oop16_library.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookLoans {
    private int id;
    private int bookId;
    private int userId;
    private String bookTitle;
    private String userName;
    private int amount;
    private Date startDate;
    private Date dueDate;
    private String status;

    public BookLoans(int id, String userName, String bookTitle, Date startDate, Date dueDate, int amount, String status) {
        this.id = id;
        this.userName = userName;
        this.bookTitle = bookTitle;
        this.amount = amount;
        this.status = status;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    public BookLoans(int userId, int bookId, Date startDate, Date dueDate, int amount, String status) {
        this.userId = userId;
        this.bookId = bookId;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.amount = amount;
        this.status = status;
    }

    public BookLoans(int userID, int bookID, Date startDate, Date dueDate, int amount) {
        this.userId = userID;
        this.bookId = bookID;
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

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
