package com.example.librarymanage.user;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.DataBook;

public class SuaThongTin extends AppCompatActivity {

    private SQLiteDatabase db;
    private DataBook dbBook;
    private EditText etName, etPhone, etEmail, etStudentCode;
    private RadioGroup radioGroupGender;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_thong_tin);

        // Khởi tạo cơ sở dữ liệu
        dbBook = new DataBook(this);
        db = dbBook.getWritableDatabase();

        // Ánh xạ các thành phần giao diện
        etName = findViewById(R.id.etName);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etStudentCode = findViewById(R.id.etStudentCode);
        btnSave = findViewById(R.id.btnSave);

        // Lấy user_id từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);

        if (userId != -1) {
            loadUserInfo(userId);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc Activity nếu không có userId
        }

        // Xử lý sự kiện nhấn nút Lưu
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId != -1) {
                    updateUserInfo(userId);
                }
            }
        });
    }

    private void loadUserInfo(int userId) {
        String query = "SELECT name, gender, phone, email, student_code FROM User WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            etName.setText(cursor.getString(0));         // name
            String gender = cursor.getString(1);         // gender
            etPhone.setText(cursor.getString(2));        // phone
            etEmail.setText(cursor.getString(3));        // email
            etStudentCode.setText(cursor.getString(4));  // student_code

            // Đặt giới tính vào RadioGroup, chọn Nam mặc định
            if ("Nam".equals(gender)) {
                radioGroupGender.check(R.id.radioMale);
            } else {
                radioGroupGender.check(R.id.radioFemale);
            }
        } else {
            // Nếu không tìm thấy thông tin người dùng, chọn Nam làm mặc định
            radioGroupGender.check(R.id.radioMale);
        }
        cursor.close();
    }

    private void updateUserInfo(int userId) {
        String name = etName.getText().toString();
        String gender = ((RadioButton) findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();
        String phone = etPhone.getText().toString();
        String email = etEmail.getText().toString();
        String studentCode = etStudentCode.getText().toString();

        String updateQuery = "UPDATE User SET name = ?, gender = ?, phone = ?, email = ?, student_code = ? WHERE user_id = ?";
        db.execSQL(updateQuery, new Object[]{name, gender, phone, email, studentCode, userId});

        Toast.makeText(this, "Thông tin đã được cập nhật.", Toast.LENGTH_SHORT).show();

        // Trả về kết quả cho UserInfoActivity
        setResult(RESULT_OK);
        finish(); // Kết thúc Activity sau khi lưu thông tin
    }

}