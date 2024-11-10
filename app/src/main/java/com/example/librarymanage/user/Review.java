package com.example.librarymanage.user;

public class Review {
    private int rating;
    private String comment;
    private String reviewDate;

    public Review(int rating, String comment, String reviewDate) {
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public String getReviewDate() { return reviewDate; }
}
