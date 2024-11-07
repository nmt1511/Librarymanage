package com.example.librarymanage.user;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.DataBook;

public class dangki extends AppCompatActivity {

    private DataBook dbBook;
    private EditText edtUsername, edtPassword, edtName, edtEmail, edtConfirmPassword, edtPhoneNumber, edtStudentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layoutdangki);

        // Khởi tạo CSDL và ánh xạ các trường dữ liệu
        dbBook = new DataBook(this);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtName = findViewById(R.id.etFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtConfirmPassword = findViewById(R.id.etConfirmPassword);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtStudentID = findViewById(R.id.edtStudentID);

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhoneNumber.getText().toString().trim();
        String studentID = edtStudentID.getText().toString().trim();

        // Kiểm tra điều kiện nhập liệu
        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || studentID.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbBook.getWritableDatabase();

        // Kiểm tra tên đăng nhập đã tồn tại chưa
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE username=?", new String[]{username});
        if (cursor.getCount() > 0) {
            Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }
        cursor.close();

        // Lưu thông tin người dùng vào bảng User
        ContentValues userValues = new ContentValues();
        userValues.put("name", name);
        userValues.put("phone", phone);
        userValues.put("email", email);
        userValues.put("student_code", studentID);
        userValues.put("username", username);
        userValues.put("password", password);
        userValues.put("role", "reader"); // đặt quyền người dùng mặc định

        long userId = db.insert("User", null, userValues);

        if (userId != -1) {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(dangki.this, dangnhap.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}
