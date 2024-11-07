package com.example.librarymanage.admin.user;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.UserRepository;
import com.example.librarymanage.entities.User;

public class EditUserActivity extends AppCompatActivity {

    private EditText etName, etGender, etPhone, etEmail, etStudentCode, etUsername, etPassword;
    private Button btnSave;
    private UserRepository userRepository;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // Khởi tạo các view
        etName = findViewById(R.id.etName);
        etGender = findViewById(R.id.etGender);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etStudentCode = findViewById(R.id.etStudentCode);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSave = findViewById(R.id.btnSave);

        // Khởi tạo repository
        userRepository = new UserRepository(this);

        // Lấy dữ liệu người dùng từ Intent
        int userId = getIntent().getIntExtra("userId", -1);
        user = userRepository.getUserById(userId);

        // Nếu người dùng hợp lệ, hiển thị thông tin lên các EditText
        if (user != null) {
            etName.setText(user.getName());
            etGender.setText(user.getGender());
            etPhone.setText(user.getPhone());
            etEmail.setText(user.getEmail());
            etStudentCode.setText(user.getStudentCode());
            etUsername.setText(user.getUsername());
            etPassword.setText(user.getPassword());
        }

        // Lưu thông tin khi nhấn nút Lưu
        btnSave.setOnClickListener(v -> saveUserInfo());
    }

    private void saveUserInfo() {
        // Lấy thông tin từ các EditText
        String name = etName.getText().toString().trim();
        String gender = etGender.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String studentCode = etStudentCode.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Kiểm tra thông tin hợp lệ
        if (name.isEmpty() || gender.isEmpty() || phone.isEmpty() || email.isEmpty() || studentCode.isEmpty()
                || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật thông tin người dùng
        user.setName(name);
        user.setGender(gender);
        user.setPhone(phone);
        user.setEmail(email);
        user.setStudentCode(studentCode);
        user.setUsername(username);
        user.setPassword(password);

        boolean isUpdated = userRepository.updateUser(user);

        if (isUpdated) {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            finish(); // Quay lại màn hình trước
        } else {
            Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
