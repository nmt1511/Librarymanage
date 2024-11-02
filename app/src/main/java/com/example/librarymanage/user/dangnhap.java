package com.example.librarymanage.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.MainActivity;
import com.example.librarymanage.R;

public class dangnhap extends AppCompatActivity {

    EditText etUsername, etPassword;
    CheckBox cbRememberMe;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layoutdangnhap);

        // Initialize views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnLogin = findViewById(R.id.btnLogin);

        // Set login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (username.equals("admin") && password.equals("1234")) {
                    Toast.makeText(dangnhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    // Chuyển sang MainActivity sau khi đăng ký
                    Intent intent = new Intent(dangnhap.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(dangnhap.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}