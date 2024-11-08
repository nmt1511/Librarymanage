package com.example.librarymanage.admin.author;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.BookRepository;

import java.util.List;

public class QLAuthorActivity extends AppCompatActivity {

    private BookRepository bookRepository;
    private ListView listViewAuthors;
    private Button buttonAddAuthor;
    private ArrayAdapter<String> authorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_author);

        // Khởi tạo BookRepository để tương tác với cơ sở dữ liệu
        bookRepository = new BookRepository(this);

        // Kết nối với giao diện người dùng
        listViewAuthors = findViewById(R.id.listViewAuthors);
        buttonAddAuthor = findViewById(R.id.buttonAddAuthor);

        // Hiển thị danh sách các tác giả
        loadAuthors();

        // Sự kiện click vào nút Thêm tác giả
        buttonAddAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến activity thêm tác giả
                Intent intent = new Intent(QLAuthorActivity.this, AddAuthorActivity.class);
                startActivity(intent);
            }
        });

        // Sự kiện giữ vào một tác giả trong ListView để hiển thị các tùy chọn (Sửa/Xóa)
        listViewAuthors.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parentView, View view, int position, long id) {
                final String authorName = (String) parentView.getItemAtPosition(position);

                // Hiển thị hộp thoại để người dùng chọn Sửa hoặc Xóa
                showOptionsDialog(authorName);

                return true; // Đã xử lý sự kiện
            }
        });
    }

    // Phương thức để tải danh sách tác giả
    private void loadAuthors() {
        List<String> authors = bookRepository.getAllAuthors();
        authorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, authors);
        listViewAuthors.setAdapter(authorAdapter);
    }

    // Hiển thị hộp thoại tùy chọn (Sửa hoặc Xóa)
    private void showOptionsDialog(final String authorName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("lựa chọn");
        builder.setItems(new String[]{"sửa", "xóa"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Chọn "Edit" - chuyển đến EditAuthorActivity
                    Intent intent = new Intent(QLAuthorActivity.this, EditAuthorActivity.class);
                    intent.putExtra("authorName", authorName);
                    startActivity(intent);
                } else if (which == 1) {
                    // Chọn "Delete" - thực hiện xóa tác giả
                    deleteAuthor(authorName);
                }
            }
        });

        builder.show();
    }

    // Phương thức xóa tác giả
    private void deleteAuthor(final String authorName) {
        // Xóa tác giả trong cơ sở dữ liệu
        boolean isDeleted = bookRepository.deleteAuthor(authorName);
        if (isDeleted) {
            showMessage("xóa tác giả thành công");
            loadAuthors(); // Tải lại danh sách tác giả
        } else {
            showMessage("xóa tác giả thất bại");
        }
    }

    // Hàm hiển thị thông báo
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại danh sách tác giả khi quay lại Activity
        loadAuthors();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đóng kết nối cơ sở dữ liệu khi Activity bị hủy
        bookRepository.close();
    }
}
