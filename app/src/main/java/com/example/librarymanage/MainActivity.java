package com.example.librarymanage;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.annotation.SuppressLint;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.librarymanage.data.DataBook;
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

//         Load fragment đầu tiên nếu chưa có
        if (savedInstanceState == null) {
            //Hiện Map đầu tiên
            replaceFragment(new HomeFragment());
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        //Thay thế Fragment theo lựa chọn của NavigationView
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_settings) {
                replaceFragment(new HomeFragment());
            }else if(item.getItemId() == R.id.nav_share){
                replaceFragment(new HomeFragment());
            }else if(item.getItemId() == R.id.nav_about){
                replaceFragment(new HomeFragment());
            }else if(item.getItemId() == R.id.nav_logout){
//                LogoutActivity.showExitConfirmationDialog(MainActivity.this);
                return true;
            }
            else if(item.getItemId() == R.id.nav_home){
                replaceFragment(new HomeFragment());
            }

            return true;
        });

        // Thay thế Fragment theo lựa chọn của BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home)
            {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.noti) {
                replaceFragment(new HomeFragment());
            }else if (item.getItemId() == R.id.history) {
                replaceFragment(new HomeFragment());
            }else if (item.getItemId() == R.id.account) {
                replaceFragment(new HomeFragment());
            }
            return true;
        });
    }

    // Hàm thay thế Fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }


}
