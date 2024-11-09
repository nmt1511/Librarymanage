package com.example.librarymanage;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.data.BookRepository;

public class BookDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvAuthor, tvPublishedYear, tvDescription;
    private BookRepository bookRepository;
    private Button btnComment, btnBorrow;
    private EditText etUserComment;
    private LinearLayout layoutReviews;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Lấy bookId từ Intent
        Intent intent = getIntent();
        int bookId = intent.getIntExtra("bookId", -1);

        if (bookId == -1) {
            Toast.makeText(this, "Không tìm thấy sách!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Khởi tạo các view
        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvPublishedYear = findViewById(R.id.tvPublishedYear);
        tvDescription = findViewById(R.id.tvDescription);
        btnComment = findViewById(R.id.btnComment);
        btnBorrow = findViewById(R.id.btnBorrow);
        etUserComment = findViewById(R.id.etUserComment);
        layoutReviews = findViewById(R.id.layoutReviews);
        ratingBar = findViewById(R.id.ratingBar);

        // Khởi tạo BookRepository
        bookRepository = new BookRepository(this);

        // Hiển thị thông tin chi tiết của sách
        displayBookDetails(bookId);

        // Cập nhật danh sách đánh giá
        updateReviewsList(bookId);

//        // Sự kiện khi nhấn nút "Viết đánh giá"
//        btnComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String userComment = etUserComment.getText().toString().trim();
//                if (!userComment.isEmpty()) {
//                    // Lấy userId từ SharedPreferences
//                    int userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("user_id", -1);
//                    if (userId == -1) {
//                        Toast.makeText(BookDetailActivity.this, "Bạn cần đăng nhập để viết đánh giá!", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    // Lấy rating từ RatingBar
//                    int rating = (int) ratingBar.getRating();
//
//                    // Lưu đánh giá vào cơ sở dữ liệu
//                  //  long reviewId = bookRepository.addReview(bookId, userId, rating, userComment);
//
//                    if (reviewId != -1) {
//                        Toast.makeText(BookDetailActivity.this, "Đã thêm đánh giá!", Toast.LENGTH_SHORT).show();
//                        etUserComment.setText("");
//                        ratingBar.setRating(0); // Đặt lại RatingBar
//
//                        // Cập nhật danh sách đánh giá
//                        updateReviewsList(bookId);
//                    } else {
//                        Toast.makeText(BookDetailActivity.this, "Lỗi khi thêm đánh giá!", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(BookDetailActivity.this, "Vui lòng nhập đánh giá!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        // Sự kiện khi nhấn nút "Mượn sách"
        btnBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("user_id", -1);
                if (userId == -1) {
                    Toast.makeText(BookDetailActivity.this, "Bạn cần đăng nhập để mượn sách!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Chuyển đến BorrowActivity và truyền bookId và userId
                Intent borrowIntent = new Intent(BookDetailActivity.this, BorrowActivity.class);
                borrowIntent.putExtra("bookId", bookId);
                borrowIntent.putExtra("user_id", userId);
                startActivity(borrowIntent);
            }
        });
    }

    // Hiển thị thông tin chi tiết sách
    private void displayBookDetails(int bookId) {
        Cursor cursor = bookRepository.getBookById(bookId);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    int authorId = cursor.getInt(cursor.getColumnIndexOrThrow("author_id"));
                    String publishedYear = cursor.getString(cursor.getColumnIndexOrThrow("published_year"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                    tvTitle.setText(title);
                    tvAuthor.setText(bookRepository.getAuthorName(authorId));
                    tvPublishedYear.setText(publishedYear);
                    tvDescription.setText(description);
                } else {
                    Toast.makeText(this, "Không tìm thấy sách!", Toast.LENGTH_SHORT).show();
                }
            } finally {
                cursor.close();
            }
        } else {
            Toast.makeText(this, "Không tìm thấy sách!", Toast.LENGTH_SHORT).show();
        }
    }

    // Cập nhật danh sách đánh giá
    private void updateReviewsList(int bookId) {
        layoutReviews.removeAllViews();

        Cursor cursor = bookRepository.getReviewsForBook(bookId);
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    String comment = cursor.getString(cursor.getColumnIndexOrThrow("comment"));
                    int rating = cursor.getInt(cursor.getColumnIndexOrThrow("rating"));

                    // Tạo TextView để hiển thị đánh giá
                    TextView reviewTextView = new TextView(this);
                    reviewTextView.setText("Đánh giá: " + comment + "\nĐiểm: " + rating);
                    reviewTextView.setTextSize(16);
                    reviewTextView.setTextColor(getResources().getColor(R.color.text_secondary));

                    layoutReviews.addView(reviewTextView);
                }
            } finally {
                cursor.close();
            }
        }
    }

    @Override
    protected void onDestroy() {
        bookRepository.close();
        super.onDestroy();
    }
}
