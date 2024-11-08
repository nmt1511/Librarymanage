package com.example.librarymanage.admin.borrowrecords;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.BorrowRecordRepository;

import java.util.List;

public class QLBorrowRecordsActivity extends AppCompatActivity {

    private BorrowRecordRepository borrowRecordRepository;
    private ListView listViewBorrowRecords;
    private Button buttonAddBorrowRecord;
    private ArrayAdapter<String> borrowRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_borrow_records);

        // Khởi tạo BorrowRecordRepository để tương tác với cơ sở dữ liệu
        borrowRecordRepository = new BorrowRecordRepository(this);

        // Kết nối với giao diện người dùng
        listViewBorrowRecords = findViewById(R.id.listViewBorrowRecords);
        buttonAddBorrowRecord = findViewById(R.id.buttonAddBorrowRecord);

        // Hiển thị danh sách các bản ghi mượn
        loadBorrowRecords();

        // Sự kiện click vào một bản ghi mượn để chỉnh sửa hoặc xóa
        listViewBorrowRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
                String borrowRecordDetails = (String) parentView.getItemAtPosition(position);

                // Chuyển đến activity để xem chi tiết hoặc chỉnh sửa bản ghi mượn
                Intent intent = new Intent(QLBorrowRecordsActivity.this, EditBorrowRecordActivity.class);
                intent.putExtra("borrowRecordDetails", borrowRecordDetails);
                startActivity(intent);
            }
        });

        // Sự kiện click vào nút Thêm bản ghi mượn
        buttonAddBorrowRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến activity thêm bản ghi mượn
                Intent intent = new Intent(QLBorrowRecordsActivity.this, AddBorrowRecordActivity.class);
                startActivity(intent);
            }
        });
    }

    // Phương thức để tải danh sách các bản ghi mượn
    private void loadBorrowRecords() {
        List<String> borrowRecords = borrowRecordRepository.getAllBorrowRecords();
        borrowRecordAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, borrowRecords);
        listViewBorrowRecords.setAdapter(borrowRecordAdapter);
    }

    // Hàm hiển thị thông báo
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại danh sách bản ghi mượn khi quay lại Activity
        loadBorrowRecords();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đóng kết nối cơ sở dữ liệu khi Activity bị hủy
        borrowRecordRepository.close();
    }
}
