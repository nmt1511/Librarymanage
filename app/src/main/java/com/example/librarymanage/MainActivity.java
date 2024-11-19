package com.example.librarymanage;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.librarymanage.admin.book.QLBookActivity;
import com.example.librarymanage.admin.borrowrecords.QLBorrowRecordsActivity;
import com.example.librarymanage.data.DataBook;
import com.example.librarymanage.menubottom.DanhMucFragment;
import com.example.librarymanage.menubottom.LichSuFragment;
import com.example.librarymanage.user.dangnhap;
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

        // Cập nhật menu trong BottomNavigationView dựa trên vai trò người dùng
        updateBottomNavigationMenu(bottomNavigationView);

        // Load fragment đầu tiên nếu chưa có
        if (savedInstanceState == null) {
            replaceFragment(getFragmentForRole(new HomeFragment()));  // Mặc định là HomeFragment
            navigationView.setCheckedItem(R.id.nav_home);
        }

        // Thay thế Fragment theo lựa chọn của NavigationView
        navigationView.setNavigationItemSelectedListener(item -> {
          if (item.getItemId() == R.id.nav_about) {
                replaceFragment(getFragmentForRole(new ThongtinFragment()));
            } else if (item.getItemId() == R.id.nav_logout) {
              handleLogout();
                return true;
            } else if (item.getItemId() == R.id.nav_home) {
                replaceFragment(getFragmentForRole(new HomeFragment()));
            }
            return true;
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            // Kiểm tra người dùng là admin hay không
            if (isUserAdmin()) {
                // Nếu là admin, mở các Activity phù hợp
                if (item.getItemId() == R.id.home) {
                    replaceFragment(new AdminFragment());  // Fragment dành cho admin
                } else if (item.getItemId() == R.id.nav_history) {
                    Intent intent = new Intent(MainActivity.this, QLBorrowRecordsActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.nav_book) {
                    Intent intent = new Intent(MainActivity.this, QLBookActivity.class);
                    startActivity(intent);
                }
            } else {
                // Nếu là user, thay đổi layout/phương thức cho user
                if (item.getItemId() == R.id.home) {
                    replaceFragment(new HomeFragment());  // Fragment cho người dùng bình thường
                } else if (item.getItemId() == R.id.noti) {
                    replaceFragment(new DanhMucFragment());  // Fragment cho người dùng bình thường
                } else if (item.getItemId() == R.id.history) {
                    replaceFragment(new LichSuFragment());  // Fragment cho người dùng bình thường
                }
            }
            return true;
        });

    }

    // Cập nhật menu trong BottomNavigationView dựa trên vai trò người dùng
    private void updateBottomNavigationMenu(BottomNavigationView bottomNavigationView) {
        if (isUserAdmin()) {
            // Nếu là admin, chỉ hiển thị một số mục cần thiết (Home, Noti, History, v.v.)
            bottomNavigationView.getMenu().clear();  // Xóa menu hiện tại
            bottomNavigationView.inflateMenu(R.menu.menu_admin);  // Inflating menu dành cho admin
        } else {
            // Nếu là user, hiển thị đầy đủ menu
            bottomNavigationView.getMenu().clear();  // Xóa menu hiện tại
            bottomNavigationView.inflateMenu(R.menu.bottom_menu);  // Inflating menu đầy đủ cho user
        }
    }

    // Kiểm tra vai trò của người dùng từ SharedPreferences
    private boolean isUserAdmin() {
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int role = preferences.getInt("role", -1);  // Lấy role từ SharedPreferences, mặc định là -1 (user)
        return role == 1;  // Nếu role == 1 thì là admin
    }

    // Hàm thay thế Fragment, kiểm tra vai trò người dùng và thay đổi Fragment phù hợp
    private Fragment getFragmentForRole(Fragment fragment) {
        if (isUserAdmin()) {
            // Nếu là admin, thay đổi fragment cho admin nếu cần
            return new AdminFragment();  // Thay fragment cho admin
        } else {
            // Nếu là user, giữ nguyên fragment gốc
            return fragment;
        }
    }
    private void handleLogout() {
        // Xóa dữ liệu trong SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // Xóa toàn bộ dữ liệu trong SharedPreferences
        editor.apply();  // Lưu lại các thay đổi

        // Chuyển hướng đến màn hình đăng nhập (LoginActivity)
        Intent intent = new Intent(MainActivity.this, dangnhap.class);
        startActivity(intent);
        finish(); // Kết thúc MainActivity để không quay lại được màn hình chính sau khi logout
    }

    // Hàm thay thế Fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }
}
