// BorrowRecordAdapter.java
package com.example.librarymanage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.librarymanage.R;
import com.example.librarymanage.entities.BorrowRecord;
import java.util.List;

public class BorrowRecordAdapter extends RecyclerView.Adapter<BorrowRecordAdapter.BorrowRecordViewHolder> {

    private List<BorrowRecord> borrowRecordList;

    public BorrowRecordAdapter(List<BorrowRecord> borrowRecordList) {
        this.borrowRecordList = borrowRecordList;
    }

    @NonNull
    @Override
    public BorrowRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_borrow_record, parent, false);
        return new BorrowRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowRecordViewHolder holder, int position) {
        BorrowRecord record = borrowRecordList.get(position);
        holder.tvBookTitle.setText(record.getBookTitle());
        holder.tvBorrowDate.setText(record.getBorrowDate());
        holder.tvReturnDate.setText(record.getReturnDate());
        holder.tvStatus.setText(record.getStatus());
    }

    @Override
    public int getItemCount() {
        return borrowRecordList.size();
    }

    public static class BorrowRecordViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookTitle, tvBorrowDate, tvReturnDate, tvStatus;

        public BorrowRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvBorrowDate = itemView.findViewById(R.id.tvBorrowDate);
            tvReturnDate = itemView.findViewById(R.id.tvReturnDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
