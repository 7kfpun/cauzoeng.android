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
    private final String[] subjects;
    private final String[] descriptions;
    private final String[] finish_dates;
    private final Integer[] imageIds;

    public CustomList(Activity context,
                      String[] web, String[] description,
                      String[] finish_date, Integer[] imageId) {
        super(context, R.layout.listview_detailed_item, web);
        this.context = context;

        this.subjects = web;
        this.descriptions = description;
        this.finish_dates = finish_date;
        this.imageIds = imageId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_detailed_item, null, true);
        TextView txtSubject = (TextView) rowView.findViewById(R.id.subject);
        TextView txtDescription = (TextView) rowView.findViewById(R.id.description);
        TextView txtFinishDate = (TextView) rowView.findViewById(R.id.finish_date);
        TextView txtCount = (TextView) rowView.findViewById(R.id.count);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

        txtSubject.setText(subjects[position]);
        txtDescription.setText(descriptions[position]);
        txtFinishDate.setText(finish_dates[position].split("T")[0]);
        txtFinishDate.setText("324 人次.");
        imageView.setImageResource(imageIds[position]);
        return rowView;
    }
}