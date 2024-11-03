package com.example.librarymanage.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.librarymanage.entities.BorrowRecord;

import java.util.ArrayList;
import java.util.List;

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
    public List<BorrowRecord> getBorrowHistoryByUserId(int userId) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM BorrowRecords WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("record_id"));
                int bookId = cursor.getInt(cursor.getColumnIndexOrThrow("book_id"));
                String borrowDate = cursor.getString(cursor.getColumnIndexOrThrow("borrow_date"));
                String returnDate = cursor.getString(cursor.getColumnIndexOrThrow("return_date"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

                borrowRecords.add(new BorrowRecord(id, userId, bookId, borrowDate, returnDate, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return borrowRecords;
    }



    public void close() {
        dbHelper.close();
    }
}
