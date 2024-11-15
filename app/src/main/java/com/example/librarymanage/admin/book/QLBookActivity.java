package com.example.librarymanage.admin.book;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.adapter.BookAdapter;
import com.example.librarymanage.data.BookRepository;
import com.example.librarymanage.entities.Book;

import java.util.ArrayList;

public class QLBookActivity extends AppCompatActivity {

    private EditText etSearchBook;
    private ListView lvBooks;
    private Button btnAddBook;
    private BookRepository bookRepository;
    private ArrayList<Book> bookList;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_book);

        etSearchBook = findViewById(R.id.et_search_book);
        lvBooks = findViewById(R.id.lv_books);
        btnAddBook = findViewById(R.id.btn_add_book);

        // Initialize repository and adapter
        bookRepository = new BookRepository(this);
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(this, bookList);

        // Set adapter for ListView
        lvBooks.setAdapter(bookAdapter);

        // Add long-click listener for ListView items
        lvBooks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Book selectedBook = bookList.get(position);

                // Show dialog with options
                new android.app.AlertDialog.Builder(QLBookActivity.this)
                        .setTitle("Chọn thao tác")
                        .setItems(new CharSequence[]{"Sửa", "Xóa"}, (dialog, which) -> {
                            if (which == 0) { // Edit option
                                Intent intent = new Intent(QLBookActivity.this, EditBookActivity.class);
                                intent.putExtra("bookId", selectedBook.getBookId());
                                startActivity(intent);
                            } else if (which == 1) { // Delete option
                                // Delete book from database
                                bookRepository.deleteBook(selectedBook.getBookId());
                                Toast.makeText(QLBookActivity.this, "Đã xóa sách", Toast.LENGTH_SHORT).show();

                                // Close the current activity and reopen it to refresh the list
                                finish();
                                startActivity(new Intent(QLBookActivity.this, QLBookActivity.class));
                            }
                        })
                        .show();

                return true; // Return true to indicate long-click handled
            }
        });



        // Add TextWatcher for search functionality
        etSearchBook.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchBooks(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        // Add click listener for the add book button
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QLBookActivity.this, AddBookActivity.class);
                startActivity(intent);
            }
        });

        // Load all books initially
        loadAllBooks();
    }

    // Load all books from database
    private void loadAllBooks() {
        try {
            Cursor cursor = bookRepository.getAllBooks();
            if (cursor != null && cursor.moveToFirst()) {
                bookList.clear();

                // Lấy chỉ mục cột
                int bookIdIndex = cursor.getColumnIndex("book_id");
                int titleIndex = cursor.getColumnIndex("title");
                int authorIdIndex = cursor.getColumnIndex("author_id");
                int categoryIdIndex = cursor.getColumnIndex("category_id");
                int locationIdIndex = cursor.getColumnIndex("location_id");
                int descriptionIndex = cursor.getColumnIndex("description");
                int imageIndex = cursor.getColumnIndex("image");

                do {
                    int bookId = cursor.getInt(bookIdIndex);
                    String title = cursor.getString(titleIndex);
                    String authorName = getAuthorNameSafely(cursor.getInt(authorIdIndex));
                    String categoryName = getCategoryNameSafely(cursor.getInt(categoryIdIndex));
                    String locationName = getLocationNameSafely(cursor.getInt(locationIdIndex));
                    String description = cursor.getString(descriptionIndex);

                    // Xử lý hình ảnh
                    int imageResource = handleImageDisplay(
                            imageIndex != -1 ? cursor.getString(imageIndex) : null
                    );

                    Book book = new Book(bookId, title, authorName, description,
                            imageResource, categoryName, locationName);
                    bookList.add(book);

                } while (cursor.moveToNext());
                cursor.close();
            }

            bookAdapter.notifyDataSetChanged();

            if (bookList.isEmpty()) {
                Toast.makeText(this, "Không có sách nào", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("LoadBooksError", "Lỗi khi tải sách", e);
            Toast.makeText(this, "Có lỗi xảy ra khi tải sách", Toast.LENGTH_SHORT).show();
        }
    }

    // Search books by keyword
    private void searchBooks(String keyword) {
        Cursor cursor = bookRepository.getBooksByKeyword(keyword);
        if (cursor != null && cursor.moveToFirst()) {
            bookList.clear();
            // Lấy chỉ mục cột
            int bookIdIndex = cursor.getColumnIndex("book_id");
            int titleIndex = cursor.getColumnIndex("title");
            int authorIdIndex = cursor.getColumnIndex("author_id");
            int categoryIdIndex = cursor.getColumnIndex("category_id");
            int locationIdIndex = cursor.getColumnIndex("location_id");
            int descriptionIndex = cursor.getColumnIndex("description");
            int imageIndex = cursor.getColumnIndex("image");

            do {
                int bookId = cursor.getInt(bookIdIndex);
                String title = cursor.getString(titleIndex);
                String authorName = getAuthorNameSafely(cursor.getInt(authorIdIndex));
                String categoryName = getCategoryNameSafely(cursor.getInt(categoryIdIndex));
                String locationName = getLocationNameSafely(cursor.getInt(locationIdIndex));
                String description = cursor.getString(descriptionIndex);

                // Xử lý hình ảnh
                int imageResource = handleImageDisplay(
                        imageIndex != -1 ? cursor.getString(imageIndex) : null
                );

                Book book = new Book(bookId, title, authorName, description,
                        imageResource, categoryName, locationName);
                bookList.add(book);

            } while (cursor.moveToNext());
            cursor.close();
        }
        bookAdapter.notifyDataSetChanged();

        if (bookList.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy sách", Toast.LENGTH_SHORT).show();
        }
    }
    // Phương thức an toàn để lấy tên tác giả
    private String getAuthorNameSafely(int authorId) {
        try {
            return bookRepository.getAuthorName(authorId);
        } catch (Exception e) {
            Log.e("AuthorNameError", "Lỗi khi lấy tên tác giả", e);
            return "Không rõ tác giả";
        }
    }

    // Phương thức an toàn để lấy tên thể loại
    private String getCategoryNameSafely(int categoryId) {
        try {
            return bookRepository.getCategoryName(categoryId);
        } catch (Exception e) {
            Log.e("CategoryNameError", "Lỗi khi lấy tên thể loại", e);
            return "Không rõ thể loại";
        }
    }

    // Phương thức an toàn để lấy tên vị trí
    private String getLocationNameSafely(int locationId) {
        try {
            return bookRepository.getLocationName(locationId);
        } catch (Exception e) {
            Log.e("LocationNameError", "Lỗi khi lấy tên vị trí", e);
            return "Không rõ vị trí";
        }
    }

    // Phương thức xử lý hình ảnh sách
    private int handleImageDisplay(String imageName) {
        // Giá trị mặc định
        int defaultImageResourceId = R.drawable.ic_open_book;

        // Kiểm tra tên hình ảnh
        if (imageName == null || imageName.isEmpty()) {
            return defaultImageResourceId;
        }

        try {
            // Loại bỏ phần mở rộng file nếu có
            String resourceName = imageName.contains(".")
                    ? imageName.split("\\.")[0]
                    : imageName;

            // Lấy ID tài nguyên drawable
            int drawableResourceId = getResources().getIdentifier(
                    resourceName,
                    "drawable",
                    getPackageName()
            );

            // Trả về ID tài nguyên nếu tìm thấy, ngược lại trả về hình ảnh mặc định
            return drawableResourceId != 0
                    ? drawableResourceId
                    : defaultImageResourceId;

        } catch (Exception e) {
            // Ghi log lỗi nếu có
            Log.e("ImageLoadError", "Lỗi khi tải hình ảnh: " + imageName, e);
            return defaultImageResourceId;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookRepository.close();
    }
}
