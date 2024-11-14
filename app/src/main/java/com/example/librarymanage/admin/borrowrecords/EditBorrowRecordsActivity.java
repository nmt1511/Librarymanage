package com.example.librarymanage.admin.borrowrecords;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.data.BorrowRecordRepository;
import com.example.librarymanage.entities.BorrowRecord2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditBorrowRecordsActivity extends AppCompatActivity {
    private EditText etBorrowDate, etReturnDate;
    private Spinner spinnerStatus;
    private BorrowRecordRepository borrowRecordRepository;
    private int recordId;
    private BorrowRecord2 currentRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_borrow_records);

        // Ánh xạ các view
        etBorrowDate = findViewById(R.id.etBorrowDate);
        etReturnDate = findViewById(R.id.etReturnDate);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnBorrowDatePicker = findViewById(R.id.btnBorrowDatePicker);
        Button btnReturnDatePicker = findViewById(R.id.btnReturnDatePicker);

        // Khởi tạo repository
        borrowRecordRepository = new BorrowRecordRepository(this);

        // Lấy ID bản ghi từ Intent
        recordId = getIntent().getIntExtra("recordId", -1);

        // Thiết lập Spinner với dữ liệu từ string-array
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.status_array,
                android.R.layout.simple_spinner_item
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        // Nếu có ID bản ghi, load thông tin
        if (recordId != -1) {
            loadRecordDetails();
        }

        // Xử lý sự kiện chọn ngày
        btnBorrowDatePicker.setOnClickListener(v -> showBorrowDatePicker());
        btnReturnDatePicker.setOnClickListener(v -> showReturnDatePicker());

        // Xử lý sự kiện lưu
        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void showBorrowDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    etBorrowDate.setText(selectedDate);
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showReturnDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    etReturnDate.setText(selectedDate);
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void loadRecordDetails() {
        // Lấy chi tiết bản ghi từ repository
        currentRecord = borrowRecordRepository.getBorrowRecordById(recordId);

        if (currentRecord != null) {
            // Điền thông tin vào các trường
            etBorrowDate.setText(currentRecord.getBorrowDate());
            etReturnDate.setText(currentRecord.getReturnDate());

            // Đặt trạng thái cho Spinner
            String[] statusArray = getResources().getStringArray(R.array.status_array);
            for (int i = 0; i < statusArray.length; i++) {
                if (statusArray[i].equals(currentRecord.getStatus())) {
                    spinnerStatus.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveChanges() {
        // Lấy giá trị từ các trường
        String borrowDate = etBorrowDate.getText().toString().trim();
        String returnDate = etReturnDate.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString();

        // Nếu không nhập ngày thì giữ nguyên giá trị cũ
        if (borrowDate.isEmpty()) {
            borrowDate = currentRecord.getBorrowDate();
        }
        if (returnDate.isEmpty()) {
            returnDate = currentRecord.getReturnDate();
        }

        // Cập nhật bản ghi
        boolean updateResult = borrowRecordRepository.updateBorrowRecord(
                recordId,
                borrowDate,
                returnDate,
                status
        );

        if (updateResult) {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();

            // Tạo Intent để trả về kết quả và reload lại activity trước
            Intent resultIntent = new Intent();
            resultIntent.putExtra("needReload", true);
            setResult(RESULT_OK, resultIntent);

            finish();
        } else {
            Toast.makeText(this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
        }
    }
}