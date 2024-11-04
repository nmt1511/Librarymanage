package com.example.librarymanage.entities;

public class BorrowRecord {
    private int recordId;
    private int userId;
    private int bookId;
    private String title; // Thêm trường title
    private String borrowDate;
    private String returnDate;
    private String status;

    public BorrowRecord(int recordId, int userId, int bookId, String title, String borrowDate, String returnDate, String status) {
        this.recordId = recordId;
        this.userId = userId;
        this.bookId = bookId;
        this.title = title; // Khởi tạo title
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getters và Setters cho các thuộc tính
    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
