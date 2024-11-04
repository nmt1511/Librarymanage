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

        ImageView imageView = convertView.findViewById(R.id.imageViewBook);
        TextView titleTextView = convertView.findViewById(R.id.textViewTitle);
        TextView authorTextView = convertView.findViewById(R.id.textViewAuthor);

        imageView.setImageResource(book.getImageResource());
        titleTextView.setText(book.getTitle());
        authorTextView.setText(book.getAuthor());

        return convertView;
    }

}
