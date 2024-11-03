package com.example.librarymanage.entities;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private String description;
    private int imageResource;

    public Book(int bookId, String title, String author, String description, int imageResource) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.description = description;
        this.imageResource = imageResource;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResource() {
        return imageResource;
    }
}
