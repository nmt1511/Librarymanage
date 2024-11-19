package com.example.librarymanage.menubottom;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanage.R;
import com.example.librarymanage.adapter.BorrowRecordAdapter;
import com.example.librarymanage.data.BorrowRecordRepository;
import com.example.librarymanage.entities.BorrowRecord2;

import java.util.List;

public class LichSuFragment extends Fragment {

    private RecyclerView recyclerView;
    private BorrowRecordRepository borrowRecordRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lich_su, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewBorrowHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId != -1) {
            borrowRecordRepository = new BorrowRecordRepository(getContext());

            // Gọi phương thức tự động cập nhật trạng thái "Quá Hạn"
            try {
                borrowRecordRepository.updateOverdueRecords(userId);
            } catch (Exception e) {
                Log.e("BorrowRecordRepo", "Error updating overdue records", e);
            }


            // Lấy danh sách bản ghi mượn sách đã cập nhật
            List<BorrowRecord2> borrowRecords = borrowRecordRepository.getBorrowRecordsByUserId(userId);

            // Tạo adapter và gán cho recyclerView
            BorrowRecordAdapter adapter = new BorrowRecordAdapter(getContext(), borrowRecords);
            recyclerView.setAdapter(adapter);

        } else {
            Toast.makeText(getContext(), "Bạn cần đăng nhập để xem lịch sử mượn sách", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (borrowRecordRepository != null) {
            borrowRecordRepository.close();
        }
    }
}
