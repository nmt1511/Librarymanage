package com.example.librarymanage.admin.book;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.BookRepository;

public class AddBookActivity extends AppCompatActivity {

    private EditText edtTitle, edtYear, edtDescription, edtImage;
    private Spinner spinnerAuthor, spinnerCategory, spinnerLocation;
    private Button btnAddBook;
    private BookRepository bookRepository;
    private ArrayAdapter<String> authorAdapter, categoryAdapter, locationAdapter;
    private int selectedAuthorId, selectedCategoryId, selectedLocationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // Initialize the views
        edtTitle = findViewById(R.id.edtTitle);
        edtYear = findViewById(R.id.edtYear);
        edtDescription = findViewById(R.id.edtDescription);
        edtImage = findViewById(R.id.edtImage);
        spinnerAuthor = findViewById(R.id.spinnerAuthor);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        btnAddBook = findViewById(R.id.btnAddBook);

        // Initialize the BookRepository
        bookRepository = new BookRepository(this);

        // Load data into spinners
        loadSpinnerData();

        // Set up spinner item selected listeners
        spinnerAuthor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedAuthorId = position + 1; // assuming the ID starts from 1
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedAuthorId = -1;
            }
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategoryId = position + 1; // assuming the ID starts from 1
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedCategoryId = -1;
            }
        });

        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedLocationId = position + 1; // assuming the ID starts from 1
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedLocationId = -1;
            }
        });

        // Button click listener to add book
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values
                String title = edtTitle.getText().toString().trim();
                String yearStr = edtYear.getText().toString().trim();
                String description = edtDescription.getText().toString().trim();
                String image = edtImage.getText().toString().trim();

                // Check for empty fields
                if (title.isEmpty() || yearStr.isEmpty() || description.isEmpty() || selectedAuthorId == -1 || selectedCategoryId == -1 || selectedLocationId == -1) {
                    Toast.makeText(AddBookActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Convert year to integer
                int publishedYear;
                try {
                    publishedYear = Integer.parseInt(yearStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(AddBookActivity.this, "vui lòng nhập năm", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add the book to the database
                long newBookId = bookRepository.addBook(title, selectedAuthorId, selectedCategoryId, selectedLocationId, publishedYear, description, image, "2024-11-08");

                // Check if the book was added successfully
                if (newBookId > 0) {
                    Toast.makeText(AddBookActivity.this, "sách đã được thêm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddBookActivity.this, "thêm thất bại, hãy thử lại", Toast.LENGTH_SHORT).show();
                }

                // Clear the input fields after adding the book
                clearFields();
            }
        });
    }

    // Method to load data into spinners
    private void loadSpinnerData() {
        // Load authors into spinner
        authorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bookRepository.getAllAuthors());
        authorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAuthor.setAdapter(authorAdapter);

        // Load categories into spinner
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bookRepository.getAllCategories());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Load locations into spinner
        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bookRepository.getAllLocations());
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(locationAdapter);
    }

    // Method to clear the input fields
    private void clearFields() {
        edtTitle.setText("");
        edtYear.setText("");
        edtDescription.setText("");
        edtImage.setText("");
        spinnerAuthor.setSelection(0);
        spinnerCategory.setSelection(0);
        spinnerLocation.setSelection(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the repository when done
        bookRepository.close();
    }
}
