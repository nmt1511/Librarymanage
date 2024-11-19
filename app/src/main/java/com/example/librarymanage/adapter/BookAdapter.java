package com.example.librarymanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.librarymanage.R;
import com.example.librarymanage.entities.Book;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book book = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_book, parent, false);
        }

        // Tìm các view dựa trên ID trong XML
        ImageView imageView = convertView.findViewById(R.id.imageViewBook);
        TextView titleTextView = convertView.findViewById(R.id.textViewTitle);
        TextView authorTextView = convertView.findViewById(R.id.tvAuthor);
        TextView categoryTextView = convertView.findViewById(R.id.tvCategory);
        TextView locationTextView = convertView.findViewById(R.id.tvLocation);
        TextView descriptionTextView = convertView.findViewById(R.id.tvDescription);

        // Kiểm tra và đặt nội dung cho từng view
        if (titleTextView != null) {
            titleTextView.setText(book.getTitle());
        }
        if (authorTextView != null) {
            authorTextView.setText(book.getAuthorName());
        }
        if (categoryTextView != null) {
            categoryTextView.setText(book.getCategoryName());
        }
        if (locationTextView != null) {
            locationTextView.setText(book.getLocationName());
        }
        if (descriptionTextView != null) {
            descriptionTextView.setText(book.getDescription());
        }
        String imageUrl = book.getImageResource();
        // Kiểm tra và tải ảnh nếu có URL hợp lệ
        if (imageView != null && isValidFilePath(imageUrl)) {


            // Kiểm tra URL hợp lệ
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // Tải ảnh từ URL
                Glide.with(getContext())
                        .load(imageUrl)  // URL của hình ảnh
                        .into(imageView); // Tải ảnh vào ImageView
            }
        }else {
            // Nếu không có URL hợp lệ, hiển thị hình ảnh mặc định
            imageView.setImageResource(R.drawable.ic_open_book);
        }


        return convertView;
    }
    private boolean isValidFilePath(String path) {
        // Kiểm tra xem đường dẫn có bắt đầu bằng "/data/user/0/" (đường dẫn tệp nội bộ)
        return path != null && path.startsWith("/data/user/0/");
    }


}
