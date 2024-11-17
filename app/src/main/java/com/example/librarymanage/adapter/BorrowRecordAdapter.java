package com.example.librarymanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanage.R;
import com.example.librarymanage.entities.BorrowRecord2;

import java.util.List;

public class BorrowRecordAdapter extends RecyclerView.Adapter<BorrowRecordAdapter.BorrowRecordViewHolder> {
    private Context context;
    private List<BorrowRecord2> borrowRecordList;

    public BorrowRecordAdapter(Context context, List<BorrowRecord2> borrowRecordList) {
        this.context = context;
        this.borrowRecordList = borrowRecordList;
    }

    @NonNull
    @Override
    public BorrowRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Đảm bảo bạn sử dụng layout item_borrow_record2 hoặc một layout tương ứng để hiển thị thông tin lịch sử mượn
        View view = LayoutInflater.from(context).inflate(R.layout.item_borrow_record2, parent, false);
        return new BorrowRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowRecordViewHolder holder, int position) {
        BorrowRecord2 record = borrowRecordList.get(position);

        // Thiết lập dữ liệu cho các TextView
        holder.textViewUserName.setText("Tên người mượn: " + record.getUserName());
        holder.textViewBookTitle.setText("Tên sách: " + record.getBookTitle());
        holder.textViewBorrowDate.setText("Ngày mượn: " + record.getBorrowDate());
        holder.textViewReturnDate.setText("Ngày trả: " + record.getReturnDate());
        holder.textViewStatus.setText("Tình trạng: " + record.getStatus());
    }

    @Override
    public int getItemCount() {
        return borrowRecordList.size();
    }

    public static class BorrowRecordViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserName, textViewBookTitle, textViewBorrowDate, textViewReturnDate, textViewStatus;

        public BorrowRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewBookTitle = itemView.findViewById(R.id.textViewBookTitle);
            textViewBorrowDate = itemView.findViewById(R.id.textViewBorrowDate);
            textViewReturnDate = itemView.findViewById(R.id.textViewReturnDate);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
        }
    }
}
