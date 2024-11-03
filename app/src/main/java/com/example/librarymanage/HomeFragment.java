package com.example.librarymanage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.librarymanage.menubottom.DanhMucFragment;
import com.example.librarymanage.user.UserInfoActivity;

public class HomeFragment extends Fragment {

    private Button btnCategories;
    private Button btnAbout;
    private Button btnInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the buttons
        btnCategories = view.findViewById(R.id.btn_categories);
        btnAbout = view.findViewById(R.id.btn_about);
        btnInfo = view.findViewById(R.id.btn_info);

        // Set up click listener for btnCategories
        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDanhMucFragment();
            }
        });

        // Set up click listener for btnAbout
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBookActivity();
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
        // Create an instance of DanhMucFragment
        DanhMucFragment danhMucFragment = new DanhMucFragment();

        // Replace the current fragment with DanhMucFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.danhmuc, danhMucFragment);
        transaction.addToBackStack(null);  // Add to back stack to enable back navigation
        transaction.commit();
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
}
