package com.example.librarymanage.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.librarymanage.entities.User;

import java.util.ArrayList;

public class UserRepository {

    private DataBook dataBook;

    public UserRepository(Context context) {
        dataBook = new DataBook(context);
    }

    // Lấy tất cả người dùng
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = dataBook.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User", null);
        if (cursor.moveToFirst()) {
            do {
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String studentCode = cursor.getString(cursor.getColumnIndexOrThrow("student_code"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                int role = cursor.getInt(cursor.getColumnIndexOrThrow("role"));

                User user = new User(userId, name, gender, phone, email, studentCode, username, password, role);
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }

    // Lấy người dùng theo ID
    public User getUserById(int userId) {
        SQLiteDatabase db = dataBook.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE user_id = ?", new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String studentCode = cursor.getString(cursor.getColumnIndexOrThrow("student_code"));
            String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            int role = cursor.getInt(cursor.getColumnIndexOrThrow("role"));
            cursor.close();
            db.close();
            return new User(userId, name, gender, phone, email, studentCode, username, password, role);
        }
        cursor.close();
        db.close();
        return null;
    }

    // Thêm người dùng mới
    public boolean addUser(User user) {
        SQLiteDatabase db = dataBook.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("gender", user.getGender());
        values.put("phone", user.getPhone());
        values.put("email", user.getEmail());
        values.put("student_code", user.getStudentCode());
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("role", user.getRole());

        long result = db.insert("User", null, values);
        db.close();
        return result != -1; // Trả về true nếu thêm thành công
    }

    // Cập nhật thông tin người dùng
    public boolean updateUser(User user) {
        SQLiteDatabase db = dataBook.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("gender", user.getGender());
        values.put("phone", user.getPhone());
        values.put("email", user.getEmail());
        values.put("student_code", user.getStudentCode());
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("role", user.getRole());

        int rowsAffected = db.update("User", values, "user_id = ?", new String[]{String.valueOf(user.getUserId())});
        db.close();
        return rowsAffected > 0; // Trả về true nếu cập nhật thành công
    }

    // Xóa người dùng
    public boolean deleteUser(int userId) {
        SQLiteDatabase db = dataBook.getWritableDatabase();
        int rowsAffected = db.delete("User", "user_id = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsAffected > 0; // Trả về true nếu xóa thành công
    }
}
