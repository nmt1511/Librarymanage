package com.example.librarymanage.entities;

public class Review {
    private int bookId;
    private int userId;
    private String username;  // Tên người dùng
    private String bookTitle;  // Tên sách
    private String comment;
    private float rating;

    public Review(int bookId, int userId, String username, String bookTitle, String comment, float rating) {
        this.bookId = bookId;
        this.userId = userId;
        this.username = username;
        this.bookTitle = bookTitle;
        this.comment = comment;
        this.rating = rating;
    }

    // Getter và Setter
    public int getBookId() { return bookId; }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getBookTitle() { return bookTitle; }
    public String getComment() { return comment; }
    public float getRating() { return rating; }
    @Override
    public String toString() {
        return getBookTitle() + "\n" + getComment() + " (Rating: " + getRating() + ")";
    }
}