package com.example.librarymanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AuthorAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> authors;

    public AuthorAdapter(Context context, List<String> authors) {
        super(context, android.R.layout.simple_list_item_1, authors);
        this.context = context;
        this.authors = authors;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView textView = (TextView) rowView.findViewById(android.R.id.text1);
        textView.setText(authors.get(position));
        return rowView;
    }
}