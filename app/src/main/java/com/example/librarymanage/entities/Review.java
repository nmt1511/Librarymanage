package com.example.librarymanage.entities;

public class Review {
    private int bookId;
    private int userId;
    private String name;  // Tên người dùng
    private String bookTitle;  // Tên sách
    private String comment;
    private int rating;
    private String reviewDate; // Thêm thuộc tính ngày đánh giá

    public Review(int bookId, int userId, String name, String bookTitle, String comment, int rating, String reviewDate) {
        this.bookId = bookId;
        this.userId = userId;
        this.name = name;
        this.bookTitle = bookTitle;
        this.comment = comment;
        this.rating = rating;
        this.reviewDate = reviewDate; // Khởi tạo ngày đánh giá
    }

    // Getter và Setter
    public int getBookId() { return bookId; }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getBookTitle() { return bookTitle; }
    public String getComment() { return comment; }
    public int getRating() { return rating; }
    public String getReviewDate() { return reviewDate; } // Getter cho reviewDate
}