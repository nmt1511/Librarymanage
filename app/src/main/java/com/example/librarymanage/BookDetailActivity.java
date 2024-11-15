package com.example.librarymanage;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanage.adapter.ReviewAdapter;
import com.example.librarymanage.data.BookRepository;
import com.example.librarymanage.entities.Review;

import java.util.List;

public class BookDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvAuthor, tvPublishedYear, tvDescription;
    private ImageView bookImage;
    private BookRepository bookRepository;
    private Button btnFeedback, btnBorrow;
    private RecyclerView feedbackRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviews;

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
        bookImage = findViewById(R.id.bookImage);
        btnFeedback = findViewById(R.id.btnComment);
        btnBorrow = findViewById(R.id.btnBorrow);
        feedbackRecyclerView = findViewById(R.id.feedbackRecyclerView);

        // Khởi tạo BookRepository
        bookRepository = new BookRepository(this);

        // Hiển thị thông tin chi tiết của sách
        displayBookDetails(bookId);

        // Thiết lập RecyclerView cho đánh giá
        feedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviews = bookRepository.getReviewsForBook(bookId);
        reviewAdapter = new ReviewAdapter(this, reviews);
        feedbackRecyclerView.setAdapter(reviewAdapter);

        // Sự kiện khi nhấn nút "Viết đánh giá"
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("user_id", -1);
                if (userId == -1) {
                    Toast.makeText(BookDetailActivity.this, "Bạn cần đăng nhập để đánh giá sách!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Chuyển đến FeedbackActivity và truyền bookId và userId
                Intent feedbackIntent = new Intent(BookDetailActivity.this, FeedbackActivity.class);
                feedbackIntent.putExtra("bookId", bookId);
                feedbackIntent.putExtra("user_id", userId);
                startActivityForResult(feedbackIntent, 100);
            }
        });

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

    private void displayBookDetails(int bookId) {
        Cursor cursor = bookRepository.getBookById(bookId);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    int authorId = cursor.getInt(cursor.getColumnIndexOrThrow("author_id"));
                    String publishedYear = cursor.getString(cursor.getColumnIndexOrThrow("published_year"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    String imageName = cursor.getString(cursor.getColumnIndexOrThrow("image")); // Tên hình ảnh từ CSDL

                    tvTitle.setText(title);
                    tvAuthor.setText(bookRepository.getAuthorName(authorId));
                    tvPublishedYear.setText(publishedYear);
                    tvDescription.setText(description);

                    // Hiển thị hình ảnh từ drawable
                    if (imageName != null && !imageName.isEmpty()) {
                        // Loại bỏ phần đuôi file nếu có
                        String resourceName = imageName.split("\\.")[0];

                        // Lấy id của drawable
                        int drawableResourceId = getResources().getIdentifier(
                                resourceName,
                                "drawable",
                                getPackageName()
                        );

                        // Kiểm tra nếu tìm thấy drawable
                        if (drawableResourceId != 0) {
                            bookImage.setImageResource(drawableResourceId);
                        } else {
                            // Nếu không tìm thấy, đặt hình ảnh mặc định
                            bookImage.setImageResource(R.drawable.ic_open_book);
                        }
                    } else {
                        // Nếu không có tên hình ảnh, đặt hình ảnh mặc định
                        bookImage.setImageResource(R.drawable.ic_open_book);
                    }
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

    @Override
    protected void onDestroy() {
        bookRepository.close();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Cập nhật lại danh sách đánh giá nếu có thay đổi
        if (requestCode == 100 && resultCode == RESULT_OK) {
            int bookId = data.getIntExtra("bookId", -1);
            if (bookId != -1) {
                reviews.clear();
                reviews.addAll(bookRepository.getReviewsForBook(bookId));
                reviewAdapter.notifyDataSetChanged();
            }
        }
    }
}