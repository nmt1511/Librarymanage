package com.example.librarymanage.admin.user;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.UserRepository;
import com.example.librarymanage.entities.User;

public class AddUserActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextEmail, editTextStudentCode, editTextUsername, editTextPassword;
    private Spinner spinnerRole;
    private Button btnAddUser;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Ánh xạ các View từ layout
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextStudentCode = findViewById(R.id.editTextStudentCode);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnAddUser = findViewById(R.id.btnAddUser);

        userRepository = new UserRepository(this);

        // Cài đặt Spinner cho vai trò người dùng (Admin, User)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);


        // Xử lý sự kiện khi nhấn nút Thêm người dùng
        btnAddUser.setOnClickListener(v -> {
            // Lấy dữ liệu từ các EditText và Spinner
            String name = editTextName.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String studentCode = editTextStudentCode.getText().toString().trim();
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            int role = spinnerRole.getSelectedItemPosition(); // Vai trò (0 - User, 1 - Admin)

            // Kiểm tra các trường bắt buộc không được để trống
            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || studentCode.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(AddUserActivity.this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo đối tượng User
            User user = new User(0, name, "", phone, email, studentCode, username, password, role);

            // Thêm người dùng vào cơ sở dữ liệu
            boolean result = userRepository.addUser(user);
            if (result) {
                Toast.makeText(AddUserActivity.this, "Thêm người dùng thành công.", Toast.LENGTH_SHORT).show();
                finish(); // Đóng activity sau khi thêm thành công
            } else {
                Toast.makeText(AddUserActivity.this, "Có lỗi xảy ra, vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
