package com.example.librarymanage.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class BorrowRecordRepository {

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
    public void close() {
        dbHelper.close();
    }
}
