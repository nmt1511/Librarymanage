package com.example.librarymanage.admin.author;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.BookRepository;

public class EditAuthorActivity extends AppCompatActivity {

    private EditText editTextAuthorName;
    private Button buttonSave;
    private BookRepository bookRepository;
    private String oldAuthorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_author);

        // Khởi tạo BookRepository để tương tác với cơ sở dữ liệu
        bookRepository = new BookRepository(this);

        // Lấy tên tác giả cũ từ Intent
        oldAuthorName = getIntent().getStringExtra("authorName");

        // Kết nối với giao diện người dùng
        editTextAuthorName = findViewById(R.id.editTextAuthorName);
        buttonSave = findViewById(R.id.buttonSave);

        // Hiển thị tên tác giả cũ trong EditText
        editTextAuthorName.setText(oldAuthorName);

        // Lắng nghe sự kiện nhấn nút Lưu
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newAuthorName = editTextAuthorName.getText().toString();
                if (!newAuthorName.isEmpty()) {
                    // Cập nhật tên tác giả trong cơ sở dữ liệu
                    boolean success = bookRepository.updateAuthor(oldAuthorName, newAuthorName);
                    if (success) {
                        Toast.makeText(EditAuthorActivity.this, "Author updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Quay lại màn hình trước
                    } else {
                        Toast.makeText(EditAuthorActivity.this, "Failed to update author", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditAuthorActivity.this, "Please enter author name", Toast.LENGTH_SHORT).show();
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
