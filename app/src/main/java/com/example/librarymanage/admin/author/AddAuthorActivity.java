package com.example.librarymanage.admin.author;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.BookRepository;

public class AddAuthorActivity extends AppCompatActivity {

    private EditText editTextAuthorName;
    private Button buttonSave;
    private BookRepository bookRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_author);

        // Khởi tạo BookRepository để tương tác với cơ sở dữ liệu
        bookRepository = new BookRepository(this);

        // Kết nối với giao diện người dùng
        editTextAuthorName = findViewById(R.id.editTextAuthorName);
        buttonSave = findViewById(R.id.buttonSave);

        // Lắng nghe sự kiện nhấn nút Lưu
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String authorName = editTextAuthorName.getText().toString();
                if (!authorName.isEmpty()) {
                    // Thêm tác giả vào cơ sở dữ liệu
                    long authorId = bookRepository.addAuthor(authorName);
                    if (authorId != -1) {
                        Toast.makeText(AddAuthorActivity.this, "Author added successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Quay lại màn hình trước
                    } else {
                        Toast.makeText(AddAuthorActivity.this, "Failed to add author", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddAuthorActivity.this, "Please enter author name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đảm bảo đóng kết nối với cơ sở dữ liệu khi Activity bị hủy
        bookRepository.close();
    }
}
