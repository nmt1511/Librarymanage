package com.example.librarymanage.entities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.librarymanage.R;


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

        // Kiểm tra và tải ảnh nếu có URL hợp lệ
        if (imageView != null) {
            String imageUrl = book.getImageResource();

            if (imageUrl != null && !imageUrl.isEmpty()) {
                try {
                    // Kiểm tra URL hợp lệ và tải ảnh nếu có thể
                    Glide.with(getContext())
                            .load(imageUrl)  // URL của hình ảnh
                            .into(imageView); // Tải ảnh vào ImageView
                } catch (Exception e) {
                    // Nếu xảy ra lỗi khi tải ảnh, hiển thị ảnh mặc định
                    Glide.with(getContext())
                            .load(R.drawable.ic_open_book)  // Hình ảnh mặc định
                            .into(imageView); // Tải hình ảnh mặc định vào ImageView
                }
            } else {
                // Nếu không có URL, hiển thị hình ảnh mặc định
                Glide.with(getContext())
                        .load(R.drawable.ic_open_book)  // Hình ảnh mặc định
                        .into(imageView);  // Tải hình ảnh mặc định vào ImageView
            }
        }

        return convertView;
    }

}
