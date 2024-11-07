package com.example.librarymanage.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookRepository {
    private DataBook dataBook;

    public BookRepository(Context context) {
        dataBook = new DataBook(context);
    }

    public Cursor getBookById(int bookId) {
        SQLiteDatabase db = dataBook.getReadableDatabase();
        String query = "SELECT * FROM Books WHERE book_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(bookId)});
    }
    public Cursor getAllBooks() {
        SQLiteDatabase db = dataBook.getReadableDatabase();
        String query = "SELECT * FROM Books"; // Truy vấn tất cả sách
        return db.rawQuery(query, null);
    }
    public String getAuthorName(int authorId) {
        SQLiteDatabase db = dataBook.getReadableDatabase();
        Cursor cursor = db.query("Author", new String[]{"name"}, "author_id = ?", new String[]{String.valueOf(authorId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String authorName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
            return authorName;
        }
        return "";
    }

    public String getCategoryName(int categoryId) {
        SQLiteDatabase db = dataBook.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM Category WHERE category_id = ?", new String[]{String.valueOf(categoryId)});
        if (cursor != null && cursor.moveToFirst()) {
            String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
            return categoryName;
        }
        return "";
    }

    public String getLocationName(int locationId) {
        SQLiteDatabase db = dataBook.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM Locations WHERE location_id = ?", new String[]{String.valueOf(locationId)});
        if (cursor != null && cursor.moveToFirst()) {
            String locationName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
            return locationName;
        }
        return "";
    }


    public Cursor getBooksByCategory(String categoryName) {
        SQLiteDatabase db = dataBook.getReadableDatabase();
        String query = "SELECT * FROM Books WHERE category_id = (SELECT category_id FROM Category WHERE name = ?)";
        return db.rawQuery(query, new String[]{categoryName});
    }

    public Cursor getLatestBooksAddedThisMonth(int limit) {
        SQLiteDatabase db = dataBook.getReadableDatabase();
        String currentMonth = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String currentYear = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());

        // Query để lấy 5 sách được thêm trong tháng hiện tại
        return db.rawQuery("SELECT * FROM Books WHERE strftime('%m', add_date) = ? AND strftime('%Y', add_date) = ? ORDER BY add_date DESC LIMIT ?",
                new String[]{currentMonth, currentYear, String.valueOf(limit)});
    }

    public Cursor getBooksByKeyword(String keyword) {
        SQLiteDatabase db = dataBook.getReadableDatabase();
        String query = "SELECT * FROM Books WHERE title LIKE ? OR author_id IN (SELECT author_id FROM Author WHERE name LIKE ?)";
        return db.rawQuery(query, new String[]{"%" + keyword + "%", "%" + keyword + "%"});
    }




    public void close() {
        dataBook.close();
    }
}
