package com.example.librarymanage.admin.book;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.BookRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddBookActivity extends AppCompatActivity {
    private Button btnUploadImage;
    private ImageView imgBookImage;
    private String currentImagePath = "";
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText edtTitle, edtYear, edtDescription;
    private Spinner spinnerAuthor, spinnerCategory, spinnerLocation;
    private Button btnAddBook;
    private BookRepository bookRepository;
    private ArrayAdapter<String> authorAdapter, categoryAdapter, locationAdapter;
    private int selectedAuthorId, selectedCategoryId, selectedLocationId;
    private TextView edtImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // Initialize the views
        edtTitle = findViewById(R.id.edtTitle);
        edtYear = findViewById(R.id.edtYear);
        edtDescription = findViewById(R.id.edtDescription);
        spinnerAuthor = findViewById(R.id.spinnerAuthor);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        btnAddBook = findViewById(R.id.btnAddBook);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        imgBookImage = findViewById(R.id.imgBookImage);
        edtImage = findViewById(R.id.edtImage);

        // Initialize the BookRepository
        bookRepository = new BookRepository(this);

        // Load data into spinners
        loadSpinnerData();

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

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

                // Check for empty fields
                if (title.isEmpty() || yearStr.isEmpty() || description.isEmpty() ||
                        selectedAuthorId == -1 || selectedCategoryId == -1 || selectedLocationId == -1 ||
                        currentImagePath.isEmpty()) {
                    Toast.makeText(AddBookActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Convert year to integer
                int publishedYear;
                try {
                    publishedYear = Integer.parseInt(yearStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(AddBookActivity.this, "Vui lòng nhập năm hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate year
                if (publishedYear < 0 || publishedYear > Calendar.getInstance().get(Calendar.YEAR)) {
                    Toast.makeText(AddBookActivity.this, "Năm xuất bản không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Prepare current date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String currentDate = sdf.format(new Date());

                // Add the book to the database using currentImagePath
                long newBookId = bookRepository.addBook(
                        title,
                        selectedAuthorId,
                        selectedCategoryId,
                        selectedLocationId,
                        publishedYear,
                        description,
                        currentImagePath,  // Sử dụng đường dẫn ảnh từ internal storage
                        currentDate
                );

                // Check if the book was added successfully
                if (newBookId > 0) {
                    Toast.makeText(AddBookActivity.this, "Sách đã được thêm thành công", Toast.LENGTH_SHORT).show();

                    // Clear the input fields after adding the book
                    clearFields();
                } else {
                    Toast.makeText(AddBookActivity.this, "Thêm sách thất bại, hãy thử lại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Phương thức mở trình chọn ảnh
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    // Lấy Bitmap từ URI
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    imgBookImage.setImageBitmap(bitmap);

                    // Lưu ảnh vào bộ nhớ trong
                    currentImagePath = saveImageToInternalStorage(bitmap);

                    // Đặt đường dẫn ảnh vào EditText
                    edtImage.setText(currentImagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Không thể tải ảnh", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Không thể truy cập ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Phương thức lưu ảnh vào bộ nhớ trong
    private String saveImageToInternalStorage(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "BOOK_" + timeStamp + ".jpg";

        File storageDir = getFilesDir();
        File imageFile = new File(storageDir, imageFileName);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
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

        // Reset image
        imgBookImage.setImageResource(android.R.color.transparent); // Hoặc một hình ảnh mặc định
        currentImagePath = ""; // Đặt lại đường dẫn ảnh
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the repository when done
        bookRepository.close();
    }
}
