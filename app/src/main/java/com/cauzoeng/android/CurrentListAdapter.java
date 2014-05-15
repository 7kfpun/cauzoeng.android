package com.cauzoeng.android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;


public class CurrentListAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] titles;
    private final Double[] prices;
    private final String[] currencies;
    private final String[] descriptions;
    private final String[] created_dates;
    private final Integer[] imageIds;

    public CurrentListAdapter(Activity context,
                              String[] titles, Double[] prices, String[] currencies,
                              String[] descriptions, String[] created_dates, Integer[] imageIds) {
        super(context, R.layout.listview_current_item, titles);
        this.context = context;

        this.titles = titles;
        this.prices = prices;
        this.currencies = currencies;
        this.descriptions = descriptions;
        this.created_dates = created_dates;
        this.imageIds = imageIds;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_current_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.title);
        TextView txtPrice = (TextView) rowView.findViewById(R.id.price);
        TextView txtDescription = (TextView) rowView.findViewById(R.id.description);
        TextView txtCreatedDate = (TextView) rowView.findViewById(R.id.created_date);
        TextView txtCount = (TextView) rowView.findViewById(R.id.available_days);

        txtTitle.setText(titles[position]);
        txtPrice.setText(currencies[position] + ' ' + prices[position].toString());
        txtDescription.setText(descriptions[position]);
        txtCreatedDate.setText(created_dates[position].split("T")[0]);
        txtCount.setText("324 人次");

        String imageUrl = "http://img.1ting.com/images/album/0706/s300_20066574366.jpg";
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        Ion.with(imageView)
                .placeholder(R.drawable.ic_launcher)
                .load(imageUrl);

        return rowView;
    }
}