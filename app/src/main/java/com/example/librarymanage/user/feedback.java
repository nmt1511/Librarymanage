package com.example.librarymanage.user;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.librarymanage.R;
import com.example.librarymanage.data.DataBook;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class feedback extends Activity {

    private RatingBar ratingBar;
    private EditText commentEditText;
    private Button submitButton;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Kết nối các thành phần giao diện
        ratingBar = findViewById(R.id.ratingBar);
        commentEditText = findViewById(R.id.CommentbackText);
        submitButton = findViewById(R.id.submitReviewbackButton);

        // Mở cơ sở dữ liệu (hoặc khởi tạo nó nếu chưa có)
        DataBook dbHelper = new DataBook(this);
        db = dbHelper.getWritableDatabase();

        // Sự kiện khi bấm nút Gửi đánh giá
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });
    }

    private void submitFeedback() {
        // Lấy giá trị từ RatingBar và EditText
        int rating = (int) ratingBar.getRating();
        String comment = commentEditText.getText().toString().trim();

        // Kiểm tra điều kiện nhập dữ liệu
        if (rating == 0) {
            Toast.makeText(this, "Vui lòng chọn điểm đánh giá!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (comment.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nhận xét của bạn!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Định dạng ngày hiện tại cho `review_date`
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Chèn dữ liệu vào bảng Reviews
        String insertQuery = "INSERT INTO Reviews (book_id, user_id, rating, comment, review_date) " +
                "VALUES (?, ?, ?, ?, ?)";
        db.execSQL(insertQuery, new Object[]{1, 1, rating, comment, currentDate}); // Thay `1, 1` bằng `book_id` và `user_id` thực tế nếu có

        Toast.makeText(this, "Đánh giá của bạn đã được gửi!", Toast.LENGTH_SHORT).show();

        // Xóa dữ liệu sau khi gửi thành công
        ratingBar.setRating(0);
        commentEditText.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đóng cơ sở dữ liệu khi không còn sử dụng
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
