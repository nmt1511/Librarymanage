package com.example.librarymanage.admin;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.librarymanage.R;
import com.example.librarymanage.admin.book.QLBookActivity;
import com.example.librarymanage.admin.QLRatingActivity;
import com.example.librarymanage.admin.borrowrecords.QLBorrowRecordsActivity;
import com.example.librarymanage.admin.locations.QLLocationsActivity;
import com.example.librarymanage.admin.user.QLuserActivity;
import com.example.librarymanage.data.DataBook;
import com.example.librarymanage.user.UserInfoActivity;

import java.util.Calendar;

public class AdminFragment extends Fragment {

    private LinearLayout btnCategories;
    private LinearLayout btnbook;
    private LinearLayout btnInfo;
    TextView txtGreeting;
    private LinearLayout btnuser;
    private LinearLayout btn_author;
    private LinearLayout btn_review;
    SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        // Initialize the buttons
        btnCategories = view.findViewById(R.id.btn_categories);
        btnbook = view.findViewById(R.id.btn_book);
        btnInfo = view.findViewById(R.id.btn_info);
        txtGreeting = view.findViewById(R.id.greetingText);
        btnuser = view.findViewById(R.id.btn_user);
        btn_author = view.findViewById(R.id.btn_author);
        btn_review = view.findViewById(R.id.btn_review);
        // Kiểm tra thời gian và hiển thị thông điệp chào
        showGreeting();

        // Set up click listener for btnCategories
        btn_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QLAuthorActivity.class);
                startActivity(intent);
            }
        });
        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QLLocationsActivity.class);
                startActivity(intent);
            }
        });
        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QlCategoryActivity.class);
                startActivity(intent);
            }
        });

        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QLBookActivity.class);
                startActivity(intent);
            }
        });

        btnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QLuserActivity.class);
                startActivity(intent); // Mở QLUserActivity
            }
        });

        // Set up click listener for btnInfo
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QLBorrowRecordsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    private void showGreeting() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);
        String welcome = "";

        if (userId != -1) {
            DataBook helper = new DataBook(getContext());
            SQLiteDatabase db = helper.getReadableDatabase();

            Cursor c = db.rawQuery("SELECT * from User Where user_id = ?", new String[]{String.valueOf(userId)});
            if (c != null && c.moveToFirst()) {
                int nameIndex = c.getColumnIndex("name");
                if (nameIndex != -1) {
                    welcome = c.getString(nameIndex);
                }
            }
            if (c != null) {
                c.close(); // Đóng cursor sau khi sử dụng
            }
        }

        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        // Xác định thông điệp chào dựa trên giờ hiện tại
        String greeting;
        if (hour >= 5 && hour < 12) {
            greeting = "Chào buổi sáng, ";
        } else if (hour >= 12 && hour < 18) {
            greeting = "Chào buổi chiều, ";
        } else {
            greeting = "Chào buổi tối, ";
        }

        // Cập nhật TextView
        txtGreeting.setText(greeting + "\n" + welcome);
    }
}
