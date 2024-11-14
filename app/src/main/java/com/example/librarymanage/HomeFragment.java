package com.example.librarymanage;

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
import android.widget.Toast;

import com.example.librarymanage.data.DataBook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.librarymanage.menubottom.DanhMucFragment;
import com.example.librarymanage.NewBookFragment;
import com.example.librarymanage.user.UserInfoActivity;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private LinearLayout btnCategories;
    private LinearLayout btnAbout;
    private LinearLayout btnInfo;
    TextView txtGreeting;
    private LinearLayout btnsearch;
    SQLiteDatabase db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the buttons
//        btnCategories = view.findViewById(R.id.btn_categories);
        btnAbout = view.findViewById(R.id.btn_about);
        btnInfo = view.findViewById(R.id.btn_info);
        txtGreeting = view.findViewById(R.id.greetingText);
        btnsearch = view.findViewById(R.id.btn_search);
        // Kiểm tra thời gian và hiển thị thông điệp chào
        showGreeting();



        // Show NewBookFragment by default
        if (savedInstanceState == null) {
            NewBookFragment newBookFragment = new NewBookFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.sachmoi, newBookFragment); // Assuming R.id.sachmoi is the correct container ID
            transaction.commit();
        }

//        // Set up click listener for btnCategories
//        btnCategories.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openDanhMucFragment();
//            }
//        });

        // Set up click listener for btnAbout
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBookActivity();
            }
        });
        btnsearch.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v){
            openbtn_search();
        }
                                      });
        // Set up click listener for btnInfo
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserInfoActivity();
            }
        });

        return view;
    }

    private void openDanhMucFragment() {
        // Logic to open DanhMucFragment
    }
private void openbtn_search(){
    Intent intent = new Intent(getActivity(), SearchActivity.class);
    startActivity(intent);
}
    private void openBookActivity() {
        // Start BookActivity
        Intent intent = new Intent(getActivity(), BookActivity.class);
        startActivity(intent);
    }

    private void openUserInfoActivity() {
        // Start UserInfoActivity
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        startActivity(intent);
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
        txtGreeting.setText(greeting+"\n"+ welcome);
    }
}
