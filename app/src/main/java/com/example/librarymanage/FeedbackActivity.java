package com.example.librarymanage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.data.BookRepository;
import com.example.librarymanage.entities.Review;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FeedbackActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText commentEditText;
    private Button submitReviewButton;
    private BookRepository bookRepository;
    private int bookId;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Khởi tạo các view
        ratingBar = findViewById(R.id.ratingBar);
        commentEditText = findViewById(R.id.CommentbackText);
        submitReviewButton = findViewById(R.id.submitReviewbackButton);

        // Lấy bookId và userId từ Intent
        Intent intent = getIntent();
        bookId = intent.getIntExtra("bookId", -1);
        userId = intent.getIntExtra("user_id", -1);

        // Khởi tạo BookRepository
        bookRepository = new BookRepository(this);

        // Sự kiện khi nhấn nút "Gửi đánh giá"
        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });
    }

    private void submitReview() {
        // Kiểm tra xem người dùng đã mượn sách chưa
        if (!bookRepository.hasBorrowedBook(userId, bookId)) {
            Toast.makeText(this, "Vui lòng mượn sách để đánh giá!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra xem người dùng đã đánh giá sách chưa
        if (bookRepository.hasReviewedBook(userId, bookId)) {
            Toast.makeText(this, "Bạn đã đánh giá sách này rồi!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy thông tin từ các view
        String comment = commentEditText.getText().toString().trim();
        float rating = ratingBar.getRating();

        if (rating == 0) {
            Toast.makeText(this, "Vui lòng chọn đánh giá!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (comment.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập góp ý của bạn!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy ngày hiện tại
        String reviewDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Tạo đối tượng Review
        Review review = new Review(bookId, userId, "", "", comment, (int) rating, reviewDate);

        // Lưu đánh giá vào cơ sở dữ liệu
        bookRepository.addReview(review);

        // Trả kết quả về BookDetailActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("bookId", bookId);
        setResult(RESULT_OK, resultIntent);
        finish(); // Đóng FeedbackActivity
    }

    @Override
    protected void onDestroy() {
        bookRepository.close();
        super.onDestroy();
    }
}