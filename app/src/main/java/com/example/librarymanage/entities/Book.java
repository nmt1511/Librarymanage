package com.example.librarymanage.entities;

public class Book {
    private int bookId;
    private String title;
    private String authorName;
    private String description;
    private int imageResource;
    private String categoryName;
    private String locationName;

    // Constructor với 7 tham số
    public Book(int bookId, String title, String authorName, String description, int imageResource, String categoryName, String locationName) {
        this.bookId = bookId;
        this.title = title;
        this.authorName = authorName;
        this.description = description;
        this.imageResource = imageResource;
        this.categoryName = categoryName;
        this.locationName = locationName;
    }

    // Getter và Setter cho tất cả các trường
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

}
