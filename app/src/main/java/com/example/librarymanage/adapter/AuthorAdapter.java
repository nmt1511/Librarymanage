package com.example.librarymanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.example.librarymanage.R;
import com.example.librarymanage.entities.Author;

import java.util.ArrayList;

public class AuthorAdapter extends ArrayAdapter<Author> {

    public AuthorAdapter(Context context, ArrayList<Author> authors) {
        super(context, 0, authors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_author, parent, false);
        }

        Author author = getItem(position);
        TextView tvAuthorName = convertView.findViewById(R.id.tv_author_name);
        tvAuthorName.setText(author.getName());

        return convertView;
    }
}
