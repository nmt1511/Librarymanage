package com.example.librarymanage.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.adapter.CategoryAdapter;
import com.example.librarymanage.data.BookRepository;

import java.util.List;

public class QlCategoryActivity extends AppCompatActivity {

    private EditText editTextCategoryName;
    private Button buttonAdd, buttonUpdate, buttonDelete;
    private ListView listViewCategories;
    private BookRepository bookRepository;
    private List<String> categoryList;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qc_category);

        bookRepository = new BookRepository(this);
        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        listViewCategories = findViewById(R.id.listViewCategories);

        loadCategories();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = editTextCategoryName.getText().toString();
                if (!categoryName.isEmpty()) {
                    long id = bookRepository.addCategory(categoryName);
                    if (id != -1) {
                        Toast.makeText(QlCategoryActivity.this, "Category added successfully", Toast.LENGTH_SHORT).show();
                        loadCategories();
                    } else {
                        Toast.makeText(QlCategoryActivity.this, "Failed to add category", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(QlCategoryActivity.this, "Please enter category name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCategory != null) {
                    String newName = editTextCategoryName.getText().toString();
                    boolean updated = bookRepository.updateCategory(selectedCategory, newName);
                    if (updated) {
                        Toast.makeText(QlCategoryActivity.this, "Category updated successfully", Toast.LENGTH_SHORT).show();
                        loadCategories();
                    } else {
                        Toast.makeText(QlCategoryActivity.this, "Failed to update category", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(QlCategoryActivity.this, "Select a category to update", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCategory != null) {
                    boolean deleted = bookRepository.deleteCategory(selectedCategory);
                    if (deleted) {
                        Toast.makeText(QlCategoryActivity.this, "Category deleted successfully", Toast.LENGTH_SHORT).show();
                        loadCategories();
                    } else {
                        Toast.makeText(QlCategoryActivity.this, "Failed to delete category", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(QlCategoryActivity.this, "Select a category to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listViewCategories.setOnItemClickListener((parent, view, position, id) -> {
            selectedCategory = categoryList.get(position);
            editTextCategoryName.setText(selectedCategory);
        });
    }

    private void loadCategories() {
        categoryList = bookRepository.getAllCategories();
        CategoryAdapter adapter = new CategoryAdapter(this, categoryList);
        listViewCategories.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookRepository.close();
    }
}