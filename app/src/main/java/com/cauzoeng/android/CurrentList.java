package com.cauzoeng.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class CurrentList extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] subjects;
    private final Double[] prices;
    private final String[] descriptions;
    private final String[] finish_dates;
    private final int[] imageIds;

    public CurrentList(Activity context,
                       String[] subjects, Double[] prices,String[] descriptions,
                       String[] finish_dates, int[] imageIds) {
        super(context, R.layout.listview_current_item, subjects);
        this.context = context;

        this.subjects = subjects;
        this.prices = prices;
        this.descriptions = descriptions;
        this.finish_dates = finish_dates;
        this.imageIds = imageIds;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_current_item, null, true);
        TextView txtSubject = (TextView) rowView.findViewById(R.id.subject);
        TextView txtPrice = (TextView) rowView.findViewById(R.id.price);
        TextView txtDescription = (TextView) rowView.findViewById(R.id.description);
        TextView txtFinishDate = (TextView) rowView.findViewById(R.id.finish_date);
        TextView txtCount = (TextView) rowView.findViewById(R.id.count);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

        txtSubject.setText(subjects[position]);
        txtPrice.setText("HKD " + prices[position].toString());
        txtDescription.setText(descriptions[position]);
        txtFinishDate.setText(finish_dates[position].split("T")[0]);
        txtCount.setText("324 人次");
        imageView.setImageResource(imageIds[position]);
        return rowView;
    }
}