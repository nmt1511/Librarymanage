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

import java.util.ArrayList;
import java.util.List;

// Trong BorrowRecordAdapter2
public class BorrowRecordAdapter2 extends RecyclerView.Adapter<BorrowRecordAdapter2.BorrowRecordViewHolder> {
    private Context context;
    private List<BorrowRecord2> borrowRecordList;
    private OnItemLongClickListener longClickListener;

    public BorrowRecordAdapter2(Context context, List<BorrowRecord2> borrowRecordList) {
        this.context = context;
        this.borrowRecordList = borrowRecordList;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(BorrowRecord2 record);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public BorrowRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_borrow_record2, parent, false);
        return new BorrowRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowRecordViewHolder holder, int position) {
        BorrowRecord2 record = borrowRecordList.get(position);

        // Thiết lập dữ liệu cho các TextView
        holder.textViewUserName.setText(record.getUserName());
        holder.textViewBookTitle.setText(record.getBookTitle());
        holder.textViewBorrowDate.setText("Ngày mượn: " + record.getBorrowDate());
        holder.textViewReturnDate.setText("Ngày Trả: " + record.getReturnDate());
//        holder.textViewActualReturnDate.setText("Actual Return: " + record.getActualReturnDate());
        holder.textViewStatus.setText("Tình Trạng: " + record.getStatus());

        // Xử lý sự kiện khi nhấn giữ item
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(record);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return borrowRecordList.size();
    }

    public static class BorrowRecordViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserName, textViewBookTitle, textViewBorrowDate, textViewReturnDate, textViewActualReturnDate, textViewStatus;

        public BorrowRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewBookTitle = itemView.findViewById(R.id.textViewBookTitle);
            textViewBorrowDate = itemView.findViewById(R.id.textViewBorrowDate);
            textViewReturnDate = itemView.findViewById(R.id.textViewReturnDate);
//            textViewActualReturnDate = itemView.findViewById(R.id.textViewActualReturnDate);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
        }
    }
}
