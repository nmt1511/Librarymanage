package com.example.librarymanage.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.librarymanage.entities.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewRepository {

    private SQLiteDatabase db;
    private DataBook dataBook;

    public ReviewRepository(Context context) {
        dataBook = new DataBook(context);
        db = dataBook.getWritableDatabase();
    }

    // Add a new review
    public long addReview(int bookId, String reviewText, int rating) {
        ContentValues values = new ContentValues();
        values.put("book_id", bookId);
        values.put("review_text", reviewText);
        values.put("rating", rating);

        return db.insert("Reviews", null, values);
    }

    // Get all reviews
    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Reviews", null);
        if (cursor.moveToFirst()) {
            do {
                int reviewId = cursor.getInt(cursor.getColumnIndexOrThrow("review_id"));
                int bookId = cursor.getInt(cursor.getColumnIndexOrThrow("book_id"));
                String reviewText = cursor.getString(cursor.getColumnIndexOrThrow("review_text"));
                int rating = cursor.getInt(cursor.getColumnIndexOrThrow("rating"));

                reviews.add(new Review(reviewId, bookId, reviewText, rating));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reviews;
    }

    // Get reviews for a specific book
    public List<Review> getReviewsByBookId(int bookId) {
        List<Review> reviews = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Reviews WHERE book_id = ?", new String[]{String.valueOf(bookId)});
        if (cursor.moveToFirst()) {
            do {
                int reviewId = cursor.getInt(cursor.getColumnIndexOrThrow("review_id"));
                String reviewText = cursor.getString(cursor.getColumnIndexOrThrow("review_text"));
                int rating = cursor.getInt(cursor.getColumnIndexOrThrow("rating"));

                reviews.add(new Review(reviewId, bookId, reviewText, rating));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reviews;
    }

    // Delete a review
    public void deleteReview(int reviewId) {
        db.delete("Reviews", "review_id = ?", new String[]{String.valueOf(reviewId)});
    }

    // Close the database connection
    public void close() {
        dataBook.close();
    }
}
