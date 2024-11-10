package com.example.librarymanage.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.adapter.AuthorAdapter;
import com.example.librarymanage.data.BookRepository;

import java.util.List;

public class QLAuthorActivity extends AppCompatActivity {

    private EditText editTextAuthorName;
    private Button buttonAdd, buttonUpdate, buttonDelete;
    private ListView listViewAuthors;
    private BookRepository bookRepository;
    private List<String> authorList;
    private String selectedAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_author);

        // Khởi tạo BookRepository để tương tác với cơ sở dữ liệu
        bookRepository = new BookRepository(this);
        editTextAuthorName = findViewById(R.id.editTextAuthorName);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        listViewAuthors = findViewById(R.id.listViewAuthors);

        loadAuthors(); // Tải danh sách tác giả

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String authorName = editTextAuthorName.getText().toString();
                if (!authorName.isEmpty()) {
                    long id = bookRepository.addAuthor(authorName);
                    if (id != -1) {
                        Toast.makeText(QLAuthorActivity.this, "Author added successfully", Toast.LENGTH_SHORT).show();
                        loadAuthors(); // Tải lại danh sách sau khi thêm
                    } else {
                        Toast.makeText(QLAuthorActivity.this, "Failed to add author", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(QLAuthorActivity.this, "Please enter author name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAuthor != null) {
                    String newName = editTextAuthorName.getText().toString();
                    boolean updated = bookRepository.updateAuthor(selectedAuthor, newName);
                    if (updated) {
                        Toast.makeText(QLAuthorActivity.this, "Author updated successfully", Toast.LENGTH_SHORT).show();
                        loadAuthors(); // Tải lại danh sách sau khi cập nhật
                    } else {
                        Toast.makeText(QLAuthorActivity.this, "Failed to update author", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(QLAuthorActivity.this, "Select an author to update", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAuthor != null) {
                    boolean deleted = bookRepository.deleteAuthor(selectedAuthor);
                    if (deleted) {
                        Toast.makeText(QLAuthorActivity.this, "Author deleted successfully", Toast.LENGTH_SHORT).show();
                        loadAuthors(); // Tải lại danh sách sau khi xóa
                    } else {
                        Toast.makeText(QLAuthorActivity.this, "Failed to delete author", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(QLAuthorActivity.this, "Select an author to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listViewAuthors.setOnItemClickListener((parent, view, position, id) -> {
            selectedAuthor = authorList.get(position);
            editTextAuthorName.setText(selectedAuthor);
        });
    }

    private void loadAuthors() {
        authorList = bookRepository.getAllAuthors();
        AuthorAdapter adapter = new AuthorAdapter(this, authorList);
        listViewAuthors.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookRepository.close();
    }
}