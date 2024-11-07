package com.example.librarymanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.librarymanage.R;
import com.example.librarymanage.entities.User;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {

    private Context context;
    private ArrayList<User> users;

    public UserAdapter(Context context, ArrayList<User> users) {
        super(context, R.layout.item_user, users); // layout của mỗi item
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // Tạo mới layout item cho mỗi người dùng
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_user, parent, false);
        }

        // Lấy đối tượng User tại vị trí hiện tại
        User user = users.get(position);

        // Lấy các TextView trong layout item để hiển thị thông tin
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvEmail = convertView.findViewById(R.id.tvEmail);

        // Thiết lập dữ liệu cho các TextView
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());

        return convertView;
    }
}
