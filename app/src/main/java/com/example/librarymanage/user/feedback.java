package com.example.librarymanage.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanage.R;

import java.util.ArrayList;

public class feedback extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText feedbackText;
    private Button submitFeedbackButton;
    private RecyclerView feedbackRecyclerView;
    private FeedbackAdapter feedbackAdapter;
    private ArrayList<FeedbackItem> feedbackList;
    private TextView ratingMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Khởi tạo các View
        ratingBar = findViewById(R.id.ratingBar);
        feedbackText = findViewById(R.id.feedbackText);
        submitFeedbackButton = findViewById(R.id.submitFeedbackButton);
        ratingMessage = findViewById(R.id.ratingMessage);

        // Khởi tạo danh sách và adapter
        feedbackList = new ArrayList<>();
        feedbackAdapter = new FeedbackAdapter(feedbackList);
        feedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedbackRecyclerView.setAdapter(feedbackAdapter);

        // Thiết lập sự kiện thay đổi sao
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String message;
                if (rating == 1) {
                    message = "Dở, tệ";
                } else if (rating == 2) {
                    message = "Trung bình";
                } else if (rating == 3) {
                    message = "Ổn";
                } else if (rating == 4) {
                    message = "Hay";
                } else {
                    message = "Xuất sắc";
                }
                ratingMessage.setText(message);
            }
        });

        // Thiết lập sự kiện cho nút gửi đánh giá
        submitFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });
    }

    private void submitFeedback() {
        String comment = feedbackText.getText().toString().trim();
        float rating = ratingBar.getRating();

        if (comment.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập góp ý của bạn", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm phản hồi mới vào danh sách
        FeedbackItem newFeedback = new FeedbackItem("Đánh giá", rating, comment);
        feedbackList.add(newFeedback);
        feedbackAdapter.notifyItemInserted(feedbackList.size() - 1);

        // Xóa nội dung đã nhập
        feedbackText.setText("");
        ratingBar.setRating(0);
        ratingMessage.setText("");
        Toast.makeText(this, "Cảm ơn bạn đã gửi góp ý!", Toast.LENGTH_SHORT).show();
    }

    private static class FeedbackItem {
        private String category;
        private float rating;
        private String comment;

        public FeedbackItem(String category, float rating, String comment) {
            this.category = category;
            this.rating = rating;
            this.comment = comment;
        }

        public String getCategory() {
            return category;
        }

        public float getRating() {
            return rating;
        }

        public String getComment() {
            return comment;
        }
    }

    private class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {
        private ArrayList<FeedbackItem> feedbackList;

        public FeedbackAdapter(ArrayList<FeedbackItem> feedbackList) {
            this.feedbackList = feedbackList;
        }

        @NonNull
        @Override
        public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, parent, false);
            return new FeedbackViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
            FeedbackItem feedbackItem = feedbackList.get(position);
            holder.ratingTextView.setText("Chất lượng: " + feedbackItem.getRating());
            holder.commentTextView.setText("Bình luận: " + feedbackItem.getComment());
        }

        @Override
        public int getItemCount() {
            return feedbackList.size();
        }

        class FeedbackViewHolder extends RecyclerView.ViewHolder {
            TextView ratingTextView;
            TextView commentTextView;

            public FeedbackViewHolder(@NonNull View itemView) {
                super(itemView);
                ratingTextView = itemView.findViewById(R.id.tv_rating);
                commentTextView = itemView.findViewById(R.id.tv_comment);
            }
        }
    }
}
