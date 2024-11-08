package com.example.librarymanage.admin.reviews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.ReviewRepository;
import com.example.librarymanage.entities.Review;

import java.util.ArrayList;
import java.util.List;

public class QLReviewActivity extends AppCompatActivity {

    private ReviewRepository reviewRepository;
    private ListView listViewReviews;
    private Button buttonAddReview;
    private ArrayAdapter<String> reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_review);

        // Initialize the ReviewRepository to interact with the database
        reviewRepository = new ReviewRepository(this);

        // Bind UI components
        listViewReviews = findViewById(R.id.listViewReviews);
        buttonAddReview = findViewById(R.id.buttonAddReview);

        // Load and display the reviews
        loadReviews();

        // Handle long-clicks to show options to edit or delete
        listViewReviews.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parentView, View view, int position, long id) {
                // Get the selected Review object
                Review selectedReview = (Review) parentView.getItemAtPosition(position);

                // Show options to edit or delete the review
                showReviewOptions(selectedReview.getReviewId());

                return true; // Indicate that the long-click event was handled
            }
        });

        // Add a new review when the button is clicked
        buttonAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to the activity to add a new review
                Intent intent = new Intent(QLReviewActivity.this, AddReviewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadReviews() {
        // Get all reviews from the repository
        List<Review> reviews = reviewRepository.getAllReviews();
        reviewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, convertReviewListToTextList(reviews));
        listViewReviews.setAdapter(reviewAdapter);
    }

    // Convert the list of reviews to a list of review texts for display
    private List<String> convertReviewListToTextList(List<Review> reviews) {
        List<String> reviewTexts = new ArrayList<>();
        for (Review review : reviews) {
            reviewTexts.add(review.getReviewText());
        }
        return reviewTexts;
    }

    private void showReviewOptions(int reviewId) {
        // Show options to edit or delete the review
        Toast.makeText(this, "Long clicked on review with ID: " + reviewId, Toast.LENGTH_SHORT).show();

        // Add code here to launch either the EditReviewActivity or DeleteReviewActivity
        // For example, we'll just launch EditReviewActivity
        Intent intent = new Intent(QLReviewActivity.this, EditReviewActivity.class);
        intent.putExtra("reviewId", reviewId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload the reviews when the activity is resumed (e.g., after adding/editing/deleting a review)
        loadReviews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection when the activity is destroyed
        reviewRepository.close();
    }
}
