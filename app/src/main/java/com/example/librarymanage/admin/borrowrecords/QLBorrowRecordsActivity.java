package com.example.librarymanage.admin.borrowrecords;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanage.R;
import com.example.librarymanage.adapter.BorrowRecordAdapter2;
import com.example.librarymanage.data.BorrowRecordRepository;
import com.example.librarymanage.entities.BorrowRecord2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QLBorrowRecordsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BorrowRecordAdapter2 adapter;
    private BorrowRecordRepository borrowRecordRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_borrow_records);

        recyclerView = findViewById(R.id.recyclerView);
        borrowRecordRepository = new BorrowRecordRepository(this);

        loadBorrowRecords();
    }

    private void loadBorrowRecords() {
        List<BorrowRecord2> borrowRecordList = borrowRecordRepository.getAllBorrowRecordsWithDetails();
        Collections.sort(borrowRecordList, new Comparator<BorrowRecord2>() {
            @Override
            public int compare(BorrowRecord2 o1, BorrowRecord2 o2) {
                return o2.getBorrowDate().compareTo(o1.getBorrowDate()); // Sắp xếp theo ngày mới nhất
            }
        });

        // Chuyển đổi List thành ArrayList trước khi truyền vào adapter
        adapter = new BorrowRecordAdapter2(this, new ArrayList<>(borrowRecordList));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Kiểm tra xem adapter có phương thức setOnItemLongClickListener không
        adapter.setOnItemLongClickListener(new BorrowRecordAdapter2.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(BorrowRecord2 record) {
                showOptionsDialog(record);
            }
        });
    }

    private void showOptionsDialog(BorrowRecord2 record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn hành động")
                .setItems(new CharSequence[]{"Sửa", "Xóa"}, (dialog, which) -> {
                    if (which == 0) {
                        // Sửa bản ghi
                        Intent intent = new Intent(QLBorrowRecordsActivity.this, EditBorrowRecordsActivity.class);
                        intent.putExtra("recordId", record.getRecordId());
                        startActivityForResult(intent, REQUEST_CODE_EDIT); // Sử dụng startActivityForResult
                    } else if (which == 1) {
                        // Xóa bản ghi
                        showDeleteConfirmation(record);
                    }
                })
                .show();
    }

    // Thêm hằng số request code
    private static final int REQUEST_CODE_EDIT = 1001;

    // Thêm phương thức onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra("needReload", false)) {
                // Reload lại danh sách bản ghi
                loadBorrowRecords();
            }
        }
    }

    private void showDeleteConfirmation(BorrowRecord2 record) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa bản ghi")
                .setMessage("Bạn có chắc chắn muốn xóa bản ghi này?")
                .setPositiveButton("Có", (dialog, which) -> {
                    if (borrowRecordRepository.deleteBorrowRecord(record.getRecordId())) {
                        Toast.makeText(this, "Đã xóa bản ghi", Toast.LENGTH_SHORT).show();
                        loadBorrowRecords(); // Tải lại danh sách
                    } else {
                        Toast.makeText(this, "Xóa không thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }
}
