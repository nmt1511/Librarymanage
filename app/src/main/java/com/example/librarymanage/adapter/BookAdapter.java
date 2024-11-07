package com.example.librarymanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        if (imageView != null) {
            imageView.setImageResource(book.getImageResource());
        }

        return convertView;
    }


}
