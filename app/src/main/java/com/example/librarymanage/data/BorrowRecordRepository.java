package com.example.librarymanage.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.librarymanage.entities.BorrowRecord2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public void updateOverdueRecords(int userId) {
        // Đảm bảo rằng kết nối đến cơ sở dữ liệu đang mở
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }

        // Log kiểm tra khi bắt đầu cập nhật
        Log.d("BorrowRecordRepo", "Updating overdue records for userId: " + userId);

        // Cập nhật trạng thái của các bản ghi mượn sách
        String sql = "UPDATE BorrowRecords SET status = 'Quá Hạn' " +
                "WHERE user_id = ? AND status = 'Đang Mượn' " +
                "AND return_date < date('now')";

        // Sử dụng db.execSQL() để thực thi câu lệnh SQL với tham số userId
        try {
            db.execSQL(sql, new Object[]{userId});
            Log.d("BorrowRecordRepo", "Updated overdue records for userId: " + userId);
        } catch (SQLException e) {
            Log.e("BorrowRecordRepo", "Error updating overdue records", e);
        }
    }



    public boolean deleteBorrowRecord(int recordId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("BorrowRecords", "record_id = ?", new String[]{String.valueOf(recordId)});
        db.close();
        return result > 0; // Trả về true nếu xóa thành công
    }
    public List<BorrowRecord2> getBorrowRecordsByUserId(int userId) {
        List<BorrowRecord2> borrowRecordList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Truy vấn kết hợp dữ liệu từ các bảng và lọc theo userId
        String query = "SELECT br.record_id, br.borrow_date, br.return_date, br.actual_return_date, br.status, " +
                "u.name AS user_name, b.title AS book_title " +
                "FROM BorrowRecords AS br " +
                "JOIN User AS u ON br.user_id = u.user_id " +
                "JOIN Books AS b ON br.book_id = b.book_id " +
                "WHERE br.user_id = ?"; // Thêm điều kiện WHERE để lọc theo userId

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)}); // Truyền userId vào query
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int recordId = cursor.getInt(cursor.getColumnIndexOrThrow("record_id"));
                String borrowDate = cursor.getString(cursor.getColumnIndexOrThrow("borrow_date"));
                String returnDate = cursor.getString(cursor.getColumnIndexOrThrow("return_date"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String userName = cursor.getString(cursor.getColumnIndexOrThrow("user_name"));
                String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow("book_title"));

                BorrowRecord2 record = new BorrowRecord2(recordId, userName, bookTitle, borrowDate, returnDate, status);
                borrowRecordList.add(record);
            }
            cursor.close();
        }
        db.close();
        return borrowRecordList;
    }


    // Phương thức lấy bản ghi mượn sách theo ID
    public BorrowRecord2 getBorrowRecordById(int recordId) {
        db = dbHelper.getReadableDatabase();
        BorrowRecord2 record = null;

        String query = "SELECT " +
                "br.record_id, " +
                "u.name AS user_name, " +
                "b.title AS book_title, " +
                "br.borrow_date, " +
                "br.return_date, " +
                "br.status " +
                "FROM BorrowRecords br " +
                "JOIN User u ON br.user_id = u.user_id " +
                "JOIN Books b ON br.book_id = b.book_id " +
                "WHERE br.record_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(recordId)});

        if (cursor.moveToFirst()) {
            record = new BorrowRecord2(
                    cursor.getInt(cursor.getColumnIndexOrThrow("record_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("user_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("book_title")),
                    cursor.getString(cursor.getColumnIndexOrThrow("borrow_date")),
                    cursor.getString(cursor.getColumnIndexOrThrow("return_date")),
                    cursor.getString(cursor.getColumnIndexOrThrow("status"))
            );
        }

        cursor.close();
        db.close();
        return record;
    }

    // Thêm phương thức cập nhật mới
    public boolean updateBorrowRecord(int recordId,
                                      String borrowDate,
                                      String returnDate,
                                      String status) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("borrow_date", borrowDate);
        values.put("return_date", returnDate);
        values.put("status", status);

        int rowsAffected = db.update(
                "BorrowRecords",
                values,
                "record_id = ?",
                new String[]{String.valueOf(recordId)}
        );

        db.close();
        return rowsAffected > 0;
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
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                String userName = cursor.getString(cursor.getColumnIndexOrThrow("user_name"));
                String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow("book_title"));

                BorrowRecord2 record = new BorrowRecord2(recordId, userName, bookTitle, borrowDate, returnDate, status);
                borrowRecordList.add(record);
            }
            cursor.close();
        }
        db.close();
        return borrowRecordList;
    }



    public String getNameById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM User WHERE user_id = ?", new String[]{String.valueOf(userId)});

        String userName = null;
        if (cursor != null && cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return userName;
    }


    public void close() {
        dbHelper.close();
    }
}
