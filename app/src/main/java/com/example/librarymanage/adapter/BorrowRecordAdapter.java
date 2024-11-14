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

public class BorrowRecordAdapter extends RecyclerView.Adapter<BorrowRecordAdapter.ViewHolder> {

    private List<BorrowRecord> borrowRecords;
    private OnItemLongClickListener longClickListener;

    public BorrowRecordAdapter(List<BorrowRecord> borrowRecords) {
        this.borrowRecords = borrowRecords;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_borrow_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BorrowRecord record = borrowRecords.get(position);
        holder.bookTitleTextView.setText(record.getTitle());
        holder.borrowDateTextView.setText(record.getBorrowDate());
        holder.returnDateTextView.setText(record.getReturnDate());
        holder.statusTextView.setText(record.getStatus());

        // Thiết lập sự kiện nhấn giữ
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(record);
            }
            return true; // Trả về true để cho biết sự kiện đã được xử lý
        });
    }

    @Override
    public int getItemCount() {
        return borrowRecords.size();
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(BorrowRecord record);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitleTextView;
        TextView borrowDateTextView;
        TextView returnDateTextView;
        TextView statusTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            bookTitleTextView = itemView.findViewById(R.id.tvBookTitle);
            borrowDateTextView = itemView.findViewById(R.id.tvBorrowDate);
            returnDateTextView = itemView.findViewById(R.id.tvReturnDate);
            statusTextView = itemView.findViewById(R.id.tvStatus);
        }
    }
}