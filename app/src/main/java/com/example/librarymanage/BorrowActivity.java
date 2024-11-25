package com.example.librarymanage;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.data.BorrowRecordRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BorrowActivity extends AppCompatActivity {

    private TextView tvBookTitle, tvBorrowDate, tvReturnDate;
    private Button btnSelectReturnDate, btnBorrow;
    private EditText etReturnDate;
    private BorrowRecordRepository borrowRecordRepository;

    private String bookTitle;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);

        tvBookTitle = findViewById(R.id.tvBookTitle);
        tvBorrowDate = findViewById(R.id.tvBorrowDate);
        btnSelectReturnDate = findViewById(R.id.btnSelectReturnDate);
        btnBorrow = findViewById(R.id.btnBorrow);
        etReturnDate = findViewById(R.id.etReturnDate);

        // Nhận thông tin từ Intent
        bookId = getIntent().getIntExtra("bookId", -1);
        bookTitle = getIntent().getStringExtra("bookTitle");

        // Thiết lập tiêu đề sách
        tvBookTitle.setText(bookTitle);

        // Hiển thị ngày mượn
        String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        tvBorrowDate.setText("Ngày mượn: " + currentDate);

        // Khởi tạo BorrowRecordRepository
        borrowRecordRepository = new BorrowRecordRepository(this);

        // Sự kiện khi nhấn nút chọn ngày trả
        btnSelectReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiện một hộp thoại để chọn ngày trả
                showDatePicker();
            }
        });

        // Sự kiện khi nhấn nút mượn sách
        btnBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBorrowConfirmationDialog();
            }
        });
    }

    private void showDatePicker() {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    String selectedDate = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etReturnDate.setText(selectedDate);
                },
                year, month, day);

        // Đặt ngày tối thiểu là hôm nay
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }


    private void showBorrowConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận mượn sách")
                .setMessage("Bạn có chắc chắn muốn mượn sách này không?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrowBook();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void borrowBook() {
        // Lấy userId từ SharedPreferences
        int userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("user_id", -1);
        if (userId == -1) {
            Toast.makeText(this, "Bạn cần đăng nhập để mượn sách!", Toast.LENGTH_SHORT).show();
            return;
        }

        String returnDate = etReturnDate.getText().toString();
        String borrowDate = tvBorrowDate.getText().toString().split(": ")[1]; // Lấy ngày mượn từ TextView

        // Lưu vào bảng BorrowRecords
        boolean success = borrowRecordRepository.borrowBook(userId, bookId, borrowDate, returnDate, "Đang Mượn");

        if (success) {
            Toast.makeText(this, "Mượn sách thành công!", Toast.LENGTH_SHORT).show();
            finish(); // Đóng Activity sau khi mượn
        } else {
            Toast.makeText(this, "Có lỗi xảy ra khi mượn sách.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        borrowRecordRepository.close(); // Đóng BorrowRecordRepository khi không còn sử dụng
        super.onDestroy();
    }
}
