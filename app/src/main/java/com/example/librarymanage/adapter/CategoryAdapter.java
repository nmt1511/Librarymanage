package com.example.librarymanage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.librarymanage.R;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> categories;

    public CategoryAdapter(Context context, List<String> categories) {
        super(context, android.R.layout.simple_list_item_1, categories);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.category_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.textViewCategory);
        textView.setText(categories.get(position));
        return rowView;
    }
}