package com.example.librarymanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.librarymanage.R;
import com.example.librarymanage.entities.Review;

import java.util.List;

public class RatingAdapter extends ArrayAdapter<Review> {
    private final Context context;
    private final List<Review> reviews;

    public RatingAdapter(Context context, List<Review> reviews) {
        super(context, R.layout.rating_item, reviews);
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rating_item, parent, false);
        }

        Review review = reviews.get(position);
        TextView textViewUsername = convertView.findViewById(R.id.textViewUsername);
        TextView textViewBookTitle = convertView.findViewById(R.id.textViewBookTitle);
        TextView textViewRating = convertView.findViewById(R.id.textViewRating);
        TextView textViewComment = convertView.findViewById(R.id.textViewComment);

        textViewUsername.setText(review.getUsername());
        textViewBookTitle.setText(review.getBookTitle());
        textViewRating.setText(String.valueOf(review.getRating()));
        textViewComment.setText(review.getComment());

        return convertView;
    }
}