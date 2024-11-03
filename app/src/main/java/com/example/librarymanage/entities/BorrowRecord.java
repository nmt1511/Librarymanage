package com.example.librarymanage.entities;

public class BorrowRecord {
    private int recordId;
    private int userId;
    private int bookId;
    private String borrowDate;
    private String returnDate;
    private String status;

    private String bookTitle;  // Tiêu đề sách, nếu bạn muốn hiển thị

    // Constructor để khởi tạo các thuộc tính chính
    public BorrowRecord(int recordId, int userId, int bookId, String borrowDate, String returnDate, String status) {
        this.recordId = recordId;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Constructor khác nếu bạn muốn lưu cả tiêu đề sách
    public BorrowRecord(int recordId, int userId, int bookId, String borrowDate, String returnDate, String status, String bookTitle) {
        this.recordId = recordId;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
        this.bookTitle = bookTitle;
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

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}
