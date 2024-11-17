package com.example.librarymanage.entities;

public class BorrowRecord2 {
    private int recordId;
    private String userName;
    private String bookTitle;
    private String borrowDate;
    private String returnDate;
    private String status;

    public BorrowRecord2(int recordId, String userName, String bookTitle, String borrowDate, String returnDate, String status) {
        this.recordId = recordId;
        this.userName = userName;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Các getter và setter cho các thuộc tính
    public String getUserName() {
        return userName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }

    // Set status
    public void setStatus(String status) {
        this.status = status;
    }
    public int getRecordId() {
        return recordId;
    }
}
