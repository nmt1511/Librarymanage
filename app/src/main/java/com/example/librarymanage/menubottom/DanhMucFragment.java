package com.example.librarymanage.menubottom;

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

    // Method to open BookFragment with selected category
    private void openBooksByCategory(String categoryName) {
        // Create an instance of BookFragment and pass the category name
        BookFragment bookFragment = new BookFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category_name", categoryName);
        bookFragment.setArguments(bundle);

        // Replace current fragment with BookFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, bookFragment); // Ensure the container ID is correct
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
