package com.example.librarymanage;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

        return view;
    }

    private void loadNewBooksThisMonth() {
        Cursor cursor = bookRepository.getLatestBooksAddedThisMonth(5); // Lấy 5 sách mới nhất
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int bookId = cursor.getInt(cursor.getColumnIndexOrThrow("book_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author_id")); // Lưu ID tác giả
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                int imageResource = R.drawable.ic_open_book; // Hình ảnh mặc định

                // Thêm sách vào danh sách
                bookList.add(new Book(bookId, title, author, description, imageResource));
            } while (cursor.moveToNext());
            cursor.close(); // Đóng cursor
        } else {
            Toast.makeText(getContext(), "Không có sách nào được thêm trong tháng này", Toast.LENGTH_SHORT).show();
        }
    }


}
