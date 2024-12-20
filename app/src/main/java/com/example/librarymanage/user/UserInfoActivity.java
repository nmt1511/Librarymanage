package com.example.librarymanage.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.DataBook;

public class UserInfoActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private DataBook dbBook; // Giả sử bạn có một lớp DataBook để quản lý truy cập cơ sở dữ liệu
    private TextView tvName, tvGender, tvPhone, tvEmail, tvStudentCode;
    private Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // Khởi tạo cơ sở dữ liệu
        dbBook = new DataBook(this);
        db = dbBook.getReadableDatabase();

        // Ánh xạ các thành phần giao diện
        tvName = findViewById(R.id.tvName);
        tvGender = findViewById(R.id.tvGender);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvStudentCode = findViewById(R.id.tvStudentCode);
        btnEdit = findViewById(R.id.btnEdit);

        // Lấy user_id từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);
        Log.d("UserInfoActivity", "user_id: " + userId);

        if (userId != -1) {
            displayUserInfo(userId);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc Activity nếu không có userId
        }

        // Xử lý sự kiện nhấn nút Sửa Thông Tin
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, SuaThongTin.class);
                startActivityForResult(intent, 1); // Khởi động SuaThongTinActivity
            }
        });
    }

    // Xử lý kết quả trả về từ SuaThongTinActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Lấy user_id từ SharedPreferences
            SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            int userId = preferences.getInt("user_id", -1);
            if (userId != -1) {
                displayUserInfo(userId); // Cập nhật lại thông tin người dùng
            }
        }
    }
    private void displayUserInfo(int userId) {
        // Câu truy vấn lấy thông tin người dùng từ bảng User
        String query = "SELECT name, gender, phone, email, student_code FROM User WHERE user_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);         // name
            String gender = cursor.getString(1);       // gender
            String phone = cursor.getString(2);        // phone
            String email = cursor.getString(3);        // email
            String studentCode = cursor.getString(4);  // student_code

            // Set dữ liệu lên các TextView
            tvName.setText(name);
            tvGender.setText(gender);
            tvPhone.setText(phone);
            tvEmail.setText(email);
            tvStudentCode.setText(studentCode);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

}