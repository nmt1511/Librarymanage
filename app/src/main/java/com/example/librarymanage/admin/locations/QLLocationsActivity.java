package com.example.librarymanage.admin.locations;

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
import com.example.librarymanage.admin.locations.AddLocationActivity;
import com.example.librarymanage.admin.locations.EditLocationActivity;
import com.example.librarymanage.data.BookRepository;

import java.util.List;

public class QLLocationsActivity extends AppCompatActivity {

    private BookRepository bookRepository;
    private ListView listViewLocations;
    private Button buttonAddLocation;
    private ArrayAdapter<String> locationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_locations);

        // Khởi tạo BookRepository để tương tác với cơ sở dữ liệu
        bookRepository = new BookRepository(this);

        // Kết nối với giao diện người dùng
        listViewLocations = findViewById(R.id.listViewLocations);
        buttonAddLocation = findViewById(R.id.buttonAddLocation);

        // Hiển thị danh sách các địa điểm
        loadLocations();

        // Sự kiện click vào nút Thêm địa điểm
        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QLLocationsActivity.this, AddLocationActivity.class);
                startActivity(intent);
            }
        });


        // Sự kiện giữ vào một địa điểm trong ListView để hiển thị các tùy chọn (Sửa/Xóa)
        listViewLocations.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parentView, View view, int position, long id) {
                final String locationName = (String) parentView.getItemAtPosition(position);

                // Hiển thị hộp thoại để người dùng chọn Sửa hoặc Xóa
                showOptionsDialog(locationName);

                return true; // Đã xử lý sự kiện
            }
        });
    }

    // Phương thức để tải danh sách địa điểm
    private void loadLocations() {
        List<String> locations = bookRepository.getAllLocations();
        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locations);
        listViewLocations.setAdapter(locationAdapter);
    }

    // Hiển thị hộp thoại tùy chọn (Sửa hoặc Xóa)
    private void showOptionsDialog(final String locationName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Action");
        builder.setItems(new String[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Chọn "Edit" - chuyển đến EditLocationActivity
                    Intent intent = new Intent(QLLocationsActivity.this, EditLocationActivity.class);
                    intent.putExtra("locationName", locationName);
                    startActivity(intent);
                } else if (which == 1) {
                    // Chọn "Delete" - thực hiện xóa địa điểm
                    deleteLocation(locationName);
                }
            }
        });

        builder.show();
    }

    // Phương thức xóa địa điểm
    private void deleteLocation(final String locationName) {
        // Xóa địa điểm trong cơ sở dữ liệu
        boolean isDeleted = bookRepository.deleteLocation(locationName);
        if (isDeleted) {
            showMessage("Location deleted successfully");
            loadLocations(); // Tải lại danh sách địa điểm
        } else {
            showMessage("Failed to delete location");
        }
    }

    // Hàm hiển thị thông báo
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại danh sách địa điểm khi quay lại Activity
        loadLocations();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đóng kết nối cơ sở dữ liệu khi Activity bị hủy
        bookRepository.close();
    }
}
