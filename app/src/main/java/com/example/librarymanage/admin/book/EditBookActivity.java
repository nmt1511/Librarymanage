package com.example.librarymanage.admin.book;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.BookRepository;

import java.util.List;

public class EditBookActivity extends AppCompatActivity {
    private EditText editTitle, editPublishedYear, editDescription, editImagePath;
    private Spinner spinnerAuthor, spinnerCategory, spinnerLocation;
    private Button btnUpdate;

    private BookRepository bookRepository;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        // Khởi tạo các view
        initializeViews();

        // Khởi tạo repository
        bookRepository = new BookRepository(this);

        // Lấy bookId từ intent
        bookId = getIntent().getIntExtra("bookId", -1);

        // Tải dữ liệu sách
        loadBookData();

        // Thiết lập sự kiện click cho nút cập nhật
        btnUpdate.setOnClickListener(v -> updateBook());
    }

    private void initializeViews() {
        editTitle = findViewById(R.id.editTitle);
        editPublishedYear = findViewById(R.id.editPublishedYear);
        editDescription = findViewById(R.id.editDescription);
        editImagePath = findViewById(R.id.editImagePath); // Thêm EditText cho đường dẫn ảnh
        spinnerAuthor = findViewById(R.id.spinnerAuthor);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        btnUpdate = findViewById(R.id.btnUpdate);
    }

    private void loadBookData() {
        Cursor cursor = bookRepository.getBookById(bookId);
        if (cursor != null && cursor.moveToFirst()) {
            // Điền thông tin sách vào các trường
            editTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            editPublishedYear.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("published_year"))));
            editDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow("description")));

            // Nếu có cột image, hiển thị đường dẫn ảnh
            try {
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                if (!TextUtils.isEmpty(imagePath)) {
                    editImagePath.setText(imagePath);
                }
            } catch (IllegalArgumentException e) {
                // Nếu không có cột image, bỏ qua
            }

            cursor.close();
        }

        // Điền dữ liệu vào các Spinner
        populateSpinner(spinnerAuthor, bookRepository.getAllAuthors());
        populateSpinner(spinnerCategory, bookRepository.getAllCategories());
        populateSpinner(spinnerLocation, bookRepository.getAllLocations());
    }

    private void populateSpinner(Spinner spinner, List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void updateBook() {
        // Kiểm tra và thu thập dữ liệu từ các trường
        String title = editTitle.getText().toString().trim();
        String author = spinnerAuthor.getSelectedItem().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        String location = spinnerLocation.getSelectedItem().toString();
        String imagePath = editImagePath.getText().toString().trim();

        // Kiểm tra validate
        if (TextUtils.isEmpty(title)) {
            editTitle.setError("Tiêu đề không được để trống");
            return;
        }

        int publishedYear;
        try {
            publishedYear = Integer.parseInt(editPublishedYear.getText().toString().trim());
        } catch (NumberFormatException e) {
            editPublishedYear.setError("Năm xuất bản không hợp lệ");
            return;
        }

        String description = editDescription.getText().toString().trim();

        // Gọi phương thức cập nhật sách
        long result;
        if (TextUtils.isEmpty(imagePath)) {
            // Nếu không có đường dẫn ảnh
            result = bookRepository.updateBook(
                    bookId, title, author, category, location,
                    publishedYear, description
            );
        } else {
            // Nếu có đường dẫn ảnh
            result = bookRepository.updateBook(
                    bookId, title, author, category, location,
                    publishedYear, description, imagePath
            );
        }

        // Xử lý kết quả cập nhật
        if (result > 0) {
            Toast.makeText(this, "Sách cập nhật thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Đã xảy ra lỗi khi cập nhật", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookRepository.close();
    }
}