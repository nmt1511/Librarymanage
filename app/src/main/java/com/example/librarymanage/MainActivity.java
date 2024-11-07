package com.example.librarymanage;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.librarymanage.admin.AdminFragment;
import com.example.librarymanage.data.DataBook;
import com.example.librarymanage.menubottom.DanhMucFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    private SQLiteDatabase db;
    DataBook dbBook;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbBook = new DataBook(this);
        dbBook.getWritableDatabase();

        // Gán view cho DrawerLayout, Toolbar và BottomNavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Thiết lập Toolbar
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Load fragment đầu tiên nếu chưa có
        if (savedInstanceState == null) {
            replaceFragment(getFragmentForRole(new HomeFragment()));  // Mặc định là HomeFragment
            navigationView.setCheckedItem(R.id.nav_home);
        }

        // Thay thế Fragment theo lựa chọn của NavigationView
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_settings) {
                replaceFragment(getFragmentForRole(new LichSuFragment()));
            } else if (item.getItemId() == R.id.nav_share) {
                replaceFragment(getFragmentForRole(new LichSuFragment()));
            } else if (item.getItemId() == R.id.nav_about) {
                replaceFragment(getFragmentForRole(new LichSuFragment()));
            } else if (item.getItemId() == R.id.nav_logout) {
                // Xử lý logout
                return true;
            } else if (item.getItemId() == R.id.nav_home) {
                replaceFragment(getFragmentForRole(new HomeFragment()));
            }
            return true;
        });

        // Thay thế Fragment theo lựa chọn của BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(getFragmentForRole(new HomeFragment()));
            } else if (item.getItemId() == R.id.noti) {
                replaceFragment(getFragmentForRole(new DanhMucFragment()));
            } else if (item.getItemId() == R.id.history) {
                replaceFragment(getFragmentForRole(new LichSuFragment()));
            } else if (item.getItemId() == R.id.account) {
                replaceFragment(getFragmentForRole(new LichSuFragment()));
            }
            return true;
        });
    }

    // Hàm thay thế Fragment, kiểm tra vai trò người dùng và thay đổi Fragment phù hợp
    private Fragment getFragmentForRole(Fragment fragment) {
        if (isUserAdmin()) {
            // Nếu là admin, có thể thay đổi fragment cho admin (nếu cần thiết)
            return new AdminFragment(); // Thay fragment cho admin nếu cần
        } else {
            // Nếu là user, giữ nguyên fragment gốc
            return fragment;
        }
    }

    // Kiểm tra vai trò của người dùng từ SharedPreferences
    private boolean isUserAdmin() {
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int role = preferences.getInt("role", -1); // Lấy role từ SharedPreferences, mặc định là -1 (user)
        return role == 1; // Nếu role == 1 thì là admin
    }

    // Hàm thay thế Fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }
}
