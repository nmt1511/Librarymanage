package com.example.librarymanage;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.librarymanage.data.BookRepository;
import com.example.librarymanage.data.BorrowRecordRepository;
import com.example.librarymanage.user.feedback;

public class BookDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvAuthor, tvPublishedYear, tvDescription;
    private BookRepository bookRepository; // Khai báo lớp BookRepository
    private Button btnComment, btnBorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvPublishedYear = findViewById(R.id.tvPublishedYear);
        tvDescription = findViewById(R.id.tvDescription);
        btnComment = findViewById(R.id.btnComment);
        btnBorrow = findViewById(R.id.btnBorrow);

        // Nhận bookId từ Intent
        Intent intent = getIntent();
        int bookId = intent.getIntExtra("bookId", -1);

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent feedbackIntent = new Intent(BookDetailActivity.this, feedback.class);
                startActivity(feedbackIntent);
            }
        });

        // Khởi tạo BookRepository
        bookRepository = new BookRepository(this);

        // Hiển thị thông tin chi tiết của sách
        displayBookDetails(bookId);

        btnBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy userId từ SharedPreferences
                int userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("userId", -1);

                // Kiểm tra xem userId có hợp lệ không
                if (userId == -1) {
                    Toast.makeText(BookDetailActivity.this, "Bạn cần đăng nhập để mượn sách!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Chuyển đến BorrowActivity và truyền bookId và userId
                Intent borrowIntent = new Intent(BookDetailActivity.this, BorrowActivity.class);
                borrowIntent.putExtra("bookId", bookId);
                borrowIntent.putExtra("userId", userId);
                startActivity(borrowIntent);
            }
        });
    }

    private void displayBookDetails(int bookId) {
        Cursor cursor = bookRepository.getBookById(bookId); // Sử dụng BookRepository

        if (cursor != null && cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            int authorId = cursor.getInt(cursor.getColumnIndexOrThrow("author_id"));
            String publishedYear = cursor.getString(cursor.getColumnIndexOrThrow("published_year"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

            tvTitle.setText(title);
            tvAuthor.setText(bookRepository.getAuthorName(authorId)); // Phương thức để lấy tên tác giả
            tvPublishedYear.setText(publishedYear);
            tvDescription.setText(description);

            cursor.close(); // Đóng cursor
        } else {
            Toast.makeText(this, "Không tìm thấy sách!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        bookRepository.close(); // Đóng BookRepository khi không còn sử dụng
        super.onDestroy();
    }
}
