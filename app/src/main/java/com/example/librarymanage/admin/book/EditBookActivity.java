package com.example.librarymanage.admin.book;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.BookRepository;

import java.util.ArrayList;
import java.util.List;

public class EditBookActivity extends AppCompatActivity {
    private EditText editTitle, editPublishedYear, editDescription;
    private Spinner spinnerAuthor, spinnerCategory, spinnerLocation;
    private Button btnUpdate;

    private BookRepository bookRepository;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        editTitle = findViewById(R.id.editTitle);
        editPublishedYear = findViewById(R.id.editPublishedYear);
        editDescription = findViewById(R.id.editDescription);
        spinnerAuthor = findViewById(R.id.spinnerAuthor);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        btnUpdate = findViewById(R.id.btnUpdate);

        bookRepository = new BookRepository(this);

        // Get the bookId from the intent
        bookId = getIntent().getIntExtra("bookId", -1);
        loadBookData();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBook();
            }
        });
    }

    private void loadBookData() {
        Cursor cursor = bookRepository.getBookById(bookId);
        if (cursor != null && cursor.moveToFirst()) {
            editTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            editPublishedYear.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("published_year"))));
            editDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow("description")));
            cursor.close();
        }

        populateSpinner(spinnerAuthor, bookRepository.getAllAuthors());
        populateSpinner(spinnerCategory, bookRepository.getAllCategories());
        populateSpinner(spinnerLocation, bookRepository.getAllLocations());
    }

    private void populateSpinner(Spinner spinner, List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void updateBook() {
        String title = editTitle.getText().toString();
        String author = spinnerAuthor.getSelectedItem().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        String location = spinnerLocation.getSelectedItem().toString();
        int publishedYear = Integer.parseInt(editPublishedYear.getText().toString());
        String description = editDescription.getText().toString();

        long result = bookRepository.updateBook(bookId, title, author, category, location, publishedYear, description);
        if (result > 0) {
            Toast.makeText(this, "Book updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error updating book", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookRepository.close();
    }
}
