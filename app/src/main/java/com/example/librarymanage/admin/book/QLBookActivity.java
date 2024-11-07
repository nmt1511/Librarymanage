package com.example.librarymanage.admin.book;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
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

        // Khởi tạo repository và adapter
        bookRepository = new BookRepository(this);
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(this, bookList);

        // Thiết lập adapter cho ListView
        lvBooks.setAdapter(bookAdapter);

        // Sự kiện khi nhấn nút tìm kiếm
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

        // Sự kiện khi nhấn nút thêm sách
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QLBookActivity.this, AddBookActivity.class);
                startActivity(intent);
            }
        });

        loadAllBooks();
    }

    // Hàm tải tất cả sách
    private void loadAllBooks() {
        Cursor cursor = bookRepository.getAllBooks();
        if (cursor != null && cursor.moveToFirst()) {
            bookList.clear();
            do {
                int bookId = cursor.getInt(cursor.getColumnIndexOrThrow("book_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String authorName = bookRepository.getAuthorName(cursor.getInt(cursor.getColumnIndexOrThrow("author_id")));
                String categoryName = bookRepository.getCategoryName(cursor.getInt(cursor.getColumnIndexOrThrow("category_id")));
                String locationName = bookRepository.getLocationName(cursor.getInt(cursor.getColumnIndexOrThrow("location_id")));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                Book book = new Book(bookId, title, authorName, description, R.drawable.ic_open_book, categoryName, locationName);
                bookList.add(book);
            } while (cursor.moveToNext());
            cursor.close();
        }
        bookAdapter.notifyDataSetChanged();
    }

    // Hàm tìm kiếm sách theo từ khóa
    private void searchBooks(String keyword) {
        Cursor cursor = bookRepository.getBooksByKeyword(keyword);
        if (cursor != null && cursor.moveToFirst()) {
            bookList.clear();
            do {
                int bookId = cursor.getInt(cursor.getColumnIndexOrThrow("book_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String authorName = bookRepository.getAuthorName(cursor.getInt(cursor.getColumnIndexOrThrow("author_id")));
                String categoryName = bookRepository.getCategoryName(cursor.getInt(cursor.getColumnIndexOrThrow("category_id")));
                String locationName = bookRepository.getLocationName(cursor.getInt(cursor.getColumnIndexOrThrow("location_id")));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                Book book = new Book(bookId, title, authorName, description, R.drawable.ic_open_book, categoryName, locationName);
                bookList.add(book);
            } while (cursor.moveToNext());
            cursor.close();
        }
        bookAdapter.notifyDataSetChanged();

        if (bookList.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy sách", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookRepository.close();
    }
}
