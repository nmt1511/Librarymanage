package com.example.librarymanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanage.entities.Review;
import com.example.librarymanage.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewList;
    private Context context;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.tvName.setText(review.getName());

        // Đặt RatingBar ở chế độ chỉ hiển thị
        holder.ratingBar.setRating(review.getRating());
        holder.ratingBar.setIsIndicator(true);

        holder.tvFeedbackText.setText(review.getComment());
        holder.tvFeedbackDate.setText(formatDate(review.getReviewDate())); // Giả sử bạn đã có thuộc tính reviewDate trong Review
    }


    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvFeedbackText, tvFeedbackDate;
        RatingBar ratingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.Name_user);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvFeedbackText = itemView.findViewById(R.id.feedbackText);
            tvFeedbackDate = itemView.findViewById(R.id.feedbackDate);
        }
    }

    private String formatDate(String date) {
        // Implement logic to format the date as per your requirements
        return date;
    }
}