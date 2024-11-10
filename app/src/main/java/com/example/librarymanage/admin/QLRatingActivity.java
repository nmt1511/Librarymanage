package com.example.librarymanage.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.adapter.RatingAdapter;
import com.example.librarymanage.data.BookRepository;
import com.example.librarymanage.entities.Review;

import java.util.List;

public class QLRatingActivity extends AppCompatActivity {

    private EditText editTextRatingValue;
    private Button buttonDelete;
    private ListView listViewRatings;
    private BookRepository bookRepository;
    private List<Review> ratingList; // Thay đổi kiểu dữ liệu
    private Review selectedReview; // Lưu đối tượng Review đã chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_rating);

        bookRepository = new BookRepository(this);
        editTextRatingValue = findViewById(R.id.editTextRatingValue);
        buttonDelete = findViewById(R.id.buttonDelete);
        listViewRatings = findViewById(R.id.listViewRatings);

        loadRatings(); // Tải danh sách đánh giá

        buttonDelete.setOnClickListener(v -> {
            if (selectedReview != null) {
                boolean deleted = bookRepository.deleteRating(selectedReview.getRating()); // Xóa bằng rating
                if (deleted) {
                    Toast.makeText(QLRatingActivity.this, "Rating deleted successfully", Toast.LENGTH_SHORT).show();
                    loadRatings(); // Tải lại danh sách sau khi xóa
                    editTextRatingValue.setText(""); // Xóa giá trị trong EditText
                } else {
                    Toast.makeText(QLRatingActivity.this, "Failed to delete rating", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(QLRatingActivity.this, "Select a rating to delete", Toast.LENGTH_SHORT).show();
            }
        });

        listViewRatings.setOnItemClickListener((parent, view, position, id) -> {
            selectedReview = ratingList.get(position); // Lưu đối tượng Review đã chọn
            editTextRatingValue.setText(String.valueOf(selectedReview.getRating())); // Hiển thị giá trị đã chọn
        });
    }

    private void loadRatings() {
        ratingList = bookRepository.getAllReviews(); // Lấy danh sách đối tượng Review
        RatingAdapter adapter = new RatingAdapter(this, ratingList); // Cập nhật adapter để nhận đối tượng Review
        listViewRatings.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookRepository.close();
    }
}