package com.example.librarymanage;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.adapter.BookAdapter;
import com.example.librarymanage.data.BookRepository;
import com.example.librarymanage.entities.Book;

import java.io.File;
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
// Thiết lập sự kiện click cho từng mục trong ListView
        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Book selectedBook = bookList.get(position);

                // Mở chi tiết sách
                Intent intent = new Intent(SearchActivity.this, BookDetailActivity.class);
                intent.putExtra("bookId", selectedBook.getBookId()); // Gửi bookId sang BookDetailActivity
                startActivity(intent);
            }
        });
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

                // Xử lý hình ảnh - Lấy giá trị hình ảnh (image là String kiểu URL hoặc tên tệp)
                String imageResource = imageIndex != -1 ? cursor.getString(imageIndex) : null;

                // Tạo đối tượng sách và thêm vào danh sách
                Book book = new Book(
                        bookId,
                        title,
                        authorName,
                        description,
                        imageResource,  // imageResource là String
                        categoryName,
                        locationName
                );
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookRepository.close();
    }
}
