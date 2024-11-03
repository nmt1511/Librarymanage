package com.example.librarymanage.menubottom;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.librarymanage.R;
import com.example.librarymanage.data.DataBook;

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends Fragment {

    private ListView bookListView;
    private DataBook dataHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        // Initialize UI components and database helper
        bookListView = view.findViewById(R.id.bookListView);
        dataHelper = new DataBook(getContext());

        // Get the selected category from arguments
        String categoryName = getArguments() != null ? getArguments().getString("category_name") : "";

        // Retrieve books in the selected category and set them to the ListView
        List<String> books = getBooksByCategory(categoryName);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, books);
        bookListView.setAdapter(adapter);

        return view;
    }

    // Method to query and retrieve books based on category
    private List<String> getBooksByCategory(String categoryName) {
        List<String> books = new ArrayList<>();
        SQLiteDatabase db = dataHelper.getReadableDatabase();

        String query = "SELECT title FROM Books WHERE category_id = (SELECT category_id FROM Category WHERE name = ?)";
        Cursor cursor = db.rawQuery(query, new String[]{categoryName});
        if (cursor.moveToFirst()) {
            do {
                String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                books.add(bookTitle);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return books;
    }
}
