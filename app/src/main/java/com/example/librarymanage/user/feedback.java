package com.example.librarymanage.user;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanage.R;
import com.example.librarymanage.data.DataBook;

import java.util.ArrayList;

public class feedback extends AppCompatActivity {

//    private RatingBar ratingBar;
//    private EditText feedbackText;
//    private Button submitFeedbackButton;
//    private RecyclerView feedbackRecyclerView;
//    private FeedbackAdapter feedbackAdapter;
//    private ArrayList<FeedbackItem> feedbackList;
//    private TextView ratingMessage;
//    private DataBook dbBook;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_feedback);
//
//        // Khởi tạo các View
//        dbBook = new DataBook(this);
//        ratingBar = findViewById(R.id.ratingBar);
//        feedbackText = findViewById(R.id.feedbackText);
//        submitFeedbackButton = findViewById(R.id.submitFeedbackButton);
//        ratingMessage = findViewById(R.id.ratingMessage);
//        feedbackRecyclerView = findViewById(R.id.feedbackRecyclerView);
//
//        // Khởi tạo danh sách và adapter
//        feedbackList = new ArrayList<>();
//        feedbackAdapter = new FeedbackAdapter(feedbackList);
//        feedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        feedbackRecyclerView.setAdapter(feedbackAdapter);
//
//        // Thiết lập sự kiện thay đổi sao
//        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
//            String message;
//            switch ((int) rating) {
//                case 1:
//                    message = "Dở, tệ";
//                    break;
//                case 2:
//                    message = "Trung bình";
//                    break;
//                case 3:
//                    message = "Ổn";
//                    break;
//                case 4:
//                    message = "Hay";
//                    break;
//                default:
//                    message = "Xuất sắc";
//                    break;
//            }
//            ratingMessage.setText(message);
//        });
//
//        // Thiết lập sự kiện cho nút gửi đánh giá
//        submitFeedbackButton.setOnClickListener(v -> submitFeedback());
//    }
//
//    private void submitFeedback() {
//        String comment = feedbackText.getText().toString().trim();
//        int rating = (int) ratingBar.getRating();
//
//        if (comment.isEmpty()) {
//            Toast.makeText(this, "Vui lòng nhập góp ý của bạn", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Thêm phản hồi mới vào cơ sở dữ liệu
//        SQLiteDatabase db = dbBook.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("book_id", 1);  // Thay thế bằng ID sách thực tế
//        values.put("user_id", 1);  // Thay thế bằng ID người dùng thực tế
//        values.put("rating", rating);
//        values.put("comment", comment);
//        values.put("review_date", System.currentTimeMillis());
//
//        long result = db.insert("Reviews", null, values);
//        db.close();
//
//        if (result != -1) {
//            // Thêm phản hồi mới vào danh sách hiển thị
//            FeedbackItem newFeedback = new FeedbackItem("Đánh giá", rating, comment);
//            feedbackList.add(newFeedback);
//            feedbackAdapter.notifyItemInserted(feedbackList.size() - 1);
//
//            // Xóa nội dung đã nhập
//            feedbackText.setText("");
//            ratingBar.setRating(0);
//            ratingMessage.setText("");
//            Toast.makeText(this, "Cảm ơn bạn đã gửi góp ý!", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Gửi góp ý thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private static class FeedbackItem {
//        private final String category;
//        private final float rating;
//        private final String comment;
//
//        public FeedbackItem(String category, float rating, String comment) {
//            this.category = category;
//            this.rating = rating;
//            this.comment = comment;
//        }
//
//        public String getCategory() {
//            return category;
//        }
//
//        public float getRating() {
//            return rating;
//        }
//
//        public String getComment() {
//            return comment;
//        }
//    }
//
//    private class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {
//        private final ArrayList<FeedbackItem> feedbackList;
//
//        public FeedbackAdapter(ArrayList<FeedbackItem> feedbackList) {
//            this.feedbackList = feedbackList;
//        }
//
//        @NonNull
//        @Override
//        public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, parent, false);
//            return new FeedbackViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
//            FeedbackItem feedbackItem = feedbackList.get(position);
//            holder.ratingTextView.setText("Chất lượng: " + feedbackItem.getRating());
//            holder.commentTextView.setText("Bình luận: " + feedbackItem.getComment());
//        }
//
//        @Override
//        public int getItemCount() {
//            return feedbackList.size();
//        }
//
//        class FeedbackViewHolder extends RecyclerView.ViewHolder {
//            TextView ratingTextView;
//            TextView commentTextView;
//
//            public FeedbackViewHolder(@NonNull View itemView) {
//                super(itemView);
//                ratingTextView = itemView.findViewById(R.id.tv_rating);
//                commentTextView = itemView.findViewById(R.id.tv_comment);
//            }
//        }
//    }
}
