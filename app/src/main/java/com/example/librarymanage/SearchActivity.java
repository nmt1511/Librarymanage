package com.example.librarymanage;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.adapter.BookAdapter;
import com.example.librarymanage.data.BookRepository;
import com.example.librarymanage.entities.Book;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private EditText etSearch;
    private ListView lvBooks;
    private BookRepository bookRepository;
    private BookAdapter bookAdapter;
    private ArrayList<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Khởi tạo các view
        etSearch = findViewById(R.id.etSearch);
        lvBooks = findViewById(R.id.lvBooks);

        // Khởi tạo repository và adapter
        bookRepository = new BookRepository(this);
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(this, bookList);
        lvBooks.setAdapter(bookAdapter);

        // Sử dụng TextWatcher để lắng nghe thay đổi trong etSearch
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Không cần thực hiện gì khi văn bản chưa thay đổi
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Tìm kiếm khi người dùng thay đổi văn bản
                String query = charSequence.toString().trim();
                if (!query.isEmpty()) {
                    searchBooks(query);
                } else {
                    // Nếu không có gì nhập vào, xóa danh sách hiện tại
                    bookList.clear();
                    bookAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Không cần thực hiện gì sau khi văn bản đã thay đổi
            }
        });
    }

    private void searchBooks(String query) {
        // Tìm kiếm sách trong cơ sở dữ liệu
        Cursor cursor = bookRepository.getBooksByKeyword(query);

        // Xóa danh sách cũ
        bookList.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int bookId = cursor.getInt(cursor.getColumnIndexOrThrow("book_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                int authorId = cursor.getInt(cursor.getColumnIndexOrThrow("author_id"));
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));
                int locationId = cursor.getInt(cursor.getColumnIndexOrThrow("location_id"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                int imageResource = R.drawable.ic_open_book; // Hình ảnh mặc định

                // Lấy thông tin từ các bảng liên quan
                String authorName = bookRepository.getAuthorName(authorId); // Lấy tên tác giả
                String categoryName = bookRepository.getCategoryName(categoryId); // Lấy tên thể loại
                String locationName = bookRepository.getLocationName(locationId); // Lấy tên vị trí

                // Thêm sách vào danh sách với tất cả thông tin cần thiết
                bookList.add(new Book(bookId, title, authorName, description, imageResource, categoryName, locationName));
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Cập nhật adapter để hiển thị kết quả
        if (bookList.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy sách nào!", Toast.LENGTH_SHORT).show();
        }
        bookAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookRepository.close();
    }
}
