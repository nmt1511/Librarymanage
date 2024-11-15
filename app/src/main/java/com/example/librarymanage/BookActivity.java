package com.example.librarymanage;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.adapter.BookAdapter;
import com.example.librarymanage.data.BookRepository;
import com.example.librarymanage.entities.Book;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {

    private ListView listView;
    private BookAdapter bookAdapter;
    private ArrayList<Book> bookList;
    private BookRepository bookRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        listView = findViewById(R.id.listViewBooks);
        bookList = new ArrayList<>();
        bookRepository = new BookRepository(this);

        // Nhận category_name từ Intent
        String categoryName = getIntent().getStringExtra("category_name");

        // Lấy dữ liệu sách từ BookRepository theo thể loại hoặc toàn bộ nếu không có
        if (categoryName != null && !categoryName.isEmpty()) {
            loadBooksByCategory(categoryName);
        } else {
            loadAllBooks(); // Gọi phương thức để lấy toàn bộ sách
        }

        // Khởi tạo BookAdapter
        bookAdapter = new BookAdapter(this, bookList);
        listView.setAdapter(bookAdapter);

        // Thiết lập sự kiện click cho từng mục trong ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Book selectedBook = bookList.get(position);

                // Mở chi tiết sách
                Intent intent = new Intent(BookActivity.this, BookDetailActivity.class);
                intent.putExtra("bookId", selectedBook.getBookId()); // Gửi bookId sang BookDetailActivity
                startActivity(intent);
            }
        });

        // Xử lý sự kiện nhấn nút quay lại
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kết thúc Activity và quay lại Activity trước đó
            }
        });
    }

    // Phương thức để lấy dữ liệu sách theo danh mục
    private void loadBooksByCategory(String categoryName) {
        Cursor cursor = bookRepository.getBooksByCategory(categoryName); // Gọi phương thức từ BookRepository
        loadBooksFromCursor(cursor);
    }

    // Phương thức để lấy tất cả sách
    private void loadAllBooks() {
        Cursor cursor = bookRepository.getAllBooks(); // Gọi phương thức từ BookRepository
        loadBooksFromCursor(cursor);
    }

    private void loadBooksFromCursor(Cursor cursor) {
        // Xóa danh sách sách cũ trước khi nạp
        bookList.clear();

        if (cursor != null && cursor.moveToFirst()) {
            // Lấy chỉ mục cột một cách an toàn trước
            int bookIdIndex = cursor.getColumnIndex("book_id");
            int titleIndex = cursor.getColumnIndex("title");
            int authorIdIndex = cursor.getColumnIndex("author_id");
            int descriptionIndex = cursor.getColumnIndex("description");
            int categoryIdIndex = cursor.getColumnIndex("category_id");
            int locationIdIndex = cursor.getColumnIndex("location_id");
            int imageIndex = cursor.getColumnIndex("image");

            // Kiểm tra tính hợp lệ của các chỉ mục
            if (bookIdIndex == -1 || titleIndex == -1 || authorIdIndex == -1 ||
                    descriptionIndex == -1 || categoryIdIndex == -1 ||
                    locationIdIndex == -1) {

                Log.e("LoadBooksError", "Một hoặc nhiều chỉ mục cột bị thiếu");
                Toast.makeText(this, "Lỗi truy xuất dữ liệu sách", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                do {
                    // Lấy giá trị từ cursor
                    int bookId = cursor.getInt(bookIdIndex);
                    String title = cursor.getString(titleIndex);
                    int authorId = cursor.getInt(authorIdIndex);
                    String description = cursor.getString(descriptionIndex);
                    int categoryId = cursor.getInt(categoryIdIndex);
                    int locationId = cursor.getInt(locationIdIndex);

                    // Lấy tên tác giả, thể loại, vị trí
                    String authorName = getAuthorNameSafely(authorId);
                    String categoryName = getCategoryNameSafely(categoryId);
                    String locationName = getLocationNameSafely(locationId);

                    // Xử lý hình ảnh
                    int imageResource = handleBookImageDisplay(
                            imageIndex != -1 ? cursor.getString(imageIndex) : null
                    );

                    // Tạo đối tượng sách và thêm vào danh sách
                    Book book = new Book(
                            bookId,
                            title,
                            authorName,
                            description,
                            imageResource,
                            categoryName,
                            locationName
                    );
                    bookList.add(book);

                } while (cursor.moveToNext());

                // Thông báo nếu danh sách rỗng
                if (bookList.isEmpty()) {
                    Toast.makeText(this, "Không có sách nào trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.e("LoadBooksError", "Lỗi khi nạp sách", e);
                Toast.makeText(this, "Có lỗi xảy ra khi tải sách", Toast.LENGTH_SHORT).show();
            } finally {
                // Đảm bảo đóng cursor
                cursor.close();
            }
        } else {
            Toast.makeText(this, "Không có sách nào trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
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
    private int handleBookImageDisplay(String imageName) {
        // Sử dụng phương thức handleImageDisplay đã có
        return handleImageDisplay(imageName);
    }

    // Phương thức riêng để xử lý hiển thị hình ảnh
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
    }
}
