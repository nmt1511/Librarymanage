package com.example.librarymanage.admin.book;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditBookActivity extends AppCompatActivity {
    private EditText editTitle, editPublishedYear, editDescription;
    private TextView edtImage;
    private Spinner spinnerAuthor, spinnerCategory, spinnerLocation;
    private Button btnUpdate;
    private ImageView imgBookImage;
    private Button btnUploadImage;
    private String currentImagePath = "";
    private static final int PICK_IMAGE_REQUEST = 1;

    private BookRepository bookRepository;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        initializeViews();
        bookRepository = new BookRepository(this);
        bookId = getIntent().getIntExtra("bookId", -1);
        loadBookData();
        btnUpdate.setOnClickListener(v -> updateBook());

        // Thiết lập sự kiện click để chọn ảnh
        btnUploadImage.setOnClickListener(v -> openImagePicker());
    }

    private void initializeViews() {
        editTitle = findViewById(R.id.editTitle);
        editPublishedYear = findViewById(R.id.editPublishedYear);
        editDescription = findViewById(R.id.editDescription);
        edtImage = findViewById(R.id.editImagePath); // Thêm EditText cho đường dẫn ảnh
        spinnerAuthor = findViewById(R.id.spinnerAuthor);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        btnUpdate = findViewById(R.id.btnUpdate);
        imgBookImage = findViewById(R.id.imgBookImage);
        btnUploadImage = findViewById(R.id.btnUploadImage);
    }

    private void loadBookData() {
        Cursor cursor = bookRepository.getBookById(bookId);
        if (cursor != null && cursor.moveToFirst()) {
            // Điền thông tin sách vào các trường
            editTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            editPublishedYear.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("published_year"))));
            editDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow("description")));

            // Xử lý đường dẫn ảnh (nếu có)
            try {
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                if (!TextUtils.isEmpty(imagePath)) {
                    edtImage.setText(imagePath);
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    imgBookImage.setImageBitmap(bitmap);
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
    private String saveImageToInternalStorage(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
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
    private void updateBook() {
        // Kiểm tra và thu thập dữ liệu từ các trường
        String title = editTitle.getText().toString().trim();
        String author = spinnerAuthor.getSelectedItem().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        String location = spinnerLocation.getSelectedItem().toString();
        String imagePath = edtImage.getText().toString().trim();

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