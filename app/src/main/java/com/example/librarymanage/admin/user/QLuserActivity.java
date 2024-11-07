package com.example.librarymanage.admin.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanage.R;
import com.example.librarymanage.adapter.UserAdapter;
import com.example.librarymanage.data.UserRepository;
import com.example.librarymanage.entities.User;

import java.util.ArrayList;

public class QLuserActivity extends AppCompatActivity {

    private ListView listViewUsers;
    private Button btnAddUser;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_user);

        listViewUsers = findViewById(R.id.listview_users);
        btnAddUser = findViewById(R.id.btn_add_user); // Nút Thêm
        userRepository = new UserRepository(this);

        // Lấy danh sách người dùng từ cơ sở dữ liệu
        ArrayList<User> users = userRepository.getAllUsers();

        // Khởi tạo adapter và thiết lập cho ListView
        UserAdapter adapter = new UserAdapter(this, users);
        listViewUsers.setAdapter(adapter);

        // Xử lý sự kiện khi nhấn vào một người dùng trong danh sách
        listViewUsers.setOnItemClickListener((parent, view, position, id) -> {
            User selectedUser = (User) parent.getItemAtPosition(position);
            showPopupMenu(view, selectedUser);  // Hiển thị PopupMenu khi người dùng được chọn
        });

        // Xử lý sự kiện khi nhấn vào nút "Thêm người dùng"
        btnAddUser.setOnClickListener(v -> {
            // Mở màn hình thêm người dùng
            Intent intent = new Intent(QLuserActivity.this, AddUserActivity.class);
            startActivity(intent);
        });
    }

    private void showPopupMenu(View view, User user) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        // Inflate menu
        popupMenu.getMenuInflater().inflate(R.menu.menu_user, popupMenu.getMenu());

        // Xử lý sự kiện chọn menu
        popupMenu.setOnMenuItemClickListener(item -> {
            // Sử dụng if-else để xử lý menu item
            if (item.getItemId() == R.id.menu_edit) {
                // Xử lý sửa thông tin người dùng
                editUser(user);
                return true;
            } else if (item.getItemId() == R.id.menu_delete) {
                // Xử lý xóa người dùng
                deleteUser(user);
                return true;
            } else {
                return false;
            }
        });

        popupMenu.show();  // Hiển thị menu
    }

    // Xử lý sửa người dùng
    private void editUser(User user) {
        // Mở một Activity hoặc Dialog để sửa thông tin người dùng
        Toast.makeText(this, "Chỉnh sửa người dùng: " + user.getName(), Toast.LENGTH_SHORT).show();
        // Ví dụ: mở màn hình sửa thông tin người dùng
        Intent intent = new Intent(QLuserActivity.this, EditUserActivity.class);
        intent.putExtra("userId", user.getUserId());
        startActivity(intent);
    }

    // Xử lý xóa người dùng
    private void deleteUser(User user) {
        boolean result = userRepository.deleteUser(user.getUserId());
        if (result) {
            Toast.makeText(this, "Xóa người dùng thành công", Toast.LENGTH_SHORT).show();
            // Cập nhật lại danh sách người dùng sau khi xóa
            ArrayList<User> users = userRepository.getAllUsers();
            UserAdapter adapter = new UserAdapter(this, users);
            listViewUsers.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Xóa người dùng thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
