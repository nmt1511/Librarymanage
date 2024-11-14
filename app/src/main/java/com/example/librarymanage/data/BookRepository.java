package com.example.librarymanage.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.librarymanage.entities.Review;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookRepository {
    private DataBook dataBook;
    private SQLiteDatabase db;

    public BookRepository(Context context) {
        dataBook = new DataBook(context);
        db = dataBook.getWritableDatabase();
    }

    // Quản lý sách
    public long addBook(String title, int authorId, int categoryId, int locationId, int publishedYear, String description, String image, String addDate) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("author_id", authorId);
        values.put("category_id", categoryId);
        values.put("location_id", locationId);
        values.put("published_year", publishedYear);
        values.put("description", description);
        values.put("image", image);
        values.put("add_date", addDate);
        return db.insert("Books", null, values);
    }

    public Cursor getBookById(int bookId) {
        String query = "SELECT * FROM Books WHERE book_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(bookId)});
    }

    public Cursor getAllBooks() {
        String query = "SELECT * FROM Books";
        return db.rawQuery(query, null);
    }

    public Cursor getBooksByCategory(String categoryName) {
        String query = "SELECT * FROM Books WHERE category_id = (SELECT category_id FROM Category WHERE name = ?)";
        return db.rawQuery(query, new String[]{categoryName});
    }

    public Cursor getLatestBooksAddedThisMonth(int limit) {
        String currentMonth = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String currentYear = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());

        return db.rawQuery("SELECT * FROM Books WHERE strftime('%m', add_date) = ? AND strftime('%Y', add_date) = ? ORDER BY add_date DESC LIMIT ?",
                new String[]{currentMonth, currentYear, String.valueOf(limit)});
    }

    public Cursor getBooksByKeyword(String keyword) {
        String query = "SELECT * FROM Books WHERE title LIKE ? OR author_id IN (SELECT author_id FROM Author WHERE name LIKE ?)";
        return db.rawQuery(query, new String[]{"%" + keyword + "%", "%" + keyword + "%"});
    }

    public long updateBook(int bookId, String title, String authorName, String categoryName, String locationName, int publishedYear, String description) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("author_id", getAuthorIdByName(authorName));
        values.put("category_id", getCategoryIdByName(categoryName));
        values.put("location_id", getLocationIdByName(locationName));
        values.put("published_year", publishedYear);
        values.put("description", description);

        return db.update("Books", values, "book_id = ?", new String[]{String.valueOf(bookId)});
    }

    public void deleteBook(int bookId) {
        db.delete("Books", "book_id = ?", new String[]{String.valueOf(bookId)});
        db.close();
    }

    // Quản lý tác giả
    public long addAuthor(String authorName) {
        ContentValues values = new ContentValues();
        values.put("name", authorName);
        return db.insert("Author", null, values);
    }

    public boolean updateAuthor(String oldName, String newName) {
        ContentValues values = new ContentValues();
        values.put("name", newName);
        int rowsAffected = db.update("Author", values, "name = ?", new String[]{oldName});
        return rowsAffected > 0;
    }

    public boolean deleteAuthor(String authorName) {
        Cursor cursor = db.rawQuery("SELECT author_id FROM Author WHERE name = ?", new String[]{authorName});
        if (cursor != null && cursor.moveToFirst()) {
            int authorId = cursor.getInt(cursor.getColumnIndexOrThrow("author_id"));
            cursor.close();
            int rowsAffected = db.delete("Author", "author_id = ?", new String[]{String.valueOf(authorId)});
            return rowsAffected > 0;
        }
        return false;
    }




    public String getAuthorName(int authorId) {
        Cursor cursor = db.query("Author", new String[]{"name"}, "author_id = ?", new String[]{String.valueOf(authorId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String authorName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
            return authorName;
        }
        return "";
    }

    public List<String> getAllAuthors() {
        Cursor cursor = db.rawQuery("SELECT name FROM Author", null);
        List<String> authors = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                authors.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return authors;
    }

    // Quản lý thể loại
    public String getCategoryName(int categoryId) {
        Cursor cursor = db.rawQuery("SELECT name FROM Category WHERE category_id = ?", new String[]{String.valueOf(categoryId)});
        if (cursor != null && cursor.moveToFirst()) {
            String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
            return categoryName;
        }
        return "";
    }

    public List<String> getAllCategories() {
        Cursor cursor = db.rawQuery("SELECT name FROM Category", null);
        List<String> categories = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }

    // Quản lý địa điểm
    public long addLocation(String locationName) {
        ContentValues values = new ContentValues();
        values.put("name", locationName);
        return db.insert("Locations", null, values);
    }

    public long updateLocation(int locationId, String locationName) {
        ContentValues values = new ContentValues();
        values.put("name", locationName);
        return db.update("Locations", values, "location_id = ?", new String[]{String.valueOf(locationId)});
    }

    public boolean deleteLocation(String locationName) {
        Cursor cursor = db.rawQuery("SELECT location_id FROM Locations WHERE name = ?", new String[]{locationName});
        if (cursor != null && cursor.moveToFirst()) {
            int locationId = cursor.getInt(cursor.getColumnIndexOrThrow("location_id"));
            cursor.close();
            int rowsAffected = db.delete("Locations", "location_id = ?", new String[]{String.valueOf(locationId)});
            return rowsAffected > 0;
        }
        return false;
    }

    public String getLocationName(int locationId) {
        Cursor cursor = db.rawQuery("SELECT name FROM Locations WHERE location_id = ?", new String[]{String.valueOf(locationId)});
        if (cursor != null && cursor.moveToFirst()) {
            String locationName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
            return locationName;
        }
        return "";
    }

    public List<String> getAllLocations() {
        Cursor cursor = db.rawQuery("SELECT name FROM Locations", null);
        List<String> locations = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                locations.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return locations;
    }
// danh mục
public long addCategory(String categoryName) {
    ContentValues values = new ContentValues();
    values.put("name", categoryName);
    return db.insert("Category", null, values);
}

    // Cập nhật danh mục
    public boolean updateCategory(String oldName, String newName) {
        ContentValues values = new ContentValues();
        values.put("name", newName);
        int rowsAffected = db.update("Category", values, "name = ?", new String[]{oldName});
        return rowsAffected > 0;
    }

    // Xóa danh mục
    public boolean deleteCategory(String categoryName) {
        Cursor cursor = db.rawQuery("SELECT category_id FROM Category WHERE name = ?", new String[]{categoryName});
        if (cursor != null && cursor.moveToFirst()) {
            int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));
            cursor.close();
            int rowsAffected = db.delete("Category", "category_id = ?", new String[]{String.valueOf(categoryId)});
            return rowsAffected > 0;
        }
        return false;
    }

    public boolean deleteRating(float ratingValue) {
        int rowsAffected = db.delete("Reviews", "rating = ?", new String[]{String.valueOf(ratingValue)});
        return rowsAffected > 0;
    }

    // Các phương thức trợ giúp khác
    private int getAuthorIdByName(String authorName) {
        Cursor cursor = db.rawQuery("SELECT author_id FROM Author WHERE name = ?", new String[]{authorName});
        if (cursor != null && cursor.moveToFirst()) {
            int authorId = cursor.getInt(cursor.getColumnIndexOrThrow("author_id"));
            cursor.close();
            return authorId;
        }
        return -1;
    }

    private int getCategoryIdByName(String categoryName) {
        Cursor cursor = db.rawQuery("SELECT category_id FROM Category WHERE name = ?", new String[]{categoryName});
        if (cursor != null && cursor.moveToFirst()) {
            int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));
            cursor.close();
            return categoryId;
        }
        return -1;
    }

    private int getLocationIdByName(String locationName) {
        Cursor cursor = db.rawQuery("SELECT location_id FROM Locations WHERE name = ?", new String[]{locationName});
        if (cursor != null && cursor.moveToFirst()) {
            int locationId = cursor.getInt(cursor.getColumnIndexOrThrow("location_id"));
            cursor.close();
            return locationId;
        }
        return -1;
    }

    
    //feedback
    public String getUserNameById(int userId) {
        String userName = null;
        Cursor cursor = db.rawQuery("SELECT name FROM User WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    userName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                }
            } finally {
                cursor.close();
            }
        }
        return userName;
    }
    public List<Review> getReviewsForBook(int bookId) {
        List<Review> reviews = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Reviews WHERE book_id = ?", new String[]{String.valueOf(bookId)});
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                    String name = getUserNameById(userId); // Phương thức để lấy tên người dùng
                    String comment = cursor.getString(cursor.getColumnIndexOrThrow("comment"));
                    int rating = cursor.getInt(cursor.getColumnIndexOrThrow("rating"));
                    String reviewDate = cursor.getString(cursor.getColumnIndexOrThrow("review_date")); // Giả sử bạn đã lưu ngày đánh giá

                    reviews.add(new Review(bookId, userId, name, "", comment, rating, reviewDate));
                }
            } finally {
                cursor.close();
            }
        }
        return reviews;
    }
    
    public void addReview(Review review) {
        ContentValues values = new ContentValues();
        values.put("book_id", review.getBookId());
        values.put("user_id", review.getUserId());
        values.put("comment", review.getComment());
        values.put("rating", review.getRating());
        values.put("review_date", review.getReviewDate());

        db.insert("Reviews", null, values);
    }
    public boolean hasBorrowedBook(int userId, int bookId) {
        Cursor cursor = db.rawQuery("SELECT * FROM BorrowRecords WHERE user_id = ? AND book_id = ?", new String[]{String.valueOf(userId), String.valueOf(bookId)});
        boolean hasBorrowed = cursor.getCount() > 0;
        cursor.close();
        return hasBorrowed;
    }
    public boolean hasReviewedBook(int userId, int bookId) {
        Cursor cursor = db.rawQuery("SELECT * FROM Reviews WHERE user_id = ? AND book_id = ?", new String[]{String.valueOf(userId), String.valueOf(bookId)});
        boolean hasReviewed = cursor.getCount() > 0;
        cursor.close();
        return hasReviewed;
    }




    public void close() {
        dataBook.close();
    }
}
