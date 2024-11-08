package com.example.librarymanage.entities;

public class Review {

    private int reviewId;
    private int bookId;
    private String reviewText;
    private int rating;

    public Review(int reviewId, int bookId, String reviewText, int rating) {
        this.reviewId = reviewId;
        this.bookId = bookId;
        this.reviewText = reviewText;
        this.rating = rating;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
