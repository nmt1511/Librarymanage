package com.example.librarymanage.admin.locations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.BookRepository;

public class EditLocationActivity extends AppCompatActivity {

    private EditText editTextLocationName;
    private Button buttonSaveLocation;
    private BookRepository bookRepository;
    private int locationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);

        // Khởi tạo BookRepository để tương tác với cơ sở dữ liệu
        bookRepository = new BookRepository(this);

        // Kết nối với giao diện người dùng
        editTextLocationName = findViewById(R.id.editTextLocationName);
        buttonSaveLocation = findViewById(R.id.buttonSaveLocation);

        // Nhận thông tin địa điểm từ Intent
        Intent intent = getIntent();
        locationId = intent.getIntExtra("locationId", -1);
        String locationName = intent.getStringExtra("locationName");

        // Hiển thị tên địa điểm hiện tại
        editTextLocationName.setText(locationName);

        // Sự kiện click nút lưu để cập nhật thông tin địa điểm
        buttonSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newLocationName = editTextLocationName.getText().toString().trim();

                if (newLocationName.isEmpty()) {
                    Toast.makeText(EditLocationActivity.this, "Please enter a location name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cập nhật tên địa điểm trong cơ sở dữ liệu
                long result = bookRepository.updateLocation(locationId, newLocationName);
                if (result > 0) {
                    Toast.makeText(EditLocationActivity.this, "Location updated successfully", Toast.LENGTH_SHORT).show();
                    finish();  // Kết thúc Activity và quay lại màn hình trước
                } else {
                    Toast.makeText(EditLocationActivity.this, "Failed to update location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đóng kết nối cơ sở dữ liệu khi Activity bị hủy
        bookRepository.close();
    }
}
