package com.example.librarymanage.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.librarymanage.entities.BorrowRecord;
import com.example.librarymanage.entities.BorrowRecord2;

import java.util.ArrayList;
import java.util.List;

public class BorrowRecordRepository {
    private SQLiteDatabase db;
    private DataBook dbHelper;

    public BorrowRecordRepository(Context context) {
        dbHelper = new DataBook(context);
        db = dbHelper.getWritableDatabase();
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



    public boolean deleteBorrowRecord(int recordId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("BorrowRecords", "record_id = ?", new String[]{String.valueOf(recordId)});
        db.close();
        return result > 0; // Trả về true nếu xóa thành công
    }










    public List<BorrowRecord2> getAllBorrowRecordsWithDetails() {
        List<BorrowRecord2> borrowRecordList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Truy vấn kết hợp dữ liệu từ các bảng
        String query = "SELECT br.record_id, br.borrow_date, br.return_date, br.actual_return_date, br.status, " +
                "u.name AS user_name, b.title AS book_title " +
                "FROM BorrowRecords AS br " +
                "JOIN User AS u ON br.user_id = u.user_id " +
                "JOIN Books AS b ON br.book_id = b.book_id";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int recordId = cursor.getInt(cursor.getColumnIndexOrThrow("record_id"));
                String borrowDate = cursor.getString(cursor.getColumnIndexOrThrow("borrow_date"));
                String returnDate = cursor.getString(cursor.getColumnIndexOrThrow("return_date"));
                String actualReturnDate = cursor.getString(cursor.getColumnIndexOrThrow("actual_return_date"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String userName = cursor.getString(cursor.getColumnIndexOrThrow("user_name"));
                String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow("book_title"));

                BorrowRecord2 record = new BorrowRecord2(recordId, userName, bookTitle, borrowDate, returnDate, actualReturnDate, status);
                borrowRecordList.add(record);
            }
            cursor.close();
        }
        db.close();
        return borrowRecordList;
    }
    public BorrowRecord2 getBorrowRecordById2(int recordId) {
        String query = "SELECT br.record_id, br.borrow_date, br.return_date, br.actual_return_date, br.status, " +
                "u.name AS user_name, b.title AS book_title " +  // b.title is the correct reference to the Books table
                "FROM BorrowRecords AS br " +
                "JOIN User AS u ON br.user_id = u.user_id " +
                "JOIN Books AS b ON br.book_id = b.book_id " +  // Ensure this JOIN brings in the title column
                "WHERE br.record_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(recordId)});

        if (cursor != null && cursor.moveToFirst()) {
            // Extract data from the cursor
             recordId = cursor.getInt(cursor.getColumnIndexOrThrow("record_id"));
            String borrowDate = cursor.getString(cursor.getColumnIndexOrThrow("borrow_date"));
            String returnDate = cursor.getString(cursor.getColumnIndexOrThrow("return_date"));
            String actualReturnDate = cursor.getString(cursor.getColumnIndexOrThrow("actual_return_date"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            String userName = cursor.getString(cursor.getColumnIndexOrThrow("user_name"));
            String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow("book_title"));  // Retrieve the book title here

            // Create and return a BorrowRecord2 object
            BorrowRecord2 borrowRecord2 = new BorrowRecord2(recordId, userName, bookTitle, borrowDate, returnDate, actualReturnDate, status);
            cursor.close();
            return borrowRecord2;
        }

        cursor.close();
        return null; // If no record found
    }


    public boolean updateBorrowRecord(int recordId, ContentValues values) {
        int rows = db.update("BorrowRecords", values, "record_id = ?", new String[]{String.valueOf(recordId)});
        return rows > 0;
    }


    // Lấy userId từ tên người dùng
    public int getUserIdByName(String userName) {
        Cursor cursor = db.rawQuery("SELECT user_id FROM User WHERE name = ?", new String[]{userName});
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
            cursor.close();
            return userId;
        }
        return -1; // Nếu không tìm thấy
    }

    // Lấy bookId từ tên sách
    public int getBookIdByTitle(String bookTitle) {
        Cursor cursor = db.rawQuery("SELECT book_id FROM Books WHERE title = ?", new String[]{bookTitle});
        if (cursor != null && cursor.moveToFirst()) {
            int bookId = cursor.getInt(cursor.getColumnIndexOrThrow("book_id"));
            cursor.close();
            return bookId;
        }
        return -1; // Nếu không tìm thấy
    }



    public void close() {
        dbHelper.close();
    }
}
