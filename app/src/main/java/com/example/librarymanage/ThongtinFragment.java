package com.example.librarymanage;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.librarymanage.R;

public class ThongtinFragment extends Fragment {

    private TextView txtLibraryName, txtDescription, txtAddress, txtContact;

    public ThongtinFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_thongtin, container, false);

        // Khởi tạo các TextView
        txtLibraryName = rootView.findViewById(R.id.txtLibraryName);
        txtDescription = rootView.findViewById(R.id.txtDescription);
        txtAddress = rootView.findViewById(R.id.txtAddress);
        txtContact = rootView.findViewById(R.id.txtContact);

        // Thiết lập dữ liệu cho các TextView
        setLibraryInformation();

        return rootView;
    }

    // Thiết lập thông tin giới thiệu thư viện
    private void setLibraryInformation() {
        // Bạn có thể thay đổi thông tin này tùy ý
        txtLibraryName.setText("Thư Viện Quản Lý");
        txtDescription.setText("Thư viện Quản Lý cung cấp một bộ sưu tập sách đa dạng phục vụ cho việc học tập và nghiên cứu.");
        txtAddress.setText("Địa chỉ: đường Trần Văn Ơn, Thành Phố Thủ Dầu Một");
        txtContact.setText("Liên hệ: 0123456789");
    }
}
