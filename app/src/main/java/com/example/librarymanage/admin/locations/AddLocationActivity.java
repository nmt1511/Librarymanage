package com.example.librarymanage.admin.locations;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.BookRepository;

public class AddLocationActivity extends AppCompatActivity {

    private BookRepository bookRepository;
    private EditText editTextLocationName;
    private Button buttonSaveLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        // Khởi tạo đối tượng BookRepository để tương tác với cơ sở dữ liệu
        bookRepository = new BookRepository(this);

        // Kết nối với giao diện người dùng
        editTextLocationName = findViewById(R.id.editTextLocationName);
        buttonSaveLocation = findViewById(R.id.buttonSaveLocation);

        // Sự kiện khi người dùng nhấn nút Lưu
        buttonSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy tên địa điểm nhập từ EditText
                String locationName = editTextLocationName.getText().toString().trim();

                // Kiểm tra nếu tên địa điểm không trống
                if (locationName.isEmpty()) {
                    showMessage("Location name cannot be empty");
                    return;
                }

                // Lưu địa điểm vào cơ sở dữ liệu
                long result = bookRepository.addLocation(locationName);
                if (result > 0) {
                    showMessage("Location added successfully");
                    finish(); // Đóng activity và quay lại màn hình trước
                } else {
                    showMessage("Failed to add location");
                }
            }
        });
    }

    // Phương thức hiển thị thông báo
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đóng kết nối cơ sở dữ liệu khi Activity bị hủy
        bookRepository.close();
    }
}
