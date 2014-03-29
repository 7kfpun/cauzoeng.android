package com.cauzoeng.android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] web;
    private final String[] description;
    private final Integer[] imageId;

    public CustomList(Activity context,
                      String[] web, String[] description, Integer[] imageId) {
        super(context, R.layout.listview_detailed_item, web);
        this.context = context;
        this.web = web;
        this.description = description;
        this.imageId = imageId;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_detailed_item, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView txtDescription = (TextView) rowView.findViewById(R.id.description);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web[position]);
        txtDescription.setText(description[position]);
        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}