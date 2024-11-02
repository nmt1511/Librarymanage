package com.example.librarymanage.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.MainActivity;
import com.example.librarymanage.R;

public class dangki extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layoutdangki);

        // Tìm kiếm nút Register
        Button btnRegister = findViewById(R.id.btnRegister);

        // Đặt sự kiện OnClick cho nút Register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị thông báo "Đăng ký thành công"
                Toast.makeText(dangki.this, "Bạn đã đăng ký thành công", Toast.LENGTH_SHORT).show();

                // Chuyển sang MainActivity sau khi đăng ký
                Intent intent = new Intent(dangki.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Tìm kiếm TextView3
        TextView textView3 = findViewById(R.id.textView3);

        // Đặt sự kiện OnClick cho TextView3
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang trang đăng nhập (dangnhap)
                Intent intent = new Intent(dangki.this, dangnhap.class);
                startActivity(intent);
            }
        });
    }
}
