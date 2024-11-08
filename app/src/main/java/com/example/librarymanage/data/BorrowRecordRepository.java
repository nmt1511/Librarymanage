package com.example.librarymanage.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.librarymanage.entities.BorrowRecord;

import java.util.ArrayList;
import java.util.List;

public class BorrowRecordRepository {
    private SQLiteDatabase db;
    private DataBook dbHelper;

    public BorrowRecordRepository(Context context) {
        dbHelper = new DataBook(context);
    }

    // Phương thức để thêm bản ghi mượn sách
    public boolean borrowBook(int userId, int bookId, String borrowDate, String returnDate, String status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("book_id", bookId);
        values.put("borrow_date", borrowDate);
        values.put("return_date", returnDate);
        values.put("status", status);

        long result = db.insert("BorrowRecords", null, values);
        db.close();
        return result != -1; // Trả về true nếu insert thành công
    }
    public List<BorrowRecord> getBorrowHistoryByUserId(int userId) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Sử dụng JOIN để kết hợp thông tin từ BorrowRecords và Books
        Cursor cursor = db.rawQuery(
                "SELECT b.title, br.user_id, br.book_id, br.borrow_date, br.return_date, br.status, b.title " +
                        "FROM BorrowRecords br " +
                        "JOIN Books b ON br.book_id = b.book_id " +
                        "WHERE br.user_id = ?",
                new String[]{String.valueOf(userId)}
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("title"));
                int bookId = cursor.getInt(cursor.getColumnIndexOrThrow("book_id"));
                String borrowDate = cursor.getString(cursor.getColumnIndexOrThrow("borrow_date"));
                String returnDate = cursor.getString(cursor.getColumnIndexOrThrow("return_date"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title")); // Lấy tiêu đề sách

                // Tạo một đối tượng BorrowRecord với thông tin sách
                borrowRecords.add(new BorrowRecord(id, userId, bookId, title, borrowDate, returnDate, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return borrowRecords;
    }

    public List<String> getAllBorrowRecords() {
        List<String> borrowRecords = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM BorrowRecords", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int recordId = cursor.getInt(cursor.getColumnIndexOrThrow("record_id"));
                String bookTitle = getBookTitle(cursor.getInt(cursor.getColumnIndexOrThrow("book_id")));
                String userName = getUserName(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                String borrowDate = cursor.getString(cursor.getColumnIndexOrThrow("borrow_date"));
                String returnDate = cursor.getString(cursor.getColumnIndexOrThrow("return_date"));

                // Chế tạo chuỗi thông tin bản ghi mượn để hiển thị
                String recordDetails = "Record ID: " + recordId + "\n" +
                        "Book: " + bookTitle + "\n" +
                        "User: " + userName + "\n" +
                        "Borrowed: " + borrowDate + "\n" +
                        "Return by: " + returnDate;

                borrowRecords.add(recordDetails);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return borrowRecords;
    }

    // Phương thức lấy tên sách từ book_id
    private String getBookTitle(int bookId) {
        Cursor cursor = db.rawQuery("SELECT title FROM Books WHERE book_id = ?", new String[]{String.valueOf(bookId)});
        if (cursor != null && cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            cursor.close();
            return title;
        }
        return "";
    }

    // Phương thức lấy tên người dùng từ user_id
    private String getUserName(int userId) {
        Cursor cursor = db.rawQuery("SELECT name FROM User WHERE user_id = ?", new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
            return name;
        }
        return "";
    }

    // Phương thức thêm bản ghi mượn mới
    public long addBorrowRecord(int bookId, int userId, String borrowDate, String returnDate) {
        ContentValues values = new ContentValues();
        values.put("book_id", bookId);
        values.put("user_id", userId);
        values.put("borrow_date", borrowDate);
        values.put("return_date", returnDate);
        return db.insert("BorrowRecords", null, values);
    }


    public void close() {
        dbHelper.close();
    }
}
