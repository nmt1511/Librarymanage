package com.example.librarymanage.user;

import android.content.ContentValues;
import android.content.Intent;
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
    private EditText edtUsername, edtPassword, edtName, edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layoutdangki);

        // Khởi tạo CSDL và ánh xạ các trường dữ liệu
        dbBook = new DataBook(this);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtName = findViewById(R.id.etFullName); // Bổ sung ánh xạ cho edtName
        edtEmail = findViewById(R.id.edtEmail);

        // Nút đăng ký
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();


        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty() ) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbBook.getWritableDatabase();

        // Lưu thông tin người dùng vào bảng User
        ContentValues userValues = new ContentValues();
        userValues.put("name", name);
        userValues.put("email", email);

        long userId = db.insert("User", null, userValues);

        if (userId != -1) {
            // Lưu thông tin tài khoản vào bảng Account
            ContentValues accountValues = new ContentValues();
            accountValues.put("username", username);
            accountValues.put("password", password);
            accountValues.put("role", "reader");  // role mặc định là "reader"

            db.insert("Account", null, accountValues);

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
