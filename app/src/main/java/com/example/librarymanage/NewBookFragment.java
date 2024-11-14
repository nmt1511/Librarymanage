package com.example.librarymanage;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
        // Lấy 5 sách mới nhất được thêm trong tháng này
        Cursor cursor = bookRepository.getLatestBooksAddedThisMonth(5);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int bookId = cursor.getInt(cursor.getColumnIndexOrThrow("book_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                int authorId = cursor.getInt(cursor.getColumnIndexOrThrow("author_id"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                int imageResource = R.drawable.ic_open_book; // Hình ảnh mặc định

                // Lấy tên tác giả, thể loại và vị trí từ các bảng liên quan
                String authorName = bookRepository.getAuthorName(authorId);  // Giả sử bạn có phương thức getAuthorName trong BookRepository
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));
                int locationId = cursor.getInt(cursor.getColumnIndexOrThrow("location_id"));

                String categoryName = bookRepository.getCategoryName(categoryId);  // Lấy tên thể loại từ repository
                String locationName = bookRepository.getLocationName(locationId);  // Lấy tên vị trí từ repository

                // Thêm sách vào danh sách
                bookList.add(new Book(bookId, title, authorName, description, imageResource, categoryName, locationName));

            } while (cursor.moveToNext());
            cursor.close(); // Đóng cursor sau khi duyệt xong
        } else {
            Toast.makeText(getContext(), "Không có sách nào được thêm trong tháng này", Toast.LENGTH_SHORT).show();
        }
    }
}