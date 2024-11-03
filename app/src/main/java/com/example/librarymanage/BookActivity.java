package com.example.librarymanage;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.adapter.BookAdapter;
import com.example.librarymanage.data.BookRepository;
import com.example.librarymanage.entities.Book;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

    private ListView listView;
    private BookAdapter bookAdapter; // Sử dụng BookAdapter đúng cách
    private ArrayList<Book> bookList;
    private BookRepository bookRepository; // Khai báo lớp BookRepository

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        listView = findViewById(R.id.listViewBooks);
        bookList = new ArrayList<>();
        bookRepository = new BookRepository(this); // Khởi tạo lớp BookRepository

        // Lấy dữ liệu sách từ BookRepository
        loadBooks();

        // Khởi tạo BookAdapter
        bookAdapter = new BookAdapter(this, bookList);
        listView.setAdapter(bookAdapter);

        // Thiết lập sự kiện click cho từng mục trong ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Book selectedBook = bookList.get(position);
                Toast.makeText(BookActivity.this, "Bạn đã chọn: " + selectedBook.getTitle(), Toast.LENGTH_SHORT).show();

                // Mở chi tiết sách
                Intent intent = new Intent(BookActivity.this, BookDetailActivity.class);
                intent.putExtra("bookId", selectedBook.getBookId()); // Gửi bookId sang BookDetailActivity
                startActivity(intent);
            }
        });
    }

    private void loadBooks() {
        Cursor cursor = bookRepository.getAllBooks(); // Gọi phương thức từ BookRepository
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int bookIdIndex = cursor.getColumnIndex("book_id");
                int titleIndex = cursor.getColumnIndex("title");
                int authorIdIndex = cursor.getColumnIndex("author_id");
                int descriptionIndex = cursor.getColumnIndex("description");

                // Kiểm tra chỉ số cột có hợp lệ không
                if (bookIdIndex != -1 && titleIndex != -1 && authorIdIndex != -1 && descriptionIndex != -1) {
                    int bookId = cursor.getInt(bookIdIndex);
                    String title = cursor.getString(titleIndex);
                    int authorId = cursor.getInt(authorIdIndex); // Lưu ID tác giả
                    String description = cursor.getString(descriptionIndex);
                    int imageResource = R.drawable.ic_open_book; // Hình ảnh mặc định

                    // Thêm sách vào danh sách
                    bookList.add(new Book(bookId, title, String.valueOf(authorId), description, imageResource));
                } else {
                    // Xử lý trường hợp không tìm thấy cột
                    Toast.makeText(this, "Có lỗi xảy ra khi lấy dữ liệu sách", Toast.LENGTH_SHORT).show();
                    break;
                }
            } while (cursor.moveToNext());
            cursor.close(); // Đóng cursor
        } else {
            Toast.makeText(this, "Không có sách nào trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
