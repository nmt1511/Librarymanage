package com.example.librarymanage.user;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.DataBook;

public class dangki extends AppCompatActivity {

    private DataBook dbBook;
    private EditText edtUsername, edtPassword, edtName, edtEmail, edtConfirmPassword, edtPhoneNumber, edtStudentID;
    private RadioGroup rgGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layoutdangki);

        // Initialize database and link input fields
        dbBook = new DataBook(this);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtName = findViewById(R.id.etFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtConfirmPassword = findViewById(R.id.etConfirmPassword);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtStudentID = findViewById(R.id.edtStudentID);
        rgGender = findViewById(R.id.rgGender);  // RadioGroup for gender

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

        // Check which gender is selected
        String gender;
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        if (selectedGenderId == R.id.rbMale) {
            gender = "Nam";
        } else if (selectedGenderId == R.id.rbFemale) {
            gender = "Nữ";
        } else {
            Toast.makeText(this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validation
        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || studentID.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbBook.getWritableDatabase();

        // Check if username already exists
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE username=?", new String[]{username});
        if (cursor.getCount() > 0) {
            Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }
        cursor.close();

        // Insert user data into User table
        ContentValues userValues = new ContentValues();
        userValues.put("name", name);
        userValues.put("gender", gender);  // Save gender information
        userValues.put("phone", phone);
        userValues.put("email", email);
        userValues.put("student_code", studentID);
        userValues.put("username", username);
        userValues.put("password", password);
        userValues.put("role", "reader");

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
