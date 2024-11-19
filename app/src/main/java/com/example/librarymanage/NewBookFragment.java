package com.example.librarymanage;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.librarymanage.adapter.BookAdapter;
import com.example.librarymanage.data.BookRepository;
import com.example.librarymanage.entities.Book;

import java.util.ArrayList;

public class NewBookFragment extends Fragment {

    private ListView listViewNewBooks;
    private BookAdapter bookAdapter;
    private ArrayList<Book> bookList;
    private BookRepository bookRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_book, container, false);

        listViewNewBooks = view.findViewById(R.id.listViewNewBooks);
        bookList = new ArrayList<>();
        bookRepository = new BookRepository(getContext());

        // Tải sách mới
        loadNewBooksThisMonth();

        // Khởi tạo BookAdapter và gán vào ListView
        bookAdapter = new BookAdapter(getContext(), bookList);
        listViewNewBooks.setAdapter(bookAdapter);

        // Thiết lập sự kiện click cho từng mục trong ListView
        listViewNewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book selectedBook = bookList.get(position);

                // Mở chi tiết sách
                Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                intent.putExtra("bookId", selectedBook.getBookId()); // Gửi bookId sang BookDetailActivity
                startActivity(intent);
            }
        });

        return view;
    }
    private void loadNewBooksThisMonth() {
        // Xóa danh sách cũ trước khi nạp
        bookList.clear();

        // Lấy 5 sách mới nhất được thêm trong tháng này
        Cursor cursor = bookRepository.getLatestBooksAddedThisMonth(5);

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

                Log.e("LoadNewBooksError", "Một hoặc nhiều chỉ mục cột bị thiếu");
                Toast.makeText(getContext(), "Lỗi truy xuất dữ liệu sách", Toast.LENGTH_SHORT).show();
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

                    // Lấy giá trị của hình ảnh (image là String kiểu URL hoặc tên tệp)
                    String imageResource = imageIndex != -1 ? cursor.getString(imageIndex) : null;

                    // Tạo đối tượng sách và thêm vào danh sách
                    Book book = new Book(
                            bookId,
                            title,
                            authorName,
                            description,
                            imageResource,  // imageResource đã chuyển thành kiểu String
                            categoryName,
                            locationName
                    );
                    bookList.add(book);

                } while (cursor.moveToNext());

                // Cập nhật adapter nếu danh sách thay đổi
                if (bookAdapter != null) {
                    bookAdapter.notifyDataSetChanged();
                }

                // Thông báo nếu danh sách rỗng
                if (bookList.isEmpty()) {
                    Toast.makeText(getContext(), "Không có sách nào được thêm trong tháng này", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.e("LoadNewBooksError", "Lỗi khi nạp sách mới", e);
                Toast.makeText(getContext(), "Có lỗi xảy ra khi tải sách mới", Toast.LENGTH_SHORT).show();
            } finally {
                // Đảm bảo đóng cursor
                cursor.close();
            }
        } else {
            Toast.makeText(getContext(), "Không có sách nào được thêm trong tháng này", Toast.LENGTH_SHORT).show();
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


}