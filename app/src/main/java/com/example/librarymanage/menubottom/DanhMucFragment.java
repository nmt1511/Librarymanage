package com.example.librarymanage.menubottom;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.librarymanage.BookActivity;
import com.example.librarymanage.R;
import com.example.librarymanage.data.DataBook;

import java.util.ArrayList;
import java.util.List;

public class DanhMucFragment extends Fragment {

    private ListView categoryListView;
    private DataBook dataHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_danh_muc, container, false);

        // Initialize UI components and database helper
        categoryListView = view.findViewById(R.id.categoryListView);
        dataHelper = new DataBook(getContext());

        // Retrieve categories and set them to the ListView
        List<String> categories = getCategoriesFromDatabase();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, categories);
        categoryListView.setAdapter(adapter);

        // Set item click listener to open books for the selected category
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories.get(position);
                openBooksByCategory(selectedCategory);
            }
        });

        return view;
    }

    // Method to query and retrieve categories
    private List<String> getCategoriesFromDatabase() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = dataHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT name FROM Category", null);
        if (cursor.moveToFirst()) {
            do {
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                categories.add(categoryName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return categories;
    }


    private void openBooksByCategory(String categoryName) {
        // Tạo một Intent để mở BookActivity
        Intent intent = new Intent(getContext(), BookActivity.class);

        // Đưa tên thể loại vào Intent
        intent.putExtra("category_name", categoryName);

        // Bắt đầu BookActivity
        startActivity(intent);
    }

}
