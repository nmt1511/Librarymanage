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
        String query = "SELECT name FROM Author WHERE author_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(authorId)});
        String authorName = null; // Khởi tạo biến authorName là null
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndex("name"); // Lấy chỉ số cột
                if (index != -1) { // Kiểm tra nếu chỉ số cột hợp lệ
                    authorName = cursor.getString(index); // Lấy tên tác giả
                }
            }
            cursor.close(); // Đảm bảo đóng cursor
        }

        return authorName; // Trả về tên tác giả hoặc null
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





    public void close() {
        dataBook.close();
    }
}
